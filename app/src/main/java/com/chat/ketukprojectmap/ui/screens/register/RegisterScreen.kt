package com.chat.ketukprojectmap.ui.screens.register

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.data.remote.SupabaseProvider
import com.chat.ketukprojectmap.ui.components.AuthCloud
import com.chat.ketukprojectmap.ui.components.AuthField
import com.chat.ketukprojectmap.ui.components.FadeSlideIn
import com.chat.ketukprojectmap.ui.theme.*
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    // DIUBAH: sekarang membawa email supaya layar verifikasi tahu ke mana kode dikirim
    onNeedVerification: (String) -> Unit = { onRegisterSuccess() },
    viewModel: RegisterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = KetukTheme.colors

    // Login Google: di mode demo langsung via FakeAuthRepository (800ms), di mode full pakai Supabase
    val googleSignIn = if (!com.chat.ketukprojectmap.BuildConfig.DEMO_MODE) {
        SupabaseProvider.client.composeAuth.rememberSignInWithGoogle(
            onResult = { result ->
                when (result) {
                    is NativeSignInResult.Success -> onRegisterSuccess()
                    is NativeSignInResult.ClosedByUser -> Unit
                    is NativeSignInResult.NetworkError ->
                        viewModel.onGoogleError("Tidak ada koneksi internet")
                    is NativeSignInResult.Error ->
                        viewModel.onGoogleError(result.message)
                }
            }
        )
    } else {
        null
    }
    val onGoogleClick: () -> Unit = {
        if (com.chat.ketukprojectmap.BuildConfig.DEMO_MODE) {
            viewModel.signInWithGoogleDemo(onRegisterSuccess)
        } else {
            googleSignIn?.startFlow()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.screenBackground)
    ) {
        // Dihitung di sini (di luar Column) supaya maxWidth bisa diakses
        val mascotSize = (maxWidth * 0.5f).coerceIn(140.dp, 220.dp)

        // ===== Awan di latar: posisi dan ukuran dalam persen layar (sama seperti Login) =====
        AuthCloud(
            resourceId = R.drawable.asset_awan,
            width = maxWidth * 0.24f,
            offsetX = maxWidth * 0.10f,
            offsetY = maxHeight * 0.096f
        )
        AuthCloud(
            resourceId = R.drawable.asset_awan,
            width = maxWidth * 0.22f,
            offsetX = -(maxWidth * 0.10f),
            offsetY = maxHeight * 0.168f
        )
        AuthCloud(
            resourceId = R.drawable.asset_awan,
            width = maxWidth * 0.25f,
            offsetX = maxWidth * 0.89f,
            offsetY = maxHeight * 0.115f
        )
        AuthCloud(
            resourceId = R.drawable.asset_awan,
            width = maxWidth * 0.20f,
            offsetX = maxWidth * 0.70f,
            offsetY = maxHeight * 0.176f
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxHeight()
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FadeSlideIn(delayMillis = 0) {
                RegisterHeader(mascotSize = mascotSize)
            }

            Spacer(modifier = Modifier.height(28.dp))

            RegisterForm(
                uiState = uiState,
                viewModel = viewModel,
                onRegisterSuccess = onRegisterSuccess,
                // DIUBAH: bungkus supaya email dari form ikut dikirim
                onNeedVerification = { onNeedVerification(uiState.email.trim()) },
                onNavigateToLogin = onNavigateToLogin,
                onGoogleClick = onGoogleClick
            )
        }
    }
}

@Composable
private fun RegisterHeader(mascotSize: Dp) {
    val colors = KetukTheme.colors
    Spacer(modifier = Modifier.height(48.dp))

    // Maskot + bayangan + daun (sama seperti Login)
    Box(
        modifier = Modifier.size(
            width = mascotSize * 1.22f,
            height = mascotSize * 1.05f
        ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 2.dp)
                .size(width = mascotSize * 0.67f, height = 12.dp)
                .clip(CircleShape)
                .background(colors.authFieldBackground)
        )
        Image(
            painter = painterResource(R.drawable.tok_memeriksa),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(mascotSize)
                .padding(bottom = 4.dp),
            contentScale = ContentScale.Fit
        )
        Image(
            painter = painterResource(R.drawable.asset_ketuk_daun),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = mascotSize * 0.11f, bottom = 2.dp)
                .size(mascotSize * 0.29f),
            contentScale = ContentScale.Fit
        )
    }

    Spacer(modifier = Modifier.height(36.dp))

    // Logo + teks Ketuk
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo_ketuk),
            contentDescription = null,
            modifier = Modifier
                .size(34.dp)
                .offset(y = (-2).dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Ketuk",
            modifier = Modifier.offset(y = 3.dp),
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Terakota,
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                )
            )
        )
    }

    Text(
        text = "Yuk, buat akun barumu",
        fontSize = 12.sp,
        color = colors.textPrimary,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun RegisterForm(
    uiState: RegisterUiState,
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    onNeedVerification: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onGoogleClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        FadeSlideIn(delayMillis = 80) {
            Column(modifier = Modifier.fillMaxWidth()) {
                AuthField(
                    label = "Nama Lengkap",
                    value = uiState.fullName,
                    onValueChange = viewModel::onFullNameChange,
                    placeholder = "Masukkan nama lengkap",
                    leadingIcon = Icons.Rounded.Person,
                    isError = uiState.fullNameError.isNotEmpty(),
                    errorMessage = uiState.fullNameError
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthField(
                    label = "Email",
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    placeholder = "Contoh: nama@email.com",
                    leadingIcon = Icons.Rounded.Email,
                    keyboardType = KeyboardType.Email,
                    isError = uiState.emailError.isNotEmpty(),
                    errorMessage = uiState.emailError
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        FadeSlideIn(delayMillis = 160) {
            AuthField(
                label = "Nomor HP",
                value = uiState.phoneNumber,
                onValueChange = viewModel::onPhoneNumberChange,
                placeholder = "Contoh: 0867 6767 6767",
                leadingIcon = Icons.Rounded.Phone,
                keyboardType = KeyboardType.Phone,
                isError = uiState.phoneNumberError.isNotEmpty(),
                errorMessage = uiState.phoneNumberError
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        FadeSlideIn(delayMillis = 240) {
            Column(modifier = Modifier.fillMaxWidth()) {
                AuthField(
                    label = "Password",
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                    placeholder = "Minimal 8 karakter",
                    leadingIcon = Icons.Rounded.Lock,
                    isPassword = true,
                    isError = uiState.passwordError.isNotEmpty(),
                    errorMessage = uiState.passwordError
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthField(
                    label = "Konfirmasi Password",
                    value = uiState.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    placeholder = "Masukkan ulang password",
                    leadingIcon = Icons.Rounded.Lock,
                    isPassword = true,
                    isError = uiState.confirmPasswordError.isNotEmpty(),
                    errorMessage = uiState.confirmPasswordError
                )

                // ===== Pesan error umum (email terdaftar, tidak ada internet, dst.) =====
                if (uiState.error.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.error,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        FadeSlideIn(delayMillis = 320) {
            RegisterFormButtons(
                uiState = uiState,
                onRegister = { viewModel.register(onRegisterSuccess, onNeedVerification) },
                onNavigateToLogin = onNavigateToLogin,
                onGoogleClick = onGoogleClick
            )
        }
    }
}

@Composable
private fun RegisterFormButtons(
    uiState: RegisterUiState,
    onRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onGoogleClick: () -> Unit
) {
    // Dihitung dari uiState supaya ikut berubah saat pengguna mengetik
    val emailRegex = remember { "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex() }
    val canSubmit = uiState.fullName.isNotBlank() &&
            uiState.email.isNotBlank() &&
            emailRegex.matches(uiState.email.trim()) &&
            uiState.phoneNumber.length in 9..13 &&
            uiState.password.length >= 8 &&
            uiState.confirmPassword == uiState.password

    val colors = KetukTheme.colors
    val fieldBg = colors.authFieldBackground
    val fieldBorder = colors.authFieldBorder

    // ===== Tombol Daftar =====
    Button(
        onClick = onRegister,
        enabled = canSubmit && !uiState.isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Terakota,
            disabledContainerColor = Terakota.copy(alpha = 0.5f)
        )
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = colors.onAccent,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "Daftar",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onAccent
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // ===== Pemisah "atau" =====
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = colors.textPrimary.copy(alpha = 0.6f)
        )
        Text(
            text = "atau",
            fontSize = 10.sp,
            color = colors.textPrimary,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = colors.textPrimary.copy(alpha = 0.6f)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // ===== Tombol Google =====
    OutlinedButton(
        onClick = onGoogleClick,
        enabled = !uiState.isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = fieldBg,
            contentColor = colors.textPrimary
        ),
        border = BorderStroke(1.dp, fieldBorder)
    ) {
        Image(
            painter = painterResource(R.drawable.logo_google),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Lanjut dengan Google",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // ===== Tautan ke Login =====
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sudah punya akun? ",
            fontSize = 12.sp,
            color = colors.textPrimary
        )
        Text(
            text = "Masuk",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.tertiary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onNavigateToLogin() }
        )
    }

    Spacer(modifier = Modifier.height(32.dp))
}

@Preview(name = "HP kecil", widthDp = 320, heightDp = 568, showBackground = true)
@Preview(name = "HP normal", widthDp = 360, heightDp = 800, showBackground = true)
@Preview(name = "HP besar", widthDp = 412, heightDp = 915, showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    KetukTheme {
        RegisterScreen(
            onNavigateToLogin = {},
            onRegisterSuccess = {}
        )
    }
}

@Preview(
    name = "Mode Gelap",
    widthDp = 360,
    heightDp = 800,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RegisterScreenDarkPreview() {
    KetukTheme(darkTheme = true) {
        RegisterScreen(
            onNavigateToLogin = {},
            onRegisterSuccess = {}
        )
    }
}
