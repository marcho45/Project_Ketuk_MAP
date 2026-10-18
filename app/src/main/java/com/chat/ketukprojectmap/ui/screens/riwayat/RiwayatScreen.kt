package com.chat.ketukprojectmap.ui.screens.riwayat

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.data.dummy.ActivityCategory
import com.chat.ketukprojectmap.data.dummy.ActivityItem
import com.chat.ketukprojectmap.data.dummy.EscalationLevel
import com.chat.ketukprojectmap.ui.theme.*

@Composable
fun RiwayatScreen(
    viewModel: RiwayatViewModel = viewModel(),
    onItemClick: (String) -> Unit = {}
) {
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val filteredItems by viewModel.filteredItems.collectAsState()
    val colors = KetukTheme.colors

    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.screenBackground)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .align(Alignment.TopCenter)
                .padding(bottom = 64.dp + navBarPadding + 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Riwayat aktivitas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    label = "Semua",
                    isSelected = selectedFilter == RiwayatFilter.SEMUA,
                    onClick = { viewModel.selectFilter(RiwayatFilter.SEMUA) }
                )
                FilterChip(
                    label = "Aktivitas",
                    isSelected = selectedFilter == RiwayatFilter.AKTIVITAS,
                    onClick = { viewModel.selectFilter(RiwayatFilter.AKTIVITAS) }
                )
                FilterChip(
                    label = "HP",
                    isSelected = selectedFilter == RiwayatFilter.HP,
                    onClick = { viewModel.selectFilter(RiwayatFilter.HP) }
                )
                FilterChip(
                    label = "Sapa",
                    isSelected = selectedFilter == RiwayatFilter.SAPA,
                    onClick = { viewModel.selectFilter(RiwayatFilter.SAPA) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada riwayat",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = filteredItems,
                        key = { it.id }
                    ) { item ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(300)) +
                                    expandVertically(animationSpec = tween(300)),
                            exit = fadeOut(animationSpec = tween(300)) +
                                   shrinkVertically(animationSpec = tween(300))
                        ) {
                            RiwayatItemCard(
                                item = item,
                                onClick = { onItemClick(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = KetukTheme.colors
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else colors.chipFilter,
        animationSpec = tween(durationMillis = 300),
        label = "chipBackgroundColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) colors.onAccent else colors.textPrimary,
        animationSpec = tween(durationMillis = 300),
        label = "chipTextColor"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier.heightIn(min = 36.dp),
        shape = RoundedCornerShape(50),
        color = backgroundColor
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            )
        }
    }
}

@Composable
fun RiwayatItemCard(
    item: ActivityItem,
    onClick: () -> Unit = {}
) {
    val colors = KetukTheme.colors
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
        border = BorderStroke(1.dp, colors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val (iconColor, icon) = when {
                item.level == EscalationLevel.L3 -> Terakota to Icons.Rounded.Notifications
                item.level == EscalationLevel.L2 || item.level == EscalationLevel.L1 -> IkonOranye to Icons.Rounded.Notifications
                item.type == ActivityCategory.SAPA -> IkonBiru to Icons.Rounded.ChatBubble
                item.type == ActivityCategory.HP -> IkonHijau to Icons.Rounded.PhoneAndroid
                else -> IkonHijau to Icons.Rounded.Check
            }

            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = iconColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.onAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (!item.quote.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "\"${item.quote}\"",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.timestamp,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = "Lihat detail",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RiwayatScreenPreview() {
    KetukTheme {
        RiwayatScreen()
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 568)
@Composable
fun RiwayatScreenPreviewSmall() {
    KetukTheme {
        RiwayatScreen()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, fontScale = 1.3f)
@Composable
fun RiwayatScreenPreviewLargeFont() {
    KetukTheme {
        RiwayatScreen()
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 1280)
@Composable
fun RiwayatScreenPreviewTablet() {
    KetukTheme {
        RiwayatScreen()
    }
}

@Preview(name = "Mode Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RiwayatScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        RiwayatScreen()
    }
}

