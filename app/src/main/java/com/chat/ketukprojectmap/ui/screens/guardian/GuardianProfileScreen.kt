package com.chat.ketukprojectmap.ui.screens.guardian

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.data.dummy.DummyData
import com.chat.ketukprojectmap.ui.components.FadeSlideIn
import com.chat.ketukprojectmap.ui.components.KetukDarkModeCard
import com.chat.ketukprojectmap.ui.components.KetukLogoutButton
import com.chat.ketukprojectmap.ui.screens.profile.ProfileViewModel
import com.chat.ketukprojectmap.ui.theme.KetukTheme
import com.chat.ketukprojectmap.ui.theme.Terakota

// TODO(tim): Layar Profil Penjaga tidak memiliki frame khusus di Figma 05;
// dibuat mengikuti sistem desain Ketuk, memuat info Markolintang, lansia yang dijaga, switch tema, dan logout
@Composable
fun GuardianProfileScreen(
    onLogout: () -> Unit = {},
    profileViewModel: ProfileViewModel = viewModel()
) {
    val colors = KetukTheme.colors
    val guardian = DummyData.primaryGuardian
    val elder = DummyData.defaultElder
    val navBarInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val isLoggingOut by profileViewModel.isLoggingOut.collectAsState()
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }

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
                text = "Profil Penjaga",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // KARTU 1: Info Akun Penjaga (Markolintang)
        FadeSlideIn(delayMillis = 80) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                border = BorderStroke(1.dp, colors.cardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            shape = CircleShape,
                            color = Terakota.copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    tint = Terakota,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = guardian.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Terakota.copy(alpha = 0.14f)
                            ) {
                                Text(
                                    text = "Penjaga Terdekat",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Terakota
                                )
                            }
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 14.dp),
                        color = colors.divider
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Email,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "penjaga@ketuk.app",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Phone,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = guardian.phone,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // KARTU 2: Status Kesediaan & Radius
        FadeSlideIn(delayMillis = 160) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                border = BorderStroke(1.dp, colors.cardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = colors.accentGreen.copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.Shield,
                                    contentDescription = null,
                                    tint = colors.accentGreen,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Status Penjaga",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Text(
                                text = "Siap Membantu • Aktif",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.accentGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Radius kesiapan 200 m di sekitar Jl. Syeckh Nawawi Albantani. Notifikasi ketuk pintu akan langsung berdering saat eskalasi L3.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // KARTU 3: Lansia yang Dipantau (Oma Diyas)
        FadeSlideIn(delayMillis = 240) {
            Column {
                Text(
                    text = "Lansia yang dipantau",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                    border = BorderStroke(1.dp, colors.cardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                                Text(
                                    text = "${elder.nickname} (${elder.fullName})",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${elder.age} Tahun • 170 m darimu",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.textSecondary
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = colors.divider
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Home,
                                contentDescription = null,
                                tint = colors.accentGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${elder.area} - ${elder.address}",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textPrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // KARTU 4: Pengaturan Mode Gelap
        FadeSlideIn(delayMillis = 320) {
            KetukDarkModeCard()
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Keluar (Logout)
        FadeSlideIn(delayMillis = 400) {
            KetukLogoutButton(
                onClick = {
                    if (!isLoggingOut) {
                        showLogoutDialog = true
                    }
                },
                isLoggingOut = isLoggingOut,
                text = "Keluar",
                loadingText = "Keluar..."
            )
        }
    }

    // Dialog Konfirmasi Keluar
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isLoggingOut) {
                    showLogoutDialog = false
                }
            },
            containerColor = colors.cardSurface,
            title = {
                Text(
                    text = "Keluar dari Akun Penjaga?",
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            },
            text = {
                Text(
                    text = "Anda akan kembali ke layar masuk dan pemantauan siaga ketuk pintu akan dinonaktifkan.",
                    color = colors.textSecondary
                )
            },
            confirmButton = {
                TextButton(
                    enabled = !isLoggingOut,
                    onClick = {
                        profileViewModel.logout {
                            showLogoutDialog = false
                            onLogout()
                        }
                    }
                ) {
                    if (isLoggingOut) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = Terakota
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (isLoggingOut) "Sedang keluar..." else "Ya, Keluar",
                        color = Terakota,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isLoggingOut,
                    onClick = { showLogoutDialog = false }
                ) {
                    Text(
                        text = "Batal",
                        color = colors.textSecondary
                    )
                }
            }
        )
    }
}

@Preview(name = "Profil Penjaga - Light", showBackground = true)
@Composable
private fun PreviewGuardianProfileLight() {
    KetukTheme(darkTheme = false) {
        GuardianProfileScreen()
    }
}

@Preview(name = "Profil Penjaga - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewGuardianProfileDark() {
    KetukTheme(darkTheme = true) {
        GuardianProfileScreen()
    }
}

@Preview(name = "Profil Penjaga - 320dp", widthDp = 320, heightDp = 640, showBackground = true)
@Composable
private fun PreviewGuardianProfile320() {
    KetukTheme(darkTheme = false) {
        GuardianProfileScreen()
    }
}

@Preview(name = "Profil Penjaga - 360dp", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun PreviewGuardianProfile360() {
    KetukTheme(darkTheme = false) {
        GuardianProfileScreen()
    }
}

@Preview(name = "Profil Penjaga - Aksesibilitas 360dp fontScale 1.3", widthDp = 360, heightDp = 800, fontScale = 1.3f, showBackground = true)
@Composable
private fun PreviewGuardianProfileAccessibility() {
    KetukTheme(darkTheme = false) {
        GuardianProfileScreen()
    }
}
