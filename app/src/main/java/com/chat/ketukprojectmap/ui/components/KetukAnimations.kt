package com.chat.ketukprojectmap.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Arah gerak translasi animasi FadeSlideIn.
 */
enum class SlideDirection {
    /** Muncul dari bawah ke atas (+16dp -> 0dp) - Default Ketuk */
    UP,
    /** Muncul dari atas ke bawah (-16dp -> 0dp) */
    DOWN
}

/**
 * Helper untuk menghitung delay bertingkat (stagger) berdasarkan indeks elemen.
 * Delay = index * stepMillis, dibatasi hingga maxDelayMillis (default 600ms).
 */
fun staggerDelay(
    index: Int,
    baseDelayMillis: Int = 0,
    stepMillis: Int = 80,
    maxDelayMillis: Int = 600
): Int {
    val total = baseDelayMillis + (index * stepMillis)
    return total.coerceAtMost(maxDelayMillis)
}

/**
 * Komponen animasi masuk bersama (fade + slide translasi Y 16dp) dengan dukungan:
 * - Kontrol visibilitas ([visible])
 * - Delay bertingkat ([delayMillis])
 * - Pilihan arah gerak ([direction])
 * - Ketahanan rotasi & navigasi via [rememberSaveable] agar tidak stuck transparan.
 */
@Composable
fun FadeSlideIn(
    visible: Boolean = true,
    delayMillis: Int = 0,
    direction: SlideDirection = SlideDirection.UP,
    slideDistance: Dp = 16.dp,
    durationMillis: Int = 400,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var localVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) {
            if (delayMillis > 0) {
                delay(delayMillis.toLong())
            }
            localVisible = true
        } else {
            localVisible = false
        }
    }

    val alpha by animateFloatAsState(
        targetValue = if (localVisible) 1f else 0f,
        animationSpec = tween(durationMillis = durationMillis),
        label = "ketukFadeAlpha"
    )

    val initialOffsetY = when (direction) {
        SlideDirection.UP -> slideDistance
        SlideDirection.DOWN -> -slideDistance
    }

    val offsetY by animateDpAsState(
        targetValue = if (localVisible) 0.dp else initialOffsetY,
        animationSpec = tween(durationMillis = durationMillis),
        label = "ketukSlideOffsetY"
    )

    Box(
        modifier = modifier.graphicsLayer {
            this.alpha = alpha
            translationY = offsetY.toPx()
        }
    ) {
        content()
    }
}
