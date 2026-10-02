package com.chat.ketukprojectmap.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.ui.theme.KetukTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "alpha"
    )
    
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000)
        onNavigateToOnboarding()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        // Latar krem (sudah diatur di theme)
        
        // Rumah besar di pojok kiri bawah
        Image(
            painter = painterResource(R.drawable.rumah_besar),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(120.dp),
            contentScale = ContentScale.Fit
        )
        
        // Pagar bunga di pojok kanan bawah
        Image(
            painter = painterResource(R.drawable.pagar_bunga),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(100.dp),
            contentScale = ContentScale.Fit
        )
        
        // Konten utama (logo + teks) di tengah
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value)
        ) {
            // Logo Ketuk
            Image(
                painter = painterResource(R.drawable.logo_ketuk),
                contentDescription = "Logo Ketuk",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Teks "Ketuk"
            Text(
                text = "Ketuk",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 48.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Tagline
            Text(
                text = "Kalau HP orang tersayang hening, ada yang mengetuk pintu.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
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
