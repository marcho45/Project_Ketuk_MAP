package com.chat.ketukprojectmap.ui.screens.role

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.ui.components.KetukButton
import com.chat.ketukprojectmap.ui.theme.KetukTheme
import com.chat.ketukprojectmap.ui.theme.Terakota

@Composable
fun RoleSelectScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: RoleSelectViewModel = viewModel()
) {
    val selectedRole by viewModel.selectedRole.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val colors = KetukTheme.colors

    BackHandler(onBack = onNavigateBack)

    // Ukuran ilustrasi mengikuti tinggi layar, dibatasi 80dp sampai 96dp
    val illustrationSize = (configuration.screenHeightDp.dp * 0.11f).coerceIn(80.dp, 96.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.screenBackground)
            .systemBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Ikon di x = 32dp, sejajar dengan tepi kartu
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .padding(start = 20.dp)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Kembali",
                tint = colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            Text(
                text = "Siapa kamu?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Pilih peranmu untuk melanjutkan",
                fontSize = 12.sp,
                color = colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // fill = false: tombol menempel di bawah kartu kalau muat (sesuai Figma),
        // dan tetap terlihat di bawah layar kalau kartu perlu di-scroll
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 4.dp)
        ) {
            RoleCard(
                illustration = R.drawable.role_wali,
                illustrationSize = illustrationSize,
                title = "Saya wali Oma dan Opa",
                description = "Saya adalah anak, cucu, atau keluarga yang merawat Oma dan Opa.",
                isSelected = selectedRole == UserRole.WALI,
                onClick = { viewModel.selectRole(UserRole.WALI) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RoleCard(
                illustration = R.drawable.role_penjaga,
                illustrationSize = illustrationSize,
                title = "Saya penjaga Oma dan Opa",
                description = "Saya adalah tetangga atau orang yang paling dekat dari rumah Oma dan Opa.",
                isSelected = selectedRole == UserRole.PENJAGA,
                onClick = { viewModel.selectRole(UserRole.PENJAGA) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RoleCard(
                illustration = R.drawable.role_opa,
                illustrationSize = illustrationSize,
                title = "Saya adalah Oma/Opa",
                description = "Saya ingin memantau kesehatan dan aktivitas saya sendiri.",
                isSelected = selectedRole == UserRole.OMA_OPA,
                onClick = { viewModel.selectRole(UserRole.OMA_OPA) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pesan error simpan role
        if (state.error != null) {
            Text(
                text = state.error.orEmpty(),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 36.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Box(modifier = Modifier.padding(horizontal = 32.dp)) {
            KetukButton(
                text = if (state.isSaving) "Menyimpan..." else "Lanjutkan",
                onClick = {
                    // TODO: peran penjaga dan oma/opa sementara juga menuju home
                    viewModel.confirm(onSuccess = onNavigateToHome)
                },
                enabled = selectedRole != null && !state.isSaving
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun RoleCard(
    illustration: Int,
    illustrationSize: Dp,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = KetukTheme.colors
    // Terpilih: peach (terakota 15% di atas permukaan kartu). Tidak terpilih: permukaan kartu
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) {
            Terakota.copy(alpha = 0.15f).compositeOver(colors.cardSurface)
        } else {
            colors.cardSurface
        },
        animationSpec = tween(250),
        label = "containerColor"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Terakota.copy(alpha = 0.35f) else Color.Transparent,
        animationSpec = tween(250),
        label = "borderColor"
    )

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Ilustrasi di kiri, teks di kanan
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 26.dp, bottom = 26.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(illustration),
                    contentDescription = null,
                    modifier = Modifier.size(illustrationSize),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(12.dp))

                // end = 48.dp menyisakan ruang untuk tombol panah
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 48.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        color = colors.textPrimary
                    )
                }
            }

            // Tombol panah terakota di kanan bawah
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Terakota),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colors.onAccent,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(name = "Normal", showBackground = true)
@Composable
fun RoleSelectScreenPreview() {
    KetukTheme {
        RoleSelectScreen(onNavigateBack = {}, onNavigateToHome = {})
    }
}

@Preview(name = "Kecil", widthDp = 360, heightDp = 640, fontScale = 1.3f, showBackground = true)
@Composable
fun RoleSelectScreenSmallPreview() {
    KetukTheme {
        RoleSelectScreen(onNavigateBack = {}, onNavigateToHome = {})
    }
}

@Preview(name = "Mode Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RoleSelectScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        RoleSelectScreen(onNavigateBack = {}, onNavigateToHome = {})
    }
}