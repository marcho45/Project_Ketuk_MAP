package com.chat.ketukprojectmap.ui.screens.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: OnboardingViewModel = viewModel()
) {
    val pagerState = rememberPagerState(pageCount = { OnboardingViewModel.TOTAL_PAGES })
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(pagerState.currentPage) {
        viewModel.updatePage(pagerState.currentPage)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            OnboardingPage(
                page = page,
                onSkip = onNavigateToLogin,
                onNext = {
                    scope.launch {
                        if (page < OnboardingViewModel.TOTAL_PAGES - 1) {
                            pagerState.animateScrollToPage(page + 1)
                        }
                    }
                },
                onStart = onNavigateToLogin,
                isLastPage = page == OnboardingViewModel.TOTAL_PAGES - 1
            )
        }
    }
}

@Composable
fun OnboardingPage(
    page: Int,
    onSkip: () -> Unit,
    onNext: () -> Unit,
    onStart: () -> Unit,
    isLastPage: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KremBackground)
    ) {
        // Bagian atas: gambar bg_desa dengan tok_melambai
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.45f),
            contentAlignment = Alignment.Center
        ) {
            // Background desa
            Image(
                painter = painterResource(R.drawable.bg_desa),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Tok melambai di tengah
            Image(
                painter = painterResource(R.drawable.tok_melambai),
                contentDescription = "Maskot Tok",
                modifier = Modifier
                    .size(180.dp)
                    .offset(y = 40.dp)
            )
        }

        // Panel bawah dengan sudut atas membulat
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.55f)
                .offset(y = (-32).dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(KremBackground)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Konten sesuai halaman
            when (page) {
                0 -> OnboardingContent1()
                1 -> OnboardingContent2()
                2 -> OnboardingContent3()
                3 -> OnboardingContent4()
            }

            Spacer(modifier = Modifier.weight(1f))

            // Disclaimer kecil (hanya di halaman 4)
            if (page == 3) {
                Text(
                    text = "Ketuk bukan alat medis dan belum mendeteksi jatuh. Ketuk hanya tahu kalau aktivitas berhenti. Tetap hubungi 112/119 untuk keadaan gawat.",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = Arang.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Indikator pager
            PageIndicator(
                currentPage = page,
                totalPages = OnboardingViewModel.TOTAL_PAGES,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            // Tombol navigasi
            if (isLastPage) {
                // Tombol "Mulai" di tengah
                Button(
                    onClick = onStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Terakota
                    )
                ) {
                    Text(
                        text = "Mulai",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            } else {
                // Baris Skip dan tombol panah
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onSkip) {
                        Text(
                            text = "Skip",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Terakota,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Tombol panah pill
                    IconButton(
                        onClick = onNext,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Terakota)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = "Selanjutnya",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PageIndicator(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalPages) { index ->
            val isActive = index == currentPage
            val width by animateDpAsState(
                targetValue = if (isActive) 32.dp else 8.dp,
                animationSpec = tween(300), label = "width"
            )
            val color by animateColorAsState(
                targetValue = if (isActive) Terakota else Terakota.copy(alpha = 0.3f),
                animationSpec = tween(300), label = "color"
            )

            Box(
                modifier = Modifier
                    .width(width)
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
        }
    }
}

@Composable
fun OnboardingContent1() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Ada yang mengetuk",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Arang,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ketuk menjaga dari jauh, tanpa Anda harus menelepon setiap jam.",
            style = MaterialTheme.typography.bodyLarge,
            color = Arang.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

@Composable
fun OnboardingContent2() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "HP yang memberi kabar",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Arang,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ketuk membaca langkah, gerak, dan buka layar di HP secara diam-diam. Tidak ada yang perlu dipencet.",
            style = MaterialTheme.typography.bodyLarge,
            color = Arang.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

@Composable
fun OnboardingContent3() {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Sapa dulu, baru ketuk",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Arang,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Daftar bernomor dengan garis putus-putus
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StepItem(
                number = 1,
                text = "Hening: HP tidak menunjukkan aktivitas lebih lama dari biasanya.",
                showLine = true
            )
            StepItem(
                number = 2,
                text = "Sapa: Layar menyala: \"Oma, sehat?\" Cukup tekan hijau.",
                showLine = true
            )
            StepItem(
                number = 3,
                text = "Wali: Kalau tidak dijawab, Anda mendapat kabar (lewat notifikasi atau SMS).",
                showLine = true
            )
            StepItem(
                number = 4,
                text = "Ketuk: Kalau Anda minta atau tidak sempat merespons, tetangga terdekat yang Anda pilih datang mengecek.",
                showLine = false
            )
        }
    }
}

@Composable
fun OnboardingContent4() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Tanpa kuota, tetap jalan",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Arang,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tanpa internet, Ketuk mengirim SMS dari HP. Pastikan ada pulsa SMS.",
            style = MaterialTheme.typography.bodyLarge,
            color = Arang.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}



@Composable
fun StepItem(
    number: Int,
    text: String,
    showLine: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Lingkaran angka
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(KremGelap),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Arang
                )
            }

            // Garis putus-putus penghubung
            if (showLine) {
                Spacer(modifier = Modifier.height(4.dp))
                Column(
                    modifier = Modifier.width(2.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(8.dp)
                                .background(KremGelap)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Parse teks untuk menebalkan kata pertama (sebelum titik dua)
        val parts = text.split(":", limit = 2)
        if (parts.size == 2) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Arang.copy(alpha = 0.7f))) {
                        append(parts[0] + ":")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Arang.copy(alpha = 0.7f))) {
                        append(parts[1])
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 6.dp)
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Arang.copy(alpha = 0.7f),
                lineHeight = 20.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 6.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    KetukTheme {
        OnboardingScreen(onNavigateToLogin = {})
    }
}
