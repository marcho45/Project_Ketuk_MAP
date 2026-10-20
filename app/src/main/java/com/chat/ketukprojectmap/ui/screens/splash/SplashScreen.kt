package com.chat.ketukprojectmap.ui.screens.splash

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.data.repository.AuthRepository
import com.chat.ketukprojectmap.ui.theme.KetukTheme
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

private enum class SplashTarget { ONBOARDING, ROLE, HOME }

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToRole: () -> Unit = onNavigateToOnboarding,
    onNavigateToHome: () -> Unit = onNavigateToOnboarding
) {
    var startAnimation by remember { mutableStateOf(false) }
    val repository = remember { AuthRepository() }
    val inPreview = LocalInspectionMode.current

    // Satu progres untuk semua elemen, supaya mulai dan selesai bersamaan
    val progress = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "progress"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        if (inPreview) return@LaunchedEffect

        // Animasi minimal 2 detik, pengecekan sesi jalan bersamaan
        val target = coroutineScope {
            val minimumDelay = async { delay(2000) }
            val check = async {
                try {
                    when {
                        !repository.hasSession() -> SplashTarget.ONBOARDING
                        repository.getRole() != null -> SplashTarget.HOME
                        else -> SplashTarget.ROLE
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    // Gagal cek (misalnya tidak ada internet): aman kembali ke awal
                    SplashTarget.ONBOARDING
                }
            }
            minimumDelay.await()
            check.await()
        }

        when (target) {
            SplashTarget.ONBOARDING -> onNavigateToOnboarding()
            SplashTarget.ROLE -> onNavigateToRole()
            SplashTarget.HOME -> onNavigateToHome()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(KetukTheme.colors.screenBackground)
    ) {

        // ===== Semua ukuran dihitung dari ukuran layar =====
        val rumahWidth = maxWidth * 0.65f
        val pagarWidth = maxWidth * 0.35f         // lebar tiap pagar
        val pagarOverlap = pagarWidth * 0.3f      // tumpang tindih antar pagar
        val pagarCount = 3                        // jumlah pagar, tambah kalau masih ada celah
        val logoSize = maxWidth * 0.18f
        val kontenOffsetY = -(maxHeight * 0.06f)

        // ===== ILUSTRASI BAWAH =====
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .graphicsLayer {
                    val p = progress.value
                    alpha = p
                    translationY = (1f - p) * 24.dp.toPx()   // naik sedikit dari bawah
                }
        ) {
            // Pagar: index 0 menempel di tepi kanan, sisanya bergeser ke kiri.
            // Digambar dari kiri ke kanan (downTo) supaya yang kanan berada di atas.
            for (i in (pagarCount - 1) downTo 0) {
                Image(
                    painter = painterResource(R.drawable.pagar_bunga),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .width(pagarWidth)
                        .offset(x = -((pagarWidth - pagarOverlap) * i)),
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.BottomEnd
                )
            }

            // Rumah + semak (digambar belakangan = di atas pagar)
            Image(
                painter = painterResource(R.drawable.rumah_besar),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .width(rumahWidth),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.BottomStart
            )
        }

        // ===== KONTEN UTAMA (logo + teks) =====
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = kontenOffsetY)
                .padding(horizontal = 40.dp)
                .graphicsLayer {
                    val p = progress.value
                    alpha = p
                    val s = 0.8f + 0.2f * p
                    scaleX = s
                    scaleY = s
                }
        ) {
            Image(
                painter = painterResource(R.drawable.logo_ketuk),
                contentDescription = "Logo Ketuk",
                modifier = Modifier.size(logoSize),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ketuk",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 40.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Kalau HP orang tersayang hening, ada yang mengetuk pintu.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    KetukTheme {
        SplashScreen(onNavigateToOnboarding = {})
    }
}

@androidx.compose.ui.tooling.preview.Preview(
    name = "Mode Gelap",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SplashScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        SplashScreen(onNavigateToOnboarding = {})
    }
}