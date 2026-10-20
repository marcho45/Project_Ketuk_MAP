package com.chat.ketukprojectmap.ui.screens.onboarding

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.ui.theme.*
import kotlinx.coroutines.launch


@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: OnboardingViewModel = viewModel()
) {
    val pagerState = rememberPagerState(pageCount = { OnboardingViewModel.TOTAL_PAGES })
    val scope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage
    val isLastPage = currentPage == OnboardingViewModel.TOTAL_PAGES - 1
    val colors = KetukTheme.colors

    LaunchedEffect(currentPage) {
        viewModel.updatePage(currentPage)
    }

    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.screenBackground)
    ) {
        // ===== Proporsi dari Figma (atur di sini) =====
        val cornerRadius = 32.dp
        val panelTop = maxHeight * 0.32f                                  // awal panel krem
        val mascotTop = maxHeight * 0.19f                                 // posisi atas maskot
        val mascotSize = minOf(maxWidth * 0.52f, maxHeight * 0.26f)       // ukuran maskot
        val overlap = maxOf(0.dp, mascotTop + mascotSize - panelTop)      // bagian maskot yang masuk panel
        val contentTop = maxOf(overlap + 12.dp, maxHeight * 0.22f)        // jarak judul dari atas panel
        val stepsTop = overlap + 16.dp                                    // halaman 3: langsung di bawah maskot
        val sidePadding = 40.dp

        // 1. Latar desa (mulai di bawah status bar seperti Figma)
        Image(
            painter = painterResource(R.drawable.bg_desa),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = statusBarTop)
                .fillMaxWidth()
                .height(panelTop - statusBarTop + cornerRadius),
            contentScale = ContentScale.Crop
        )

        // 2. Panel bawah (digambar sebelum maskot)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = panelTop)
                .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                .background(colors.screenBackground)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = sidePadding,
                            end = sidePadding,
                            top = if (page == 2) stepsTop else contentTop,
                            bottom = 8.dp
                        )
                ) {
                    when (page) {
                        0 -> OnboardingContent1()
                        1 -> OnboardingContent2()
                        2 -> OnboardingContent3()
                        else -> OnboardingContent4()
                    }
                }
            }

            // Bagian bawah (tetap, di luar area scroll)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = sidePadding)
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLastPage) {
                    Text(
                        text = underlineKetuk(
                            "Ketuk bukan alat medis dan belum mendeteksi jatuh. " +
                                    "Ketuk hanya tahu kalau aktivitas berhenti. " +
                                    "Tetap hubungi 112/119 untuk keadaan gawat."
                        ),
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        color = colors.textPrimary.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                PageIndicator(
                    currentPage = currentPage,
                    totalPages = OnboardingViewModel.TOTAL_PAGES,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLastPage) {
                        // "Mulai": pill kecil di tengah
                        Button(
                            onClick = onNavigateToLogin,
                            modifier = Modifier
                                .width(120.dp)
                                .height(44.dp),
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Terakota)
                        ) {
                            Text(
                                text = "Mulai",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.onAccent
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = onNavigateToLogin) {
                                Text(
                                    text = "Skip",
                                    fontSize = 14.sp,
                                    color = colors.textPrimary.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Normal
                                )
                            }

                            IconButton(
                                onClick = {
                                    scope.launch { pagerState.animateScrollToPage(currentPage + 1) }
                                },
                                modifier = Modifier
                                    .width(64.dp)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(Terakota)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                    contentDescription = "Selanjutnya",
                                    tint = colors.onAccent,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Maskot di paling atas, menimpa lengkungan panel
        Image(
            painter = painterResource(R.drawable.tok_melambai),
            contentDescription = "Maskot Tok",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = mascotTop)
                .size(mascotSize),
            contentScale = ContentScale.Fit
        )
    }
}

/** 4 garis sama panjang, yang aktif lebih pekat (sesuai Figma). */
@Composable
fun PageIndicator(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(totalPages) { index ->
            val color by animateColorAsState(
                targetValue = if (index == currentPage) Terakota else Terakota.copy(alpha = 0.45f),
                animationSpec = tween(300),
                label = "color"
            )
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
        }
    }
}

/** Menggarisbawahi setiap kata "Ketuk" di dalam teks. */
fun underlineKetuk(text: String, brand: String = "Ketuk"): AnnotatedString =
    buildAnnotatedString {
        var start = 0
        while (true) {
            val idx = text.indexOf(brand, start)
            if (idx < 0) {
                append(text.substring(start))
                break
            }
            append(text.substring(start, idx))
            withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) { append(brand) }
            start = idx + brand.length
        }
    }

@Composable
private fun OnboardingTextPage(title: String, body: String) {
    val textPrimary = KetukTheme.colors.textPrimary
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = underlineKetuk(body),
            fontSize = 13.sp,
            lineHeight = 19.sp,
            color = textPrimary.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OnboardingContent1() {
    OnboardingTextPage(
        title = "Ada yang mengetuk",
        body = "Ketuk menjaga dari jauh, tanpa Anda harus menelepon setiap jam."
    )
}

@Composable
fun OnboardingContent2() {
    OnboardingTextPage(
        title = "HP yang memberi kabar",
        body = "Ketuk membaca langkah, gerak, dan buka layar di HP secara diam-diam. Tidak ada yang perlu dipencet."
    )
}

@Composable
fun OnboardingContent3() {
    val textPrimary = KetukTheme.colors.textPrimary
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Sapa dulu, baru ketuk",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        val steps = listOf(
            "Hening: HP tidak menunjukkan aktivitas lebih lama dari biasanya.",
            "Sapa: Layar menyala: \"Oma, sehat?\" Cukup tekan hijau.",
            "Wali: Kalau tidak dijawab, Anda mendapat kabar (lewat notifikasi atau SMS).",
            "Ketuk: Kalau Anda minta atau tidak sempat merespons, tetangga terdekat yang Anda pilih datang mengecek."
        )
        steps.forEachIndexed { index, text ->
            StepItem(
                number = index + 1,
                text = text,
                isLast = index == steps.lastIndex
            )
        }
    }
}

@Composable
fun OnboardingContent4() {
    OnboardingTextPage(
        title = "Tanpa kuota, tetap jalan",
        body = "Tanpa internet, Ketuk mengirim SMS dari HP. Pastikan ada pulsa SMS."
    )
}

@Composable
fun StepItem(
    number: Int,
    text: String,
    isLast: Boolean = false
) {
    val colors = KetukTheme.colors
    val bodyColor = colors.textPrimary.copy(alpha = 0.7f)
    val lineColor = Terakota.copy(alpha = 0.55f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),   // tinggi baris = tinggi teks, biar panah ikut memanjang
        verticalAlignment = Alignment.Top
    ) {
        // Kolom kiri: lingkaran nomor + panah putus-putus ke nomor berikutnya
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(colors.divider)
                    .border(1.dp, Terakota.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
            }

            if (!isLast) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val cx = size.width / 2
                    val top = 4.dp.toPx()
                    val bottom = size.height - 4.dp.toPx()

                    if (bottom > top) {
                        // garis titik-titik
                        drawLine(
                            color = lineColor,
                            start = Offset(cx, top),
                            end = Offset(cx, bottom),
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(0.1f, 5.dp.toPx())
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Kata sebelum titik dua ditebalkan
        val parts = text.split(":", limit = 2)
        Text(
            text = if (parts.size == 2) {
                buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = bodyColor)) {
                        append(parts[0] + ":")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = bodyColor)) {
                        append(parts[1])
                    }
                }
            } else {
                AnnotatedString(text)
            },
            fontSize = 12.sp,
            lineHeight = 17.sp,
            color = bodyColor,
            modifier = Modifier
                .weight(1f)
                .padding(top = 3.dp, bottom = if (isLast) 0.dp else 16.dp)
        )
    }
}

@Preview(name = "Normal", showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    KetukTheme {
        OnboardingScreen(onNavigateToLogin = {})
    }
}

@Preview(name = "Kecil", widthDp = 360, heightDp = 640, fontScale = 1.3f, showBackground = true)
@Composable
fun OnboardingScreenSmallPreview() {
    KetukTheme {
        OnboardingScreen(onNavigateToLogin = {})
    }
}

@Preview(name = "Mode Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OnboardingScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        OnboardingScreen(onNavigateToLogin = {})
    }
}