package com.chat.ketukprojectmap.ui.screens.verification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.ui.theme.Arang
import com.chat.ketukprojectmap.ui.theme.KetukTheme
import com.chat.ketukprojectmap.ui.theme.KremBackground
import com.chat.ketukprojectmap.ui.theme.Terakota

@Composable
fun VerificationScreen(
    onConfirm: () -> Unit,
    onResend: () -> Unit = {},
    viewModel: VerificationViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    // Teks (ubah di sini kalau mau diterjemahkan ke Indonesia)
    val confirmText = "Confirm"
    val resendQuestion = "Didn't receive the code? "
    val resendAction = "Resend"

    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(KremBackground)
    ) {
        // ===== Proporsi dari Figma (atur di sini) =====
        val cornerRadius = 32.dp
        val panelTop = maxHeight * 0.32f
        val mascotTop = maxHeight * 0.15f
        val mascotSize = minOf(maxWidth * 0.62f, maxHeight * 0.30f)
        val codeTop = maxOf(maxHeight * 0.555f - panelTop, 0.dp)
        val sidePadding = 32.dp

        // 1. Latar desa (mulai di bawah status bar)
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

        // 2. Panel krem (digambar sebelum maskot)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = panelTop)
                .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                .background(KremBackground)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(start = sidePadding, end = sidePadding, top = codeTop, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ===== Empat kotak kode (satu input tersembunyi) =====
            BasicTextField(
                value = state.code,
                onValueChange = viewModel::onCodeChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                cursorBrush = SolidColor(Color.Transparent),
                textStyle = TextStyle(color = Color.Transparent),
                decorationBox = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        repeat(VerificationViewModel.CODE_LENGTH) { index ->
                            CodeBox(
                                digit = state.code.getOrNull(index)?.toString() ?: "",
                                isActive = index == minOf(
                                    state.code.length,
                                    VerificationViewModel.CODE_LENGTH - 1
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            )

            // Pesan error
            state.errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, fontSize = 12.sp, color = Color(0xFFB3261E))
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ===== Tombol Confirm =====
            Button(
                onClick = { viewModel.onConfirm(onSuccess = onConfirm) },
                enabled = state.isCodeComplete && !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Terakota,
                    disabledContainerColor = Terakota.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = if (state.isLoading) "Memeriksa..." else confirmText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ===== Teks Resend =====
            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    text = resendQuestion,
                    fontSize = 12.sp,
                    color = Arang
                )
                Text(
                    text = if (state.resendSeconds > 0) {
                        "$resendAction (${state.resendSeconds}s)"
                    } else {
                        resendAction
                    },
                    fontSize = 12.sp,
                    color = if (state.canResend) Color(0xFF4A86C5) else Arang.copy(alpha = 0.5f),
                    modifier = Modifier.clickable(enabled = state.canResend) {
                        viewModel.onResend()
                        onResend()
                    }
                )
            }
        }

        // 3. Maskot paling atas, menimpa lengkungan panel
        Image(
            painter = painterResource(R.drawable.maskot_hp),
            contentDescription = "Maskot Tok memegang HP",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = mascotTop)
                .size(mascotSize),
            contentScale = ContentScale.Fit
        )
    }
}

/** Satu kotak digit: latar peach, border oren, garis oren di bawah digit kalau aktif. */
@Composable
private fun CodeBox(
    digit: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .aspectRatio(0.95f)
            .clip(shape)
            .background(Terakota.copy(alpha = 0.12f))
            .border(1.dp, Terakota.copy(alpha = 0.35f), shape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            fontSize = 30.sp,
            color = Arang
        )
        if (isActive) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
                    .width(24.dp)
                    .height(2.dp)
                    .background(Terakota)
            )
        }
    }
}

@Preview(name = "Normal", widthDp = 393, heightDp = 852, showBackground = true)
@Preview(name = "Kecil", widthDp = 320, heightDp = 568, showBackground = true)
@Composable
fun VerificationScreenPreview() {
    KetukTheme {
        VerificationScreen(onConfirm = {})
    }
}