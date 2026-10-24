package com.chat.ketukprojectmap.ui.screens.notifikasi

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.ketukprojectmap.data.dummy.DummyData
import com.chat.ketukprojectmap.data.dummy.EscalationLevel
import com.chat.ketukprojectmap.data.dummy.NotificationItem
import com.chat.ketukprojectmap.ui.components.FadeSlideIn
import com.chat.ketukprojectmap.ui.components.staggerDelay
import com.chat.ketukprojectmap.ui.theme.*

// TODO(tim): Layar Notifikasi tidak punya frame khusus di Figma 01-05; diisi dengan 5 item DummyData.initialNotifications
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifikasiScreen(
    onNavigateBack: () -> Unit = {},
    onOpenDetailAlert: (String) -> Unit = {}
) {
    val colors = KetukTheme.colors
    var unreadIds by rememberSaveable {
        mutableStateOf(
            DummyData.initialNotifications.filter { it.isUnread }.map { it.id }
        )
    }
    val notifications = remember(unreadIds) {
        DummyData.initialNotifications.map { item ->
            item.copy(isUnread = item.id in unreadIds)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifikasi",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Kembali",
                            tint = colors.textPrimary
                        )
                    }
                },
                actions = {
                    if (notifications.any { it.isUnread }) {
                        TextButton(
                            onClick = {
                                unreadIds = emptyList()
                            }
                        ) {
                            Text(
                                text = "Tandai dibaca",
                                color = Terakota,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.screenBackground
                )
            )
        },
        containerColor = colors.screenBackground
    ) { paddingValues ->
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.screenBackground)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada notifikasi",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.screenBackground)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                itemsIndexed(
                    items = notifications,
                    key = { _, item -> item.id }
                ) { index, item ->
                    if (index < 6) {
                        FadeSlideIn(delayMillis = staggerDelay(index, baseDelayMillis = 40)) {
                            NotificationCard(
                                item = item,
                                onClick = {
                                    if (item.id in unreadIds) {
                                        unreadIds = unreadIds - item.id
                                    }
                                    item.relatedActivityId?.let { actId ->
                                        onOpenDetailAlert(actId)
                                    }
                                }
                            )
                        }
                    } else {
                        NotificationCard(
                            item = item,
                            onClick = {
                                if (item.id in unreadIds) {
                                    unreadIds = unreadIds - item.id
                                }
                                item.relatedActivityId?.let { actId ->
                                    onOpenDetailAlert(actId)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    item: NotificationItem,
    onClick: () -> Unit
) {
    val colors = KetukTheme.colors
    val (badgeColor, icon) = when (item.level) {
        EscalationLevel.L3 -> Terakota to Icons.Rounded.WarningAmber
        EscalationLevel.L2, EscalationLevel.L1 -> colors.accentAmber to Icons.Rounded.NotificationsActive
        EscalationLevel.NORMAL -> colors.accentGreen to Icons.Rounded.CheckCircle
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isUnread) colors.cardHighlight else colors.cardSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (item.isUnread) Terakota.copy(alpha = 0.4f) else colors.cardBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = badgeColor.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    if (item.isUnread) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Terakota, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.timestamp,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = "Lihat detail",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotifikasiScreenPreview() {
    KetukTheme {
        NotifikasiScreen()
    }
}

@Preview(name = "Mode Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotifikasiScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        NotifikasiScreen()
    }
}