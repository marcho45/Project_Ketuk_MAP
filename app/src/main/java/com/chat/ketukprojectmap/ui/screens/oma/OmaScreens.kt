package com.chat.ketukprojectmap.ui.screens.oma

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.data.dummy.DummyData
import com.chat.ketukprojectmap.ui.components.FadeSlideIn
import com.chat.ketukprojectmap.ui.components.KetukButton
import com.chat.ketukprojectmap.ui.components.KetukDarkModeCard
import com.chat.ketukprojectmap.ui.components.KetukLogoutButton
import com.chat.ketukprojectmap.ui.screens.profile.ProfileViewModel
import com.chat.ketukprojectmap.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ======================================================================
// 1. SCREEN M4: PINDAI QR (03. Initial Phase oma.png)
// ======================================================================

@Composable
fun ScanQrScreen(
    onNavigateBack: () -> Unit,
    onScanSuccess: () -> Unit
) {
    val colors = KetukTheme.colors
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isFlashOn by rememberSaveable { mutableStateOf(false) }
    var isSimulatingLoading by rememberSaveable { mutableStateOf(false) }

    // Animasi garis pemindai laser naik dan turun
    val infiniteTransition = rememberInfiniteTransition(label = "scan_laser")
    val laserPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_pos"
    )

    BackHandler(enabled = isSimulatingLoading) {
        // no-op: abaikan back selama loading pairing
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colors.screenBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Judul & Subjudul
                FadeSlideIn(delayMillis = 0) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Pindai QR",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pindai QR untuk terhubung dengan Wali",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textSecondary,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Viewport Mockup Kamera Statis
                FadeSlideIn(delayMillis = 80) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(380.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(colors.scannerViewport),
                        contentAlignment = Alignment.Center
                    ) {
                        // Tampilan Kartu QR Mockup di tengah bidikan kamera
                        Card(
                            modifier = Modifier
                                .size(240.dp)
                                .padding(16.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.qrSurface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.qr_pairing),
                                    contentDescription = "QR Code Wali",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }

                        // Bingkai Reticle Sudut Kamera (White scan reticles)
                        ScanCornerReticle(
                            modifier = Modifier.size(230.dp)
                        )

                        // Garis Laser Animasi
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .height(3.dp)
                                .align(Alignment.TopCenter)
                                .offset(y = (90.dp + (laserPosition * 190).dp))
                                .background(Terakota)
                        )

                        // Tombol Kontrol Flash Mockup di pojok kanan atas viewfinder
                        IconButton(
                            onClick = {
                                isFlashOn = !isFlashOn
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        "Simulasi Mockup: Flash kamera ${if (isFlashOn) "diaktifkan" else "dimatikan"}"
                                    )
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(colors.overlayScrim)
                        ) {
                            Icon(
                                imageVector = if (isFlashOn) Icons.Rounded.FlashOn else Icons.Rounded.FlashOff,
                                contentDescription = "Flash Kamera",
                                tint = if (isFlashOn) colors.accentAmber else colors.onAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Kartu Petunjuk Kamera (Ikon Bohlam + Teks)
                FadeSlideIn(delayMillis = 160) {
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
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = colors.accentAmber.copy(alpha = 0.16f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Rounded.Lightbulb,
                                        contentDescription = null,
                                        tint = colors.accentAmber,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "Arahkan ke QR code di HP wali",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.textPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Pastikan QR code terlihat jelas di dalam bingkai",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.textSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Bantuan Simulasi Scan
                FadeSlideIn(delayMillis = 240) {
                    KetukButton(
                        text = "Simulasikan Pindai",
                        onClick = {
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("Simulasi Mockup: QR code terdeteksi!")
                            }
                            isSimulatingLoading = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Transisi Halus Menuju Loading Pairing (2 Detik)
            AnimatedVisibility(
                visible = isSimulatingLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LoadingPairingOmaContent(
                    onPairingComplete = onScanSuccess
                )
            }
        }
    }
}

/** Bingkai sudut putih khas reticle viewfinder kamera pemindai QR */
@Composable
private fun ScanCornerReticle(modifier: Modifier = Modifier) {
    val cornerSize = 24.dp
    val strokeWidth = 3.dp
    val color = KetukTheme.colors.onAccent.copy(alpha = 0.85f)

    Box(modifier = modifier) {
        // Kiri Atas
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(cornerSize)
                .drawCornerReticle(strokeWidth, color, top = true, left = true)
        )
        // Kanan Atas
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(cornerSize)
                .drawCornerReticle(strokeWidth, color, top = true, left = false)
        )
        // Kiri Bawah
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(cornerSize)
                .drawCornerReticle(strokeWidth, color, top = false, left = true)
        )
        // Kanan Bawah
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(cornerSize)
                .drawCornerReticle(strokeWidth, color, top = false, left = false)
        )
    }
}

private fun Modifier.drawCornerReticle(
    strokeWidth: androidx.compose.ui.unit.Dp,
    color: Color,
    top: Boolean,
    left: Boolean
): Modifier = this.border(
    width = strokeWidth,
    color = color,
    shape = RoundedCornerShape(
        topStart = if (top && left) 8.dp else 0.dp,
        topEnd = if (top && !left) 8.dp else 0.dp,
        bottomStart = if (!top && left) 8.dp else 0.dp,
        bottomEnd = if (!top && !left) 8.dp else 0.dp
    )
)

// ======================================================================
// 2. STATE M4: LOADING PAIRING OMA (03. Initial Phase oma.png frame 3)
// ======================================================================

@Composable
fun LoadingPairingOmaScreen(
    onPairingComplete: () -> Unit
) {
    LoadingPairingOmaContent(onPairingComplete = onPairingComplete)
}

@Composable
fun LoadingPairingOmaContent(
    onPairingComplete: () -> Unit
) {
    val colors = KetukTheme.colors
    // Animasi rotasi spinner terus menerus
    val infiniteTransition = rememberInfiniteTransition(label = "spinner_rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    // Delay otomatis 2 detik sebelum berpindah ke Persetujuan & Izin
    LaunchedEffect(Unit) {
        delay(2000L)
        onPairingComplete()
    }

    // Abaikan back sistem selama loading pairing
    BackHandler(enabled = true) {
        // no-op: layar loading pairing tidak boleh dikembalikan
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.scannerViewport),
        contentAlignment = Alignment.Center
    ) {
        // Latar Blur Asli dari Figma (aman tanpa API level 31+)
        Image(
            painter = painterResource(R.drawable.qr_pairing_blur),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Lapisan gelap agar spinner & teks terbaca sangat jelas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.overlayScrim)
        )

        // Konten Tengah: Spinner & Teks "Loading Pairing..."
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_spinner),
                contentDescription = "Loading Pairing",
                modifier = Modifier
                    .size(54.dp)
                    .rotate(angle)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Loading Pairing...",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onAccent
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Menghubungkan ke HP Wali Nanda...",
                fontSize = 13.sp,
                color = colors.onAccent.copy(alpha = 0.85f)
            )
        }
    }
}

// ======================================================================
// 3. SCREEN M4: PERSETUJUAN & IZIN (03. Initial Phase oma.png frame 4)
// ======================================================================

@Composable
fun OmaPermissionsScreen(
    onNavigateBack: () -> Unit,
    onAgree: () -> Unit
) {
    val colors = KetukTheme.colors
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isPrivacyChecked by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colors.screenBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Judul & Subjudul
            FadeSlideIn(delayMillis = 0) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Persetujuan & Izin",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Dengan menyetujui, anda mengizinkan Ketuk memantau aktivitas keamanan anda",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // KARTU 1: "Yang dipantau"
            FadeSlideIn(delayMillis = 80) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                    border = BorderStroke(1.dp, colors.cardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Yang dipantau",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // 1. Gerakan & Aktivitas HP
                        MonitoringItemRow(
                            icon = Icons.Rounded.Sensors,
                            title = "Gerakan & Aktivitas HP",
                            subtitle = "Mendeteksi aktivitas tidak biasa",
                            colors = colors
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = colors.divider
                        )

                        // 2. Status HP
                        MonitoringItemRow(
                            icon = Icons.Rounded.Smartphone,
                            title = "Status HP",
                            subtitle = "Baterai, layar, dan koneksi",
                            colors = colors
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = colors.divider
                        )

                        // 3. Lokasi (saat darurat)
                        MonitoringItemRow(
                            icon = Icons.Rounded.LocationOn,
                            title = "Lokasi (saat darurat)",
                            subtitle = "Hanya saat membutuhkan bantuan",
                            colors = colors
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KARTU 2: "Izin yang diperlukan"
            FadeSlideIn(delayMillis = 160) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                    border = BorderStroke(1.dp, colors.cardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Izin yang diperlukan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        PermissionBulletItem("1. Kamera (untuk scan QR)", colors)
                        Spacer(modifier = Modifier.height(6.dp))
                        PermissionBulletItem("2. Notifikasi (untuk peringatan)", colors)
                        Spacer(modifier = Modifier.height(6.dp))
                        PermissionBulletItem("3. Sensor Gerak (untuk deteksi aktivitas)", colors)
                        Spacer(modifier = Modifier.height(6.dp))
                        PermissionBulletItem("4. Lokasi (saat darurat)", colors)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Catatan Komitmen Privasi dengan indikator centang/checkbox
            FadeSlideIn(delayMillis = 240) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isPrivacyChecked = !isPrivacyChecked }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    RadioButton(
                        selected = isPrivacyChecked,
                        onClick = { isPrivacyChecked = !isPrivacyChecked },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Terakota,
                            unselectedColor = colors.textSecondary
                        ),
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Kami berkomitmen untuk melindungi privasi Anda. Semua data yang diakses melalui izin ini hanya akan digunakan untuk mendukung fungsi aplikasi dan tidak akan dibagikan kepada pihak ketiga tanpa persetujuan Anda.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol "Saya Setuju"
            FadeSlideIn(delayMillis = 320) {
                KetukButton(
                    text = "Saya Setuju",
                    enabled = isPrivacyChecked,
                    onClick = {
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("Simulasi Mockup: 4 izin sistem disetujui")
                        }
                        onAgree()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MonitoringItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    colors: KetukExtendedColors
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = Terakota.copy(alpha = 0.14f),
            modifier = Modifier.size(38.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Terakota,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary
            )
        }
    }
}

@Composable
private fun PermissionBulletItem(text: String, colors: KetukExtendedColors) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = colors.textPrimary.copy(alpha = 0.90f)
    )
}

// ======================================================================
// 4. SCREEN M5: MODE PASIF OMA/OPA (flowchart.png M5 & UTS Rubrik)
// ======================================================================

@Composable
fun OmaPassiveScreen(
    onNavigateBackToRole: () -> Unit = {},
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val colors = KetukTheme.colors
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val isLoggingOut by profileViewModel.isLoggingOut.collectAsState()

    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }

    // Kata pertama nickname ("Oma"/"Opa") dipakai untuk judul sapaan
    val elder = DummyData.defaultElder
    val sapaanOma = elder.nickname.substringBefore(' ')

    // Helper: tampilkan snackbar simulasi
    fun showMock(message: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colors.screenBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // 1. Baris header: ikon rumah + "Halo, <nama>" + chip "KETUK Aktif"
            FadeSlideIn(delayMillis = 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Terakota.copy(alpha = 0.12f),
                        modifier = Modifier.size(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Home,
                                contentDescription = null,
                                tint = Terakota,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Halo,",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textSecondary
                        )
                        Text(
                            text = elder.nickname,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Chip status perlindungan aktif
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = colors.accentGreen.copy(alpha = 0.16f),
                        border = BorderStroke(1.dp, colors.accentGreen.copy(alpha = 0.35f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(colors.accentGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "KETUK Aktif",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.accentGreen
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Judul besar di tengah + subjudul
            FadeSlideIn(delayMillis = 80) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$sapaanOma, sehat hari ini?",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ketuk salah satu tombol di bawah ya.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Ilustrasi Oma (oma_ungu) dengan latar lingkaran lembut
            FadeSlideIn(delayMillis = 160) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(230.dp)
                            .clip(CircleShape)
                            .background(Terakota.copy(alpha = 0.06f))
                    )
                    Image(
                        painter = painterResource(R.drawable.oma_ungu),
                        contentDescription = "Ilustrasi $sapaanOma",
                        modifier = Modifier.size(210.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Dua tombol besar: "Saya baik-baik saja" & "Saya butuh bantuan"
            FadeSlideIn(delayMillis = 240) {
                OmaBigActionButton(
                    icon = Icons.Rounded.Check,
                    title = "Saya baik-baik saja",
                    subtitle = "Terima kasih, semua aman",
                    containerColor = colors.accentGreen,
                    iconTint = colors.accentGreen,
                    onClick = {
                        showMock("Simulasi Mockup: Kabar \"Saya baik-baik saja\" dikirim ke ${DummyData.defaultWaliName} (Wali)")
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            FadeSlideIn(delayMillis = 320) {
                OmaBigActionButton(
                    icon = Icons.Rounded.VolunteerActivism,
                    title = "Saya butuh bantuan",
                    subtitle = "Tolong segera hubungi wali / penjaga",
                    containerColor = Terakota,
                    iconTint = Terakota,
                    onClick = {
                        showMock("Simulasi Mockup: Permintaan bantuan dikirim ke Wali & Penjaga!")
                    }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 5. Kartu "Kirim Pesan Cepat" dengan 3 pesan siap kirim
            FadeSlideIn(delayMillis = 400) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                    border = BorderStroke(1.dp, colors.cardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Terakota.copy(alpha = 0.14f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Rounded.ChatBubbleOutline,
                                        contentDescription = null,
                                        tint = Terakota,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Kirim Pesan Cepat",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                                Text(
                                    text = "Pilih pesan yang ingin kamu kirim ke wali.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.textSecondary
                                )
                            }

                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = null,
                                tint = colors.textSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OmaQuickMessageChip(
                                icon = Icons.Rounded.EmojiEmotions,
                                text = "Saya baik-baik saja",
                                tint = colors.accentGreen,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                onClick = { showMock("Simulasi Mockup: Pesan \"Saya baik-baik saja\" dikirim ke Wali") }
                            )
                            OmaQuickMessageChip(
                                icon = Icons.Rounded.Favorite,
                                text = "Terima kasih selalu ada",
                                tint = Terakota,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                onClick = { showMock("Simulasi Mockup: Pesan \"Terima kasih selalu ada\" dikirim ke Wali") }
                            )
                            OmaQuickMessageChip(
                                icon = Icons.Rounded.Home,
                                text = "Saya di rumah ya",
                                tint = colors.accentGreen,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                onClick = { showMock("Simulasi Mockup: Pesan \"Saya di rumah ya\" dikirim ke Wali") }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Teks bawah: HP terpantau otomatis
            FadeSlideIn(delayMillis = 480) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.VerifiedUser,
                        contentDescription = null,
                        tint = colors.accentGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "HP $sapaanOma terpantau otomatis",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.accentGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // PENGATURAN MODE GELAP (// TODO(tim): tidak ada di Figma)
            FadeSlideIn(delayMillis = 560) {
                KetukDarkModeCard()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TOMBOL KELUAR (LOGOUT)
            FadeSlideIn(delayMillis = 640) {
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

            Spacer(modifier = Modifier.height(24.dp))
        }

        // DIALOG KONFIRMASI KELUAR
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
                        text = "Keluar dari Akun Oma?",
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                },
                text = {
                    Text(
                        text = "Anda akan kembali ke layar masuk dan pemantauan aktif di HP ini akan dihentikan sementara.",
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
}

/** Tombol aksi besar berbentuk pil: lingkaran ikon putih, judul, subjudul, dan panah. */
@Composable
private fun OmaBigActionButton(
    icon: ImageVector,
    title: String,
    subtitle: String,
    containerColor: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = KetukTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp)
            .clip(RoundedCornerShape(50))
            .background(containerColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(colors.onAccent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onAccent
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = colors.onAccent.copy(alpha = 0.92f)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = colors.onAccent,
            modifier = Modifier.size(28.dp)
        )
    }
}

/** Satu pesan cepat: ikon di atas, teks di bawah (muat di layar sempit). */
@Composable
private fun OmaQuickMessageChip(
    icon: ImageVector,
    text: String,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = KetukTheme.colors
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(colors.screenBackground)
            .border(1.dp, colors.cardBorder, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            lineHeight = 15.sp,
            color = colors.textPrimary,
            textAlign = TextAlign.Center
        )
    }
}

// ======================================================================
// PREVIEWS
// ======================================================================

@Preview(name = "Scan QR - Light", showBackground = true)
@Composable
private fun PreviewScanQrLight() {
    KetukTheme(darkTheme = false) {
        ScanQrScreen(onNavigateBack = {}, onScanSuccess = {})
    }
}

@Preview(name = "Scan QR - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewScanQrDark() {
    KetukTheme(darkTheme = true) {
        ScanQrScreen(onNavigateBack = {}, onScanSuccess = {})
    }
}

@Preview(name = "Permissions - Light", showBackground = true)
@Composable
private fun PreviewOmaPermissionsLight() {
    KetukTheme(darkTheme = false) {
        OmaPermissionsScreen(onNavigateBack = {}, onAgree = {})
    }
}

@Preview(name = "Permissions - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewOmaPermissionsDark() {
    KetukTheme(darkTheme = true) {
        OmaPermissionsScreen(onNavigateBack = {}, onAgree = {})
    }
}

@Preview(name = "Oma Passive - Light", showBackground = true)
@Composable
private fun PreviewOmaPassiveLight() {
    KetukTheme(darkTheme = false) {
        OmaPassiveScreen(onLogout = {})
    }
}

@Preview(name = "Oma Passive - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewOmaPassiveDark() {
    KetukTheme(darkTheme = true) {
        OmaPassiveScreen(onLogout = {})
    }
}

@Preview(name = "Oma Passive - 360dp FontScale 1.3", widthDp = 360, fontScale = 1.3f, showBackground = true)
@Composable
private fun PreviewOmaPassiveAccessibility() {
    KetukTheme(darkTheme = false) {
        OmaPassiveScreen(onLogout = {})
    }
}

@Preview(name = "Oma Passive - 320dp", widthDp = 320, heightDp = 640, showBackground = true)
@Composable
private fun PreviewOmaPassive320() {
    KetukTheme(darkTheme = false) {
        OmaPassiveScreen(onLogout = {})
    }
}

@Preview(name = "Oma Passive - 360dp", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun PreviewOmaPassive360() {
    KetukTheme(darkTheme = false) {
        OmaPassiveScreen(onLogout = {})
    }
}