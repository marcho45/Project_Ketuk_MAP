package com.chat.ketukprojectmap.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.activity.compose.BackHandler
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.ketukprojectmap.R
import com.chat.ketukprojectmap.data.remote.SupabaseProvider
import com.chat.ketukprojectmap.ui.theme.Arang
import com.chat.ketukprojectmap.ui.theme.KetukTheme
import com.chat.ketukprojectmap.ui.theme.Terakota
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth

/** Bagian yang tersambung ke backend: ViewModel dan Google. */
@Composable
fun LoginScreen(
    onNavigateToRole: () -> Unit,
    onNavigateToVerification: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    val googleAuth = SupabaseProvider.client.composeAuth.rememberSignInWithGoogle(
        onResult = { result -> viewModel.onGoogleResult(result, onNavigateToRole) },
        fallback = { /* login native tidak tersedia di perangkat ini */ }
    )

    LoginContent(
        isLoading = state.isLoading,
        error = state.error,
        onLogin = { email, password ->
            viewModel.login(email, password, onNavigateToRole, onNavigateToVerification)
        },
        onGoogleClick = { googleAuth.startFlow() },
        onNavigateToRegister = onNavigateToRegister,
        onNavigateBack = onNavigateBack,
        onClearError = viewModel::clearError
    )
}

/** Tampilan murni, tanpa backend, jadi aman dipakai di preview. */
@Composable
fun LoginContent(
    isLoading: Boolean,
    error: String?,
    onLogin: (email: String, password: String) -> Unit,
    onGoogleClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateBack: () -> Unit = {},
    onClearError: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Handle tombol back fisik HP
    BackHandler(onBack = onNavigateBack)

    // Warna sesuai Figma: latar field peach + border terakota
    val fieldBg = Terakota.copy(alpha = 0.12f)
    val fieldBorder = Terakota.copy(alpha = 0.35f)
    val fieldShape = RoundedCornerShape(12.dp)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        // ===== Ukuran maskot ikut lebar layar =====
        val mascotSize = (maxWidth * 0.5f).coerceIn(140.dp, 220.dp)

        // ===== Awan di latar (posisi dan ukuran dalam persen layar) =====
        // kiri atas
        Cloud(
            width = maxWidth * 0.24f,
            modifier = Modifier.offset(x = maxWidth * 0.10f, y = maxHeight * 0.096f)
        )
        // menempel tepi kiri (sebagian keluar layar)
        Cloud(
            width = maxWidth * 0.22f,
            modifier = Modifier.offset(x = -(maxWidth * 0.10f), y = maxHeight * 0.168f)
        )
        // menempel tepi kanan atas (sebagian keluar layar)
        Cloud(
            width = maxWidth * 0.25f,
            modifier = Modifier.offset(x = maxWidth * 0.89f, y = maxHeight * 0.115f)
        )
        // kanan tengah
        Cloud(
            width = maxWidth * 0.20f,
            modifier = Modifier.offset(x = maxWidth * 0.70f, y = maxHeight * 0.176f)
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
            Spacer(modifier = Modifier.height(48.dp))

            // ===== Maskot + bayangan + daun =====
            Box(
                modifier = Modifier.size(
                    width = mascotSize * 1.22f,
                    height = mascotSize * 1.05f
                ),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Bayangan oval di bawah kaki
                Box(
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .size(width = mascotSize * 0.67f, height = 12.dp)
                        .clip(CircleShape)
                        .background(Terakota.copy(alpha = 0.12f))
                )
                // Maskot
                Image(
                    painter = painterResource(R.drawable.tok_memeriksa),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(mascotSize)
                        .padding(bottom = 4.dp),
                    contentScale = ContentScale.Fit
                )
                // Daun di kanan bawah kaki maskot
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

            // ===== Logo + teks Ketuk =====
            val logoOffsetY = (-2).dp   // positif = logo turun, negatif = logo naik
            val textOffsetY = 3.dp      // positif = teks turun, negatif = teks naik

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_ketuk),
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)
                        .offset(y = logoOffsetY),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Ketuk",
                    modifier = Modifier.offset(y = textOffsetY),
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
                text = "Halo! Senang melihatmu kembali",
                fontSize = 12.sp,
                color = Arang,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ===== Field Email =====
            LoginField(
                value = email,
                onValueChange = {
                    email = it
                    onClearError()
                },
                label = "Email",
                hint = "Contoh: nama@email.com",
                leadingIcon = Icons.Rounded.Email,
                keyboardType = KeyboardType.Email,
                background = fieldBg,
                shape = fieldShape
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ===== Field Password =====
            LoginField(
                value = password,
                onValueChange = {
                    password = it
                    onClearError()
                },
                label = "Password",
                hint = "Masukkan password",
                leadingIcon = Icons.Rounded.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailing = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password",
                            tint = Terakota
                        )
                    }
                },
                background = fieldBg,
                shape = fieldShape
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ===== Pesan error =====
            if (error != null) {
                Text(
                    text = error,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, bottom = 8.dp)
                )
            }

            // ===== Tombol Masuk =====
            Button(
                onClick = { onLogin(email, password) },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Terakota,
                    disabledContainerColor = Terakota.copy(alpha = 0.5f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Masuk",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
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
                    color = Arang.copy(alpha = 0.6f)
                )
                Text(
                    text = "atau",
                    fontSize = 10.sp,
                    color = Arang,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Arang.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== Tombol Google =====
            OutlinedButton(
                onClick = onGoogleClick,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = fieldBg,
                    contentColor = Arang
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

            // ===== Tautan Daftar =====
            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Belum punya akun? ",
                    fontSize = 12.sp,
                    color = Arang
                )
                Text(
                    text = "Daftar disini",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.tertiary, // hijau
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // ===== Tombol Kembali =====
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .systemBarsPadding()
                .padding(start = 8.dp, top = 4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Kembali",
                tint = Terakota,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

/** Field outlined oren: label berupa Text di atas, ikon di kiri. */
@Composable
private fun LoginField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    hint: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType,
    background: Color,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Label: tulisan saja, tanpa kotak
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Terakota,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = shape,
            placeholder = { Text(hint, fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = trailing,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(fontSize = 14.sp, color = Arang),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Terakota,
                unfocusedBorderColor = Terakota,
                focusedLeadingIconColor = Terakota,
                unfocusedLeadingIconColor = Terakota,
                focusedTrailingIconColor = Terakota,
                unfocusedTrailingIconColor = Terakota,
                focusedContainerColor = background,
                unfocusedContainerColor = background,
                focusedTextColor = Arang,
                unfocusedTextColor = Arang,
                focusedPlaceholderColor = Arang.copy(alpha = 0.55f),
                unfocusedPlaceholderColor = Arang.copy(alpha = 0.55f),
                cursorColor = Terakota
            )
        )
    }
}

/** Awan dekorasi latar dari aset asset_awan.png. */
@Composable
private fun Cloud(width: Dp, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.asset_awan),
        contentDescription = null,
        modifier = modifier.width(width),
        contentScale = ContentScale.FillWidth
    )
}

@androidx.compose.ui.tooling.preview.Preview(name = "HP kecil", widthDp = 320, heightDp = 568, showBackground = true)
@androidx.compose.ui.tooling.preview.Preview(name = "HP normal", widthDp = 360, heightDp = 800, showBackground = true)
@androidx.compose.ui.tooling.preview.Preview(name = "HP besar", widthDp = 412, heightDp = 915, showBackground = true)
@androidx.compose.ui.tooling.preview.Preview(name = "Tablet", widthDp = 800, heightDp = 1280, showBackground = true)
@Composable
fun LoginScreenPreview() {
    KetukTheme {
        LoginContent(
            isLoading = false,
            error = null,
            onLogin = { _, _ -> },
            onGoogleClick = {},
            onNavigateToRegister = {}
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(name = "Dengan error", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun LoginScreenErrorPreview() {
    KetukTheme {
        LoginContent(
            isLoading = false,
            error = "Email atau password salah",
            onLogin = { _, _ -> },
            onGoogleClick = {},
            onNavigateToRegister = {}
        )
    }
}