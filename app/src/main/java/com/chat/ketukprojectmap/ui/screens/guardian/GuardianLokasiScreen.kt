package com.chat.ketukprojectmap.ui.screens.guardian

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.data.dummy.DummyData
import com.chat.ketukprojectmap.ui.components.FadeSlideIn
import com.chat.ketukprojectmap.ui.theme.KetukTheme
import com.chat.ketukprojectmap.ui.theme.Terakota
import kotlinx.coroutines.launch

/**
 * Tab 1 Penjaga: Lokasi Oma Diyas (Sesuai 05. Core Phase Penjaga.png frame 2).
 */
@Composable
fun GuardianLokasiScreen(
    onOpenLocationHistory: () -> Unit = {},
    snackbarHostState: SnackbarHostState? = null
) {
    val colors = KetukTheme.colors
    val scope = rememberCoroutineScope()
    val elder = DummyData.defaultElder
    val navBarInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 64.dp + navBarInset + 32.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Judul Tab
        FadeSlideIn(delayMillis = 0) {
            Text(
                text = "Lokasi Oma Diyas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kartu Profil Oma Melayang di Atas Peta
        FadeSlideIn(delayMillis = 80) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            snackbarHostState?.currentSnackbarData?.dismiss()
                            snackbarHostState?.showSnackbar("Simulasi Mockup: Oma Diyas berada di rumah dalam radius aman")
                        }
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                border = BorderStroke(1.dp, colors.cardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.oma_avatar),
                        contentDescription = elder.nickname,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = colors.accentGreen.copy(alpha = 0.16f),
                            border = BorderStroke(1.dp, colors.accentGreen.copy(alpha = 0.35f))
                        ) {
                            Text(
                                text = elder.safetyBadge,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.accentGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${elder.nickname} 170 m darimu",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Di rumah • ${elder.address}",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textSecondary
                        )
                    }

                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint = colors.textSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Viewport Peta Statis dengan Kontrol Melayang
        FadeSlideIn(delayMillis = 160) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(BorderStroke(1.dp, colors.cardBorder), RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.peta_lokasi),
                    contentDescription = "Peta Lokasi Oma Diyas",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Tombol Kontrol Peta (Kanan Bawah: Kompas, +, -)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Kompas
                    MapControlButton(
                        icon = Icons.Rounded.Explore,
                        contentDescription = "Kompas",
                        onClick = {
                            scope.launch {
                                snackbarHostState?.currentSnackbarData?.dismiss()
                                snackbarHostState?.showSnackbar("Simulasi Mockup: Kompas diarahkan ke Utara")
                            }
                        }
                    )

                    // Zoom In (+)
                    MapControlButton(
                        icon = Icons.Rounded.Add,
                        contentDescription = "Perbesar Peta",
                        onClick = {
                            scope.launch {
                                snackbarHostState?.currentSnackbarData?.dismiss()
                                snackbarHostState?.showSnackbar("Simulasi Mockup: Perbesar peta (+)")
                            }
                        }
                    )

                    // Zoom Out (-)
                    MapControlButton(
                        icon = Icons.Rounded.Remove,
                        contentDescription = "Perkecil Peta",
                        onClick = {
                            scope.launch {
                                snackbarHostState?.currentSnackbarData?.dismiss()
                                snackbarHostState?.showSnackbar("Simulasi Mockup: Perkecil peta (-)")
                            }
                        }
                    )
                }

                // Keterangan Update Waktu Terakhir (Kiri Bawah)
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = colors.cardSurface.copy(alpha = 0.92f),
                    border = BorderStroke(1.dp, colors.cardBorder),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AccessTime,
                            contentDescription = null,
                            tint = Terakota,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Lokasi terakhir: ${elder.lastLocationUpdate}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = colors.textPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kartu Navigasi ke "Riwayat Lokasi" 7 Hari
        FadeSlideIn(delayMillis = 240) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenLocationHistory),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                border = BorderStroke(1.dp, colors.cardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Riwayat Lokasi",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Lihat pergerakan Oma dalam 7 hari terakhir",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textSecondary
                        )
                    }

                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = "Buka Riwayat Lokasi",
                        tint = Terakota,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MapControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    val colors = KetukTheme.colors
    Surface(
        onClick = onClick,
        modifier = Modifier
            .size(38.dp)
            .shadow(3.dp, CircleShape),
        shape = CircleShape,
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.cardBorder)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = colors.textPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(name = "Guardian Lokasi - Light", showBackground = true)
@Composable
private fun PreviewGuardianLokasiLight() {
    KetukTheme(darkTheme = false) {
        GuardianLokasiScreen()
    }
}

@Preview(name = "Guardian Lokasi - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewGuardianLokasiDark() {
    KetukTheme(darkTheme = true) {
        GuardianLokasiScreen()
    }
}

@Preview(name = "Guardian Lokasi - 320dp", widthDp = 320, heightDp = 640, showBackground = true)
@Composable
private fun PreviewGuardianLokasi320() {
    KetukTheme(darkTheme = false) {
        GuardianLokasiScreen()
    }
}

@Preview(name = "Guardian Lokasi - 360dp", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun PreviewGuardianLokasi360() {
    KetukTheme(darkTheme = false) {
        GuardianLokasiScreen()
    }
}

@Preview(name = "Guardian Lokasi - Aksesibilitas 360dp fontScale 1.3", widthDp = 360, heightDp = 800, fontScale = 1.3f, showBackground = true)
@Composable
private fun PreviewGuardianLokasiAccessibility() {
    KetukTheme(darkTheme = false) {
        GuardianLokasiScreen()
    }
}
