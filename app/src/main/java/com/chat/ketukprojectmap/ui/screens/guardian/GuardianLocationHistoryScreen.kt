package com.chat.ketukprojectmap.ui.screens.guardian

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.ketukprojectmap.data.dummy.DummyData
import com.chat.ketukprojectmap.data.dummy.LocationHistoryItem
import com.chat.ketukprojectmap.ui.components.FadeSlideIn
import com.chat.ketukprojectmap.ui.components.staggerDelay
import com.chat.ketukprojectmap.ui.theme.KetukTheme
import com.chat.ketukprojectmap.ui.theme.Terakota
import kotlinx.coroutines.launch

// TODO(tim): Layar Riwayat Lokasi 7 hari tidak memiliki frame khusus di Figma 05;
// dibuat untuk memenuhi rubrik List Screen dan melengkapi fitur kartu "Riwayat Lokasi"
@Composable
fun GuardianLocationHistoryScreen(
    onNavigateBack: () -> Unit = {}
) {
    val colors = KetukTheme.colors
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val historyList = DummyData.locationHistory7Days

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colors.screenBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Top Bar
            FadeSlideIn(delayMillis = 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Kembali ke Lokasi Penjaga",
                            tint = colors.textPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = "Riwayat Lokasi",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        Text(
                            text = "Pergerakan Oma Diyas dalam 7 hari terakhir",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textSecondary
                        )
                    }
                }
            }

            HorizontalDivider(color = colors.divider)

            // Daftar 7 Hari Pergerakan
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                itemsIndexed(historyList, key = { _, item -> item.id }) { index, item ->
                    if (index < 6) {
                        FadeSlideIn(delayMillis = staggerDelay(index, baseDelayMillis = 40)) {
                            LocationHistoryCard(
                                item = item,
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                        snackbarHostState.showSnackbar("Simulasi Mockup: Membuka koordinat ${item.placeName}")
                                    }
                                }
                            )
                        }
                    } else {
                        LocationHistoryCard(
                            item = item,
                            onClick = {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Simulasi Mockup: Membuka koordinat ${item.placeName}")
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
private fun LocationHistoryCard(
    item: LocationHistoryItem,
    onClick: () -> Unit
) {
    val colors = KetukTheme.colors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
        border = BorderStroke(1.dp, colors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Baris Atas: Badge Hari & Jam
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Terakota.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = item.dayLabel,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Terakota
                    )
                }

                Text(
                    text = "${item.dateText} • ${item.timeText}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Nama Tempat & Alamat
            Text(
                text = item.placeName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.address,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = colors.divider.copy(alpha = 0.5f))

            Spacer(modifier = Modifier.height(10.dp))

            // Baris Bawah: Jarak & Catatan Aktivitas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        tint = Terakota,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.distanceFromHome,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = colors.accentGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.statusNote,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.accentGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(name = "Riwayat Lokasi - Light", showBackground = true)
@Composable
private fun PreviewLocationHistoryLight() {
    KetukTheme(darkTheme = false) {
        GuardianLocationHistoryScreen()
    }
}

@Preview(name = "Riwayat Lokasi - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLocationHistoryDark() {
    KetukTheme(darkTheme = true) {
        GuardianLocationHistoryScreen()
    }
}
