package com.chat.ketukprojectmap.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.ketukprojectmap.ui.theme.KetukTheme
import com.chat.ketukprojectmap.ui.theme.Terakota

/**
 * Tombol Keluar outline standar aplikasi Ketuk.
 * Bentuk, tinggi (52dp), radius (12dp), outline Terakota, dan tipografi seragam
 * untuk dipakai bersama di ProfileScreen dan OmaPassiveScreen.
 */
@Composable
fun KetukLogoutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoggingOut: Boolean = false,
    text: String = "Keluar",
    loadingText: String = "Keluar..."
) {
    OutlinedButton(
        onClick = onClick,
        enabled = !isLoggingOut,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = Terakota,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Terakota.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.5.dp, if (isLoggingOut) Terakota.copy(alpha = 0.5f) else Terakota)
    ) {
        if (isLoggingOut) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = Terakota
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = if (isLoggingOut) loadingText else text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KetukLogoutButtonPreview() {
    KetukTheme {
        KetukLogoutButton(onClick = {})
    }
}

@Preview(name = "Mode Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun KetukLogoutButtonDarkPreview() {
    KetukTheme(darkTheme = true) {
        KetukLogoutButton(onClick = {})
    }
}
