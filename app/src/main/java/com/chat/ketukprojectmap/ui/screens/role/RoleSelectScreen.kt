package com.chat.ketukprojectmap.ui.screens.role

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.ui.components.KetukButton
import com.chat.ketukprojectmap.ui.theme.*

@Composable
fun RoleSelectScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: RoleSelectViewModel = viewModel()
) {
    val selectedRole by viewModel.selectedRole.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Tombol kembali
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Kembali",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Judul
        Text(
            text = "Siapa kamu?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subjudul
        Text(
            text = "Pilih peranmu untuk melanjutkan",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Kartu 1: Wali
        RoleCard(
            illustration = R.drawable.wali_dengan_tok,
            title = "Saya wali Oma dan Opa",
            description = "Saya adalah anak, cucu, atau keluarga yang merawat Oma dan Opa.",
            backgroundColor = CardWali,
            borderColor = BorderWali,
            tintColor = TintWali,
            isSelected = selectedRole == UserRole.WALI,
            onClick = { viewModel.selectRole(UserRole.WALI) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Kartu 2: Penjaga
        RoleCard(
            illustration = R.drawable.rumah_desa,
            title = "Saya penjaga",
            description = "Saya adalah tetangga atau orang yang paling dekat dari rumah Oma dan Opa.",
            backgroundColor = CardPenjaga,
            borderColor = BorderPenjaga,
            tintColor = TintPenjaga,
            isSelected = selectedRole == UserRole.PENJAGA,
            onClick = { viewModel.selectRole(UserRole.PENJAGA) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Kartu 3: Oma/Opa
        RoleCard(
            illustration = R.drawable.oma_daun,
            title = "Saya adalah Oma/Opa",
            description = "Saya ingin memantau kesehatan dan aktivitas saya sendiri.",
            backgroundColor = CardOma,
            borderColor = BorderOma,
            tintColor = TintOma,
            isSelected = selectedRole == UserRole.OMA_OPA,
            onClick = { viewModel.selectRole(UserRole.OMA_OPA) }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Tombol Lanjutkan
        KetukButton(
            text = "Lanjutkan",
            onClick = {
                // TODO: Peran penjaga dan oma/opa juga menuju home sementara
                onNavigateToHome()
            },
            enabled = selectedRole != null
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun RoleCard(
    illustration: Int,
    title: String,
    description: String,
    backgroundColor: Color,
    borderColor: Color,
    tintColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = tween(300),
        label = "scale"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else borderColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Konten teks
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 64.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Ilustrasi
                Image(
                    painter = painterResource(illustration),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.CenterStart
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Judul
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Deskripsi
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
            }

            // Tombol panah di pojok kanan bawah
            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.BottomEnd),
                shape = CircleShape,
                color = tintColor,
                onClick = onClick
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Arang
                    )
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun RoleSelectScreenPreview() {
    KetukTheme {
        RoleSelectScreen(
            onNavigateBack = {},
            onNavigateToHome = {}
        )
    }
}


