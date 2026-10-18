package com.chat.ketukprojectmap.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.ketukprojectmap.ui.theme.KetukTheme
import com.chat.ketukprojectmap.ui.theme.Terakota

/**
 * Field untuk layar autentikasi (Login, Register).
 * Label berupa Text di atas field (bukan label bawaan OutlinedTextField),
 * supaya tidak ada kotak putih di garis border.
 */
@Composable
fun AuthField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val fieldBg = KetukTheme.colors.authFieldBackground
    val textPrimary = KetukTheme.colors.textPrimary
    val placeholderColor = textPrimary.copy(alpha = 0.55f)
    val errorColor = MaterialTheme.colorScheme.error

    Column(modifier = modifier.fillMaxWidth()) {
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
            isError = isError,
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 14.sp
                )
            },
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else null,
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password",
                            tint = Terakota
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            ),
            textStyle = TextStyle(fontSize = 14.sp, color = textPrimary),
            colors = OutlinedTextFieldDefaults.colors(
                // Border
                focusedBorderColor = Terakota,
                unfocusedBorderColor = Terakota,
                errorBorderColor = errorColor,
                // Latar peach
                focusedContainerColor = fieldBg,
                unfocusedContainerColor = fieldBg,
                errorContainerColor = fieldBg,
                // Ikon
                focusedLeadingIconColor = Terakota,
                unfocusedLeadingIconColor = Terakota,
                errorLeadingIconColor = errorColor,
                focusedTrailingIconColor = Terakota,
                unfocusedTrailingIconColor = Terakota,
                errorTrailingIconColor = errorColor,
                // Teks
                focusedTextColor = textPrimary,
                unfocusedTextColor = textPrimary,
                errorTextColor = textPrimary,
                focusedPlaceholderColor = placeholderColor,
                unfocusedPlaceholderColor = placeholderColor,
                errorPlaceholderColor = placeholderColor,
                cursorColor = Terakota,
                errorCursorColor = errorColor
            )
        )

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                fontSize = 11.sp,
                color = errorColor,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

/**
 * Awan dekorasi latar. Yang diatur hanya lebar (tinggi mengikuti rasio gambar),
 * supaya bentuknya sama dengan awan di LoginScreen.
 */
@Composable
fun AuthCloud(
    resourceId: Int,
    width: Dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = resourceId),
        contentDescription = null,
        modifier = modifier
            .offset(x = offsetX, y = offsetY)
            .width(width),
        contentScale = ContentScale.FillWidth
    )
}