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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
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
import com.chat.ketukprojectmap.BuildConfig
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.data.dummy.DummyData
import com.chat.ketukprojectmap.ui.components.KetukButton
import com.chat.ketukprojectmap.ui.components.KetukDarkModeCard
import com.chat.ketukprojectmap.ui.components.KetukLogoutButton
import com.chat.ketukprojectmap.ui.components.KetukSecondaryButton
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
                // Baris Atas: Tombol Kembali
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Kembali",
                            tint = colors.textPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Judul & Subjudul
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

                Spacer(modifier = Modifier.height(20.dp))

                // Viewport Mockup Kamera Statis
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

                Spacer(modifier = Modifier.height(20.dp))

                // Kartu Petunjuk Kamera (Ikon Bohlam + Teks)
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

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Bantuan Simulasi Scan
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
            // Baris Atas: Tombol Kembali
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Kembali",
                    tint = colors.textPrimary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Judul & Subjudul
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

            Spacer(modifier = Modifier.height(20.dp))

            // KARTU 1: "Yang dipantau"
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

            Spacer(modifier = Modifier.height(16.dp))

            // KARTU 2: "Izin yang diperlukan"
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

            Spacer(modifier = Modifier.height(16.dp))

            // Catatan Komitmen Privasi dengan indikator centang/checkbox
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

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol "Saya Setuju"
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

    var showSapaDialog by rememberSaveable { mutableStateOf(false) }
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    var sapaStatusText by rememberSaveable { mutableStateOf("Status normal: Aktivitas HP terdeteksi") }

    // TODO(tim): tombol panah kembali khusus DEMO_MODE, tidak ada di Figma
    if (BuildConfig.DEMO_MODE) {
        BackHandler {
            onNavigateBackToRole()
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
            // Header: Tombol Panah Kembali Demo + Judul
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO(tim): tombol khusus demo, tidak ada di Figma
                if (BuildConfig.DEMO_MODE) {
                    IconButton(
                        onClick = onNavigateBackToRole,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Kembali ke Pilih Peran",
                            tint = Terakota
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ketuk Aktif",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Aplikasi berjalan tenang menjaga Oma",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary
                    )
                }

                // Badge Status Perlindungan Aktif
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
                            text = "Aman",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.accentGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // KARTU HERO LANSIA (Informasi Oma Diyas)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                border = BorderStroke(1.dp, colors.cardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.oma_avatar),
                        contentDescription = "Avatar Oma Diyas",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = DummyData.defaultElder.fullName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${DummyData.defaultElder.nickname} • ${DummyData.defaultElder.age} Tahun • ${DummyData.defaultElder.area}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(color = colors.divider)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Detail Status Sensor HP Lansia
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OmaSensorStat(
                            icon = Icons.AutoMirrored.Rounded.DirectionsWalk,
                            label = "Langkah",
                            value = "1.420",
                            colors = colors
                        )
                        OmaSensorStat(
                            icon = Icons.Rounded.BatteryChargingFull,
                            label = "Baterai",
                            value = "78%",
                            colors = colors
                        )
                        OmaSensorStat(
                            icon = Icons.Rounded.CheckCircle,
                            label = "Status",
                            value = "Sehat",
                            colors = colors
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // KARTU SIMULASI SAPAAN L1 ("Oma baik-baik saja?") Sesuai flowchart.png
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardHighlight),
                border = BorderStroke(1.dp, Terakota.copy(alpha = 0.35f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Terakota.copy(alpha = 0.16f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.ChatBubbleOutline,
                                    contentDescription = null,
                                    tint = Terakota,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Simulasi Sapaan L1",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Simulasi kondisi HP hening: jika HP hening lebih dari 4 jam, sistem memunculkan dialog sapa untuk mengonfirmasi keadaan Oma.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tombol untuk memicu Dialog Sapa L1
                    KetukButton(
                        text = "Munculkan Sapaan",
                        onClick = { showSapaDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = sapaStatusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.accentGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // KARTU AKSI CEPAT LANSIA
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                border = BorderStroke(1.dp, colors.cardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Kontak Keluarga & Pendamping",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tombol Hubungi Wali
                    OmaActionRow(
                        icon = Icons.Rounded.Phone,
                        title = "Telepon Wali: ${DummyData.defaultWaliName}",
                        subtitle = "+62 812 3456 7890",
                        colors = colors,
                        onClick = {
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("Simulasi Mockup: Memanggil Nanda Valeri (+62 812 3456 7890)...")
                            }
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = colors.divider)

                    // Tombol Cek Sensor Gerak
                    OmaActionRow(
                        icon = Icons.Rounded.Sensors,
                        title = "Status Sensor Gerak",
                        subtitle = "Accelerometer & Step Counter Normal",
                        colors = colors,
                        onClick = {
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("Simulasi Mockup: Sensor gerak Oma bekerja aktif normal")
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // PENGATURAN MODE GELAP (// TODO(tim): tidak ada di Figma)
            KetukDarkModeCard()

            Spacer(modifier = Modifier.height(16.dp))

            // TOMBOL KELUAR (LOGOUT)
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

            Spacer(modifier = Modifier.height(24.dp))
        }

        // DIALOG SAPA L1: "Oma baik-baik saja?"
        if (showSapaDialog) {
            AlertDialog(
                onDismissRequest = { showSapaDialog = false },
                containerColor = colors.cardSurface,
                title = {
                    Text(
                        text = "Oma baik-baik saja?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ketuk mendeteksi HP hening beberapa jam. Tekan tombol di bawah untuk mengabari Nanda dan keluarga bahwa Oma aman.",
                            fontSize = 15.sp,
                            color = colors.textSecondary,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                showSapaDialog = false
                                sapaStatusText = "Status: Oma mengonfirmasi 'Saya baik-baik saja'"
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Status Normal: Kabar baik dikirim ke Nanda (Wali)")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.accentGreen),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Saya baik-baik saja",
                                color = colors.onAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                showSapaDialog = false
                                sapaStatusText = "Status: Memanggil bantuan darurat ke Wali & Penjaga"
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Simulasi Mockup: Peringatan L2 dikirim ke Wali & Penjaga!")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Terakota),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Butuh bantuan",
                                color = colors.onAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                },
                confirmButton = {}
            )
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

@Composable
private fun OmaSensorStat(
    icon: ImageVector,
    label: String,
    value: String,
    colors: KetukExtendedColors
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Terakota,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = colors.textSecondary
        )
    }
}

@Composable
private fun OmaActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    colors: KetukExtendedColors,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
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

        Column(modifier = Modifier.weight(1f)) {
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

        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = colors.textSecondary,
            modifier = Modifier.size(20.dp)
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

