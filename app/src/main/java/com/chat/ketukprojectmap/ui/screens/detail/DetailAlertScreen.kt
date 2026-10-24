package com.chat.ketukprojectmap.ui.screens.detail

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.data.dummy.DummyData
import com.chat.ketukprojectmap.data.dummy.EscalationLevel
import com.chat.ketukprojectmap.ui.components.FadeSlideIn
import com.chat.ketukprojectmap.ui.theme.*
import kotlinx.coroutines.launch

// TODO(tim): layar detail tanpa frame figma khusus (mengikuti flowchart M8 & rubrik Detail Screen UTS)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailAlertScreen(
    itemId: String,
    onNavigateBack: () -> Unit
) {
    val colors = KetukTheme.colors
    val item = remember(itemId) { DummyData.findActivityById(itemId) }
    val elder = DummyData.defaultElder
    val guardian = DummyData.primaryGuardian
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var currentStatusText by rememberSaveable(itemId) {
        mutableStateOf(item?.status ?: "Tidak diketahui")
    }

    Scaffold(
        containerColor = colors.screenBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Aktivitas",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.screenBackground
                )
            )
        }
    ) { paddingValues ->
        if (item == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        tint = Terakota,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Data aktivitas (ID: $itemId) tidak ditemukan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary
                    )
                }
            }
        } else {
            val levelBadgeColor = when (item.level) {
                EscalationLevel.NORMAL -> colors.chipSehat
                EscalationLevel.L1 -> colors.aksiSapa
                EscalationLevel.L2, EscalationLevel.L3 -> colors.chipFilter
            }
            val levelTextColor = when (item.level) {
                EscalationLevel.NORMAL -> colors.accentGreen
                EscalationLevel.L1 -> colors.accentAmber
                EscalationLevel.L2, EscalationLevel.L3 -> Terakota
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                // Kartu Ringkasan Utama Aktivitas / Alert
                FadeSlideIn(delayMillis = 0) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                        border = BorderStroke(1.dp, colors.cardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = colors.activePill
                                ) {
                                    Text(
                                        text = "Kategori: ${item.type.label}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colors.textPrimary,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = levelBadgeColor
                                ) {
                                    Text(
                                        text = item.level.label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = levelTextColor,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = item.title,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Schedule,
                                    contentDescription = null,
                                    tint = colors.textPrimary.copy(alpha = 0.6f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = item.timestamp,
                                    fontSize = 13.sp,
                                    color = colors.textPrimary.copy(alpha = 0.7f)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = colors.cardBorder)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Status Terkini",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.textPrimary.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentStatusText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.accentGreen
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Rincian Kejadian",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.textPrimary.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.description,
                                fontSize = 14.sp,
                                color = colors.textPrimary,
                                lineHeight = 20.sp
                            )

                            if (item.quote != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = colors.cardHighlight,
                                    border = BorderStroke(1.dp, colors.cardBorder)
                                ) {
                                    Text(
                                        text = "\"${item.quote}\"",
                                        fontSize = 13.sp,
                                        fontStyle = FontStyle.Italic,
                                        color = colors.textPrimary,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Kartu Informasi Lansia Terkait
                FadeSlideIn(delayMillis = 80) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                        border = BorderStroke(1.dp, colors.cardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.oma_avatar),
                                contentDescription = elder.nickname,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${elder.nickname} (${elder.fullName})",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                                Text(
                                    text = "${elder.age} tahun \u2022 ${elder.address}",
                                    fontSize = 12.sp,
                                    color = colors.textPrimary.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                FadeSlideIn(delayMillis = 160) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Tindakan Wali",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Tombol Aksi 1: Telepon Oma
                        Button(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Simulasi Mockup: Menelepon ${elder.nickname} (${elder.fullName})..."
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = Terakota)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Call,
                                contentDescription = null,
                                tint = colors.onAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Telepon Oma",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.onAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Tombol Aksi 2: Konfirmasi Oma Aman
                        Button(
                            onClick = {
                                currentStatusText = "Aman - Dikonfirmasi oleh Wali"
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Simulasi Mockup: Kondisi ${elder.nickname} telah dikonfirmasi aman"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = HijauDaun)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = colors.onAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Konfirmasi Oma Aman",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.onAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Tombol Aksi 3: Minta Penjaga Mengecek
                        OutlinedButton(
                            onClick = {
                                currentStatusText = "Menunggu Penjaga (${guardian.name}) mengecek"
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Simulasi Mockup: Permintaan ketuk pintu dikirim ke ${guardian.name} (${guardian.distanceText})"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.5.dp, Terakota),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = colors.cardSurface,
                                contentColor = Terakota
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DirectionsWalk,
                                contentDescription = null,
                                tint = Terakota,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Minta Penjaga Mengecek",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Terakota
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailAlertScreenPreview() {
    KetukTheme {
        DetailAlertScreen(itemId = "act_2", onNavigateBack = {})
    }
}

@Preview(name = "Mode Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetailAlertScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        DetailAlertScreen(itemId = "act_2", onNavigateBack = {})
    }
}