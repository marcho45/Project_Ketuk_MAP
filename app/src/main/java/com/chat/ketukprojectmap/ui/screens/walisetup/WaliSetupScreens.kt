package com.chat.ketukprojectmap.ui.screens.walisetup

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.data.dummy.CircleMember
import com.chat.ketukprojectmap.data.dummy.DummyData
import com.chat.ketukprojectmap.data.dummy.ElderGender
import com.chat.ketukprojectmap.ui.components.FadeSlideIn
import com.chat.ketukprojectmap.ui.components.KetukButton
import com.chat.ketukprojectmap.ui.components.staggerDelay
import com.chat.ketukprojectmap.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Setup 1 Wali (`02. Initial Phase Wali.png` - Frame 2):
 * Layar Form "Detail Oma/Opa" dengan validasi wajib dan validasi rentang umur 40–120 tahun.
 */
@Composable
fun DetailOmaOpaScreen(
    onNavigateBack: () -> Unit,
    onContinue: () -> Unit
) {
    val colors = KetukTheme.colors
    var waliName by rememberSaveable { mutableStateOf(DummyData.defaultWaliName) }
    var selectedGenderName by rememberSaveable { mutableStateOf(ElderGender.OMA.name) }
    var elderName by rememberSaveable { mutableStateOf(DummyData.defaultElder.fullName) }
    var elderAgeText by rememberSaveable { mutableStateOf(DummyData.defaultElder.age.toString()) }

    val selectedGender = remember(selectedGenderName) {
        ElderGender.valueOf(selectedGenderName)
    }

    val waliNameError = if (waliName.isBlank()) "Nama wali tidak boleh kosong" else null
    val elderNameError = if (elderName.isBlank()) "Nama ${selectedGender.label} tidak boleh kosong" else null
    val ageInt = elderAgeText.toIntOrNull()
    val elderAgeError = when {
        elderAgeText.isBlank() -> "Umur ${selectedGender.label} wajib diisi"
        ageInt == null -> "Umur hanya boleh berisi angka"
        ageInt < 40 || ageInt > 120 -> "Umur harus berada di rentang 40–120 tahun"
        else -> null
    }

    val isFormValid = waliNameError == null && elderNameError == null && elderAgeError == null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.screenBackground)
            .systemBarsPadding()
            .imePadding()
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            FadeSlideIn(delayMillis = 0) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Detail Oma/Opa",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Isi detailmu sebagai wali dan juga detail Oma/Opa\nagar Ketuk dapat menyapa !",
                        fontSize = 12.sp,
                        color = colors.textPrimary.copy(alpha = 0.8f),
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Field Nama Wali
            FadeSlideIn(delayMillis = 80) {
                SetupLabeledTextField(
                    label = "Masukkan nama anda",
                    value = waliName,
                    onValueChange = { waliName = it.take(60) },
                    keyboardType = KeyboardType.Text,
                    isError = waliNameError != null,
                    supportingText = waliNameError
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Toggle Pill Opa / Oma
            FadeSlideIn(delayMillis = 160) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = colors.cardSurface,
                    border = BorderStroke(1.dp, colors.divider)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ElderGender.entries.forEach { gender ->
                            val isSelected = selectedGender == gender
                            Surface(
                                onClick = { selectedGenderName = gender.name },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp),
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Terakota else Color.Transparent
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = gender.label,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) colors.onAccent else colors.textPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Field Nama Oma/Opa & 4. Field Umur
            FadeSlideIn(delayMillis = 240) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SetupLabeledTextField(
                        label = "Masukkan nama ${selectedGender.label} anda",
                        value = elderName,
                        onValueChange = { elderName = it.take(60) },
                        keyboardType = KeyboardType.Text,
                        isError = elderNameError != null,
                        supportingText = elderNameError
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    SetupLabeledTextField(
                        label = "Masukkan umur ${selectedGender.label} anda",
                        value = elderAgeText,
                        onValueChange = { input ->
                            // Tolak karakter non-angka dan batasi 3 digit
                            val digitsOnly = input.filter { it.isDigit() }.take(3)
                            elderAgeText = digitsOnly
                        },
                        keyboardType = KeyboardType.Number,
                        isError = elderAgeError != null,
                        supportingText = elderAgeError,
                        trailingBadge = {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Terakota
                            ) {
                                Text(
                                    text = "Tahun",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.onAccent,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }

        // TODO(tim): Tombol Lanjutkan ditambahkan agar form dapat disubmit ke tahap Circle Keluarga
        FadeSlideIn(
            delayMillis = 320,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 20.dp)
        ) {
            KetukButton(
                text = "Lanjutkan",
                onClick = onContinue,
                enabled = isFormValid
            )
        }
    }
}

@Composable
private fun SetupLabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    isError: Boolean,
    supportingText: String?,
    trailingBadge: (@Composable () -> Unit)? = null
) {
    val colors = KetukTheme.colors
    val borderColor = if (isError) MaterialTheme.colorScheme.error else colors.divider
    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = colors.cardSurface,
            border = BorderStroke(1.dp, borderColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = colors.textPrimary.copy(alpha = 0.45f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.textPrimary
                        ),
                        cursorBrush = SolidColor(Terakota),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (trailingBadge != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    trailingBadge()
                }
            }
        }
        if (isError && !supportingText.isNullOrBlank()) {
            Text(
                text = supportingText,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

/**
 * Setup 2 Wali (`02. Initial Phase Wali.png` - Frame 3):
 * Layar "Circle Keluarga" dengan daftar anggota, tombol "+ Tambah Anggota", dan kartu "Undang Keluarga Lain".
 */
@Composable
fun CircleKeluargaScreen(
    onNavigateBack: () -> Unit,
    onContinue: () -> Unit
) {
    val colors = KetukTheme.colors
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var hasAddedExtraMember by rememberSaveable { mutableStateOf(false) }
    val members = remember(hasAddedExtraMember) {
        if (hasAddedExtraMember) {
            DummyData.initialCircleMembers + CircleMember(
                id = "circle_4",
                name = "Kakak Raka",
                roleLabel = "Anggota",
                isLeader = false,
                phone = "+62 813 9988 7766"
            )
        } else {
            DummyData.initialCircleMembers
        }
    }

    Scaffold(
        containerColor = colors.screenBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 32.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                FadeSlideIn(delayMillis = 0) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Circle Keluarga",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Kelola daftar keluarga yang bisa melihat\ndan menerima notifikasi",
                            fontSize = 12.sp,
                            color = colors.textPrimary.copy(alpha = 0.8f),
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Kartu 1: Anggota Circle
                FadeSlideIn(delayMillis = 80) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                        border = BorderStroke(1.dp, colors.divider),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Anggota Circle",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                                Text(
                                    text = "${members.size} Anggota",
                                    fontSize = 11.sp,
                                    color = colors.textPrimary.copy(alpha = 0.5f)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            members.forEachIndexed { index, member ->
                                CircleMemberRow(
                                    member = member,
                                    onClick = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Simulasi Mockup: Detail anggota ${member.name}"
                                            )
                                        }
                                    }
                                )
                                if (index < members.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        thickness = 0.8.dp,
                                        color = colors.divider.copy(alpha = 0.6f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Tombol + Tambah Anggota
                            OutlinedButton(
                                onClick = {
                                    if (!hasAddedExtraMember) {
                                        hasAddedExtraMember = true
                                    }
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Simulasi Mockup: Tambah anggota keluarga ke Circle"
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, colors.divider),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = colors.cardSurface,
                                    contentColor = colors.textPrimary
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        modifier = Modifier.size(26.dp),
                                        shape = CircleShape,
                                        color = Terakota
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Rounded.Add,
                                                contentDescription = null,
                                                tint = colors.onAccent,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Tambah Anggota",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = colors.textPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Kartu 2: Undang Keluarga Lain
                FadeSlideIn(delayMillis = 160) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                        border = BorderStroke(1.dp, colors.divider),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Undang Keluarga Lain",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Bagikan QR code atau tautan undangan untuk\nmenambahkan anggota baru.",
                                fontSize = 11.sp,
                                color = colors.textPrimary.copy(alpha = 0.65f),
                                lineHeight = 14.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // QR tetap berlatar terang (qrCardWali) agar dapat dipindai di mode gelap
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(colors.qrCardWali)
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.qr_pairing),
                                    contentDescription = "QR Undangan Circle Keluarga",
                                    modifier = Modifier.size(104.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Tombol Bagikan QR
                            Button(
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Simulasi Mockup: Bagikan QR Circle Keluarga")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = Terakota)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Share,
                                    contentDescription = null,
                                    tint = colors.onAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Bagikan QR",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.onAccent
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Tombol Salin Tautan
                            OutlinedButton(
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Simulasi Mockup: Salin Tautan Undangan Circle")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp),
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, colors.borderWali),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = colors.cardWali.copy(alpha = 0.5f),
                                    contentColor = colors.textPrimary
                                )
                            ) {
                                Text(
                                    text = "Salin Tautan",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.textPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                FadeSlideIn(delayMillis = 240) {
                    KetukButton(
                        text = "Lanjutkan",
                        onClick = onContinue
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun CircleMemberRow(
    member: CircleMember,
    onClick: () -> Unit
) {
    val colors = KetukTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(36.dp),
            shape = CircleShape,
            color = colors.tintWali
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = member.name.first().uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Terakota
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = member.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary
            )
            Text(
                text = member.roleLabel,
                fontSize = 11.sp,
                color = colors.textPrimary.copy(alpha = 0.55f)
            )
        }

        if (member.isLeader) {
            Icon(
                imageVector = Icons.Rounded.WorkspacePremium,
                contentDescription = "Ketua Circle",
                tint = colors.accentAmber,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = colors.textPrimary.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Setup 3 & 3b Wali (`02. Initial Phase Wali.png` - Frame 4 & 5):
 * Layar "QR Wali Oma Diyas" dan simulasi "Loading Pairing..." (2 detik) sebelum lanjut ke Pilih Penjaga.
 */
@Composable
fun QrWaliScreen(
    onNavigateBack: () -> Unit,
    onPairingComplete: () -> Unit
) {
    val colors = KetukTheme.colors
    var isPairing by rememberSaveable { mutableStateOf(false) }

    // Jika sedang Loading Pairing, otomatis lanjut setelah 2 detik
    LaunchedEffect(isPairing) {
        if (isPairing) {
            delay(2000L)
            onPairingComplete()
        }
    }

    if (isPairing) {
        BackHandler(enabled = true) { /* no-op: abaikan back selama loading pairing */ }
        LoadingPairingWaliContent()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.screenBackground)
                .systemBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 32.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                FadeSlideIn(delayMillis = 0) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "QR Wali ${DummyData.defaultElder.nickname}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Arahkan QR ke HP Oma/Opa untuk mengakses",
                            fontSize = 12.sp,
                            color = colors.textPrimary.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // QR tetap berlatar putih (qrSurface) agar dapat dipindai di mode gelap
                FadeSlideIn(delayMillis = 80) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.qrSurface),
                        border = BorderStroke(1.dp, colors.divider),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(28.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.qr_pairing),
                                contentDescription = "QR Pairing Wali Oma Diyas",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
            }

            // TODO(tim): Tombol simulasi pindai ditambahkan agar alur demo bisa lanjut ke Loading Pairing -> Pilih Penjaga
            FadeSlideIn(
                delayMillis = 160,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 24.dp)
            ) {
                KetukButton(
                    text = "Simulasikan Pindai",
                    onClick = { isPairing = true }
                )
            }
        }
    }
}

@Composable
private fun LoadingPairingWaliContent() {
    val colors = KetukTheme.colors
    val infiniteTransition = rememberInfiniteTransition(label = "waliPairingSpinner")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spinnerRotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.screenBackground)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.85f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.qr_pairing_blur),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                Column(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_spinner),
                        contentDescription = "Loading Pairing",
                        modifier = Modifier
                            .size(56.dp)
                            .rotate(rotation)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Loading Pairing...",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Setup 4 Wali (`02. Initial Phase Wali.png` - Frame 6):
 * Layar "Pilih Penjaga Terdekat" dengan search bar reaktif dan daftar kontak Radio Button.
 */
@Composable
fun PilihPenjagaScreen(
    onNavigateBack: () -> Unit,
    onGuardianConfirmed: () -> Unit
) {
    val colors = KetukTheme.colors
    var searchQuery by rememberSaveable { mutableStateOf("") }
    // Default terpilih: Adik Adam ("guard_3") sesuai gambar Figma 02
    var selectedGuardianId by rememberSaveable { mutableStateOf("guard_3") }

    val filteredGuardians = remember(searchQuery) {
        val q = searchQuery.trim().lowercase()
        if (q.isEmpty()) {
            DummyData.candidateGuardians
        } else {
            DummyData.candidateGuardians.filter {
                it.name.lowercase().contains(q) || it.phone.lowercase().contains(q)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.screenBackground)
            .systemBarsPadding()
            .imePadding()
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            FadeSlideIn(delayMillis = 0) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Pilih Penjaga Terdekat",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pastikan penjaga menerima undangan mu",
                        fontSize = 12.sp,
                        color = colors.textPrimary.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Search Bar "Cari nama atau nomor..."
            FadeSlideIn(delayMillis = 80) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    placeholder = {
                        Text(
                            text = "Cari nama atau nomor...",
                            fontSize = 13.sp,
                            color = colors.textPrimary.copy(alpha = 0.45f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Cari",
                            tint = IkonBiru,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    textStyle = TextStyle(fontSize = 13.sp, color = colors.textPrimary),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.cardSurface,
                        unfocusedContainerColor = colors.cardSurface,
                        focusedBorderColor = Terakota,
                        unfocusedBorderColor = colors.divider,
                        cursorColor = Terakota
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Kartu Daftar Kontak Penjaga
            FadeSlideIn(delayMillis = 160) {
                Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
                border = BorderStroke(1.dp, colors.divider),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                if (filteredGuardians.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Kontak tidak ditemukan",
                            fontSize = 13.sp,
                            color = colors.textPrimary.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        filteredGuardians.forEachIndexed { index, guardian ->
                            val isSelected = selectedGuardianId == guardian.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedGuardianId = guardian.id }
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(38.dp),
                                    shape = CircleShape,
                                    color = colors.tintPenjaga
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = guardian.name.first().uppercase(),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Terakota
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = guardian.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colors.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = guardian.phone,
                                        fontSize = 11.sp,
                                        color = colors.textPrimary.copy(alpha = 0.55f)
                                    )
                                }

                                // Indikator Radio sesuai Figma 02
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isSelected) Terakota else colors.divider,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(11.dp)
                                                .clip(CircleShape)
                                                .background(Terakota)
                                        )
                                    }
                                }
                            }

                            if (index < filteredGuardians.lastIndex) {
                                HorizontalDivider(
                                    thickness = 0.8.dp,
                                    color = colors.divider.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }

            Spacer(modifier = Modifier.height(20.dp))

            FadeSlideIn(delayMillis = 240) {
                KetukButton(
                    text = "Pilih sebagai Penjaga",
                    onClick = onGuardianConfirmed,
                    enabled = selectedGuardianId.isNotBlank()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailOmaOpaScreenPreview() {
    KetukTheme {
        DetailOmaOpaScreen(onNavigateBack = {}, onContinue = {})
    }
}

@Preview(name = "Detail Oma/Opa - Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetailOmaOpaScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        DetailOmaOpaScreen(onNavigateBack = {}, onContinue = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun CircleKeluargaScreenPreview() {
    KetukTheme {
        CircleKeluargaScreen(onNavigateBack = {}, onContinue = {})
    }
}

@Preview(name = "Circle Keluarga - Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CircleKeluargaScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        CircleKeluargaScreen(onNavigateBack = {}, onContinue = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun QrWaliScreenPreview() {
    KetukTheme {
        QrWaliScreen(onNavigateBack = {}, onPairingComplete = {})
    }
}

@Preview(name = "QR Wali - Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun QrWaliScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        QrWaliScreen(onNavigateBack = {}, onPairingComplete = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PilihPenjagaScreenPreview() {
    KetukTheme {
        PilihPenjagaScreen(onNavigateBack = {}, onGuardianConfirmed = {})
    }
}

@Preview(name = "Pilih Penjaga - Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PilihPenjagaScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        PilihPenjagaScreen(onNavigateBack = {}, onGuardianConfirmed = {})
    }
}

