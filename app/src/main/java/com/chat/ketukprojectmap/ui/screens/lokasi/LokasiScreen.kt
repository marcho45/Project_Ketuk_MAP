package com.chat.ketukprojectmap.ui.screens.lokasi

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.data.dummy.DummyData
import com.chat.ketukprojectmap.data.model.LokasiData
import com.chat.ketukprojectmap.ui.components.FadeSlideIn
import com.chat.ketukprojectmap.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LokasiScreen(
    viewModel: LokasiViewModel = viewModel()
) {
    val lokasiData by viewModel.lokasiData.collectAsState()
    var contentVisible by remember { mutableStateOf(false) }
    // Posisi panel bawah: true = terbuka, false = tertutup (peta lebih luas)
    var sheetExpanded by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val colors = KetukTheme.colors

    val showMockupMessage: (String) -> Unit = { msg ->
        scope.launch {
            snackbarHostState.showSnackbar(msg)
        }
    }

    LaunchedEffect(Unit) {
        delay(100.milliseconds)
        contentVisible = true
    }

    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.screenBackground)
            .statusBarsPadding()
    ) {
        // Tinggi maksimum panel: sisakan ruang untuk judul dan bottom bar.
        // Di layar kecil / font besar, isi panel yang terbuka bisa di-scroll.
        val sheetMaxHeight = maxOf(maxHeight - 64.dp - navBarPadding - 140.dp, 200.dp)

        // Lapisan bawah: judul + peta (peta mengisi sisa ruang, panel menimpa di atasnya)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .widthIn(max = 600.dp)
                .fillMaxSize()
                .padding(bottom = 64.dp + navBarPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            FadeSlideIn(visible = contentVisible, delayMillis = 0) {
                Text(
                    text = "Lokasi ${lokasiData.elderName}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FadeSlideIn(
                visible = contentVisible,
                delayMillis = 80,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.peta_lokasi),
                    contentDescription = "Peta lokasi ${lokasiData.elderName}",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Lapisan atas: panel bawah yang bisa ditarik / dipencet
        FadeSlideIn(
            visible = contentVisible,
            delayMillis = 160,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .padding(bottom = 64.dp + navBarPadding)
        ) {
            LokasiBottomSheet(
                lokasiData = lokasiData,
                expanded = sheetExpanded,
                onExpandedChange = { sheetExpanded = it },
                maxHeight = sheetMaxHeight,
                onShowRoute = {
                    showMockupMessage("Simulasi Mockup: Membuka rute ke ${DummyData.defaultElder.address}")
                },
                onCallElder = {
                    showMockupMessage("Simulasi Mockup: Menghubungi ${lokasiData.elderName}")
                },
                onGuardianClick = {
                    showMockupMessage("Simulasi Mockup: Detail penjaga terdekat ${lokasiData.guardianName} (${lokasiData.guardianDistance})")
                }
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        )
    }
}

/**
 * Panel bawah lokasi.
 *
 * - [expanded] true: kartu Oma + tombol aksi + kartu penjaga terlihat.
 * - [expanded] false: hanya garis (handle) dan kartu Oma, peta lebih luas.
 * - Handle bisa ditarik naik (buka) / turun (tutup) dan bisa dipencet untuk
 *   bolak-balik. Kalau [onExpandedChange] null, handle hanya hiasan.
 * - [maxHeight] diisi agar isi panel bisa di-scroll di layar kecil. Kalau
 *   Dp.Unspecified (default), panel memakai tinggi isinya (tanpa scroll).
 */
@Composable
fun LokasiBottomSheet(
    lokasiData: LokasiData,
    expanded: Boolean = true,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    maxHeight: Dp = Dp.Unspecified,
    onShowRoute: () -> Unit = {},
    onCallElder: () -> Unit = {},
    onGuardianClick: () -> Unit = {}
) {
    val colors = KetukTheme.colors
    val hasMaxHeight = maxHeight != Dp.Unspecified

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (hasMaxHeight) Modifier.heightIn(max = maxHeight) else Modifier),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = colors.sheetBackground,
        border = BorderStroke(1.dp, colors.cardBorder)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SheetHandle(
                expanded = expanded,
                onExpandedChange = onExpandedChange
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .then(
                        if (hasMaxHeight) Modifier.verticalScroll(rememberScrollState())
                        else Modifier
                    )
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElderInfoCard(lokasiData)

                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))

                        ActionButtons(
                            onShowRoute = onShowRoute,
                            onCallElder = onCallElder
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        GuardianCard(
                            lokasiData = lokasiData,
                            onClick = onGuardianClick
                        )
                    }
                }
            }
        }
    }
}

/** Area handle selebar panel (tinggi 44dp): ditarik naik/turun atau dipencet. */
@Composable
private fun SheetHandle(
    expanded: Boolean,
    onExpandedChange: ((Boolean) -> Unit)?
) {
    val interactive = onExpandedChange != null
    val thresholdPx = with(LocalDensity.current) { 36.dp.toPx() }
    var dragTotal by remember { mutableFloatStateOf(0f) }
    val dragState = rememberDraggableState { delta -> dragTotal += delta }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .draggable(
                state = dragState,
                orientation = Orientation.Vertical,
                enabled = interactive,
                onDragStarted = { dragTotal = 0f },
                onDragStopped = { velocity ->
                    when {
                        dragTotal < -thresholdPx || velocity < -800f -> onExpandedChange?.invoke(true)
                        dragTotal > thresholdPx || velocity > 800f -> onExpandedChange?.invoke(false)
                    }
                }
            )
            .clickable(
                enabled = interactive,
                onClickLabel = if (expanded) "Perkecil panel" else "Perbesar panel",
                role = Role.Button
            ) { onExpandedChange?.invoke(!expanded) }
            .semantics {
                contentDescription = "Panel lokasi"
                stateDescription = if (expanded) "Terbuka" else "Tertutup"
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun ElderInfoCard(lokasiData: LokasiData) {
    val colors = KetukTheme.colors
    val unifiedAge = DummyData.defaultElder.age
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardNeutral),
        border = BorderStroke(1.dp, colors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.oma_avatar),
                contentDescription = lokasiData.elderName,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = colors.chipAman,
                    modifier = Modifier.heightIn(min = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(colors.accentGreen)
                        )
                        Text(
                            text = lokasiData.status,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = colors.accentGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = lokasiData.elderName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$unifiedAge tahun - ${lokasiData.elderLocation}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun ActionButtons(
    onShowRoute: () -> Unit = {},
    onCallElder: () -> Unit = {}
) {
    val colors = KetukTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onShowRoute,
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 52.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Navigation,
                contentDescription = null,
                tint = colors.onAccent,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Lihat Rute",
                color = colors.onAccent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        OutlinedButton(
            onClick = onCallElder,
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 52.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = colors.cardNeutral
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Icon(
                imageVector = Icons.Rounded.Call,
                contentDescription = null,
                tint = colors.textPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Hubungi Oma",
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun GuardianCard(
    lokasiData: LokasiData,
    onClick: () -> Unit = {}
) {
    val colors = KetukTheme.colors
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardNeutral),
        border = BorderStroke(1.dp, colors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.penjaga_lintang),
                contentDescription = lokasiData.guardianName,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Penjaga terdekat",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lokasiData.guardianName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    tint = colors.accentGreen,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = lokasiData.guardianDistance,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LokasiScreenPreview() {
    KetukTheme {
        LokasiScreen()
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 568)
@Composable
fun LokasiScreenPreviewSmall() {
    KetukTheme {
        LokasiScreen()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, fontScale = 1.3f)
@Composable
fun LokasiScreenPreviewLargeFont() {
    KetukTheme {
        LokasiScreen()
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 1280)
@Composable
fun LokasiScreenPreviewTablet() {
    KetukTheme {
        LokasiScreen()
    }
}

@Preview(name = "Mode Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LokasiScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        LokasiScreen()
    }
}