package com.chat.ketukprojectmap.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chat.ketukprojectmap.ui.theme.KetukTheme
import com.chat.ketukprojectmap.ui.theme.LocalDarkThemeToggle
import com.chat.ketukprojectmap.ui.theme.Terakota

/**
 * Kartu pengaturan Switch Mode Gelap standar aplikasi Ketuk.
 * Menampilkan status "Aktif" / "Nonaktif", ikon mode, dan Switch dengan kontras
 * yang jelas di mode terang maupun mode gelap (menggunakan palet semantik KetukTheme.colors).
 * Dipakai bersama di ProfileScreen dan OmaPassiveScreen.
 */
@Composable
fun KetukDarkModeCard(
    modifier: Modifier = Modifier
) {
    val isDarkTheme = KetukTheme.isDark
    val onToggleDarkTheme = LocalDarkThemeToggle.current
    val colors = KetukTheme.colors

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
        border = BorderStroke(1.dp, colors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f, fill = false),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(38.dp),
                    shape = CircleShape,
                    color = Terakota.copy(alpha = 0.14f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                            contentDescription = "Mode gelap",
                            tint = Terakota,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Mode gelap",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary
                    )
                    Text(
                        text = if (isDarkTheme) "Aktif" else "Nonaktif",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Switch(
                checked = isDarkTheme,
                onCheckedChange = { onToggleDarkTheme(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colors.onAccent,
                    checkedTrackColor = Terakota,
                    checkedBorderColor = Terakota,
                    uncheckedThumbColor = colors.textSecondary,
                    uncheckedTrackColor = colors.divider,
                    uncheckedBorderColor = colors.cardBorder
                )
            )
        }
    }
}

@Preview(name = "Mode Terang", showBackground = true)
@Composable
fun KetukDarkModeCardPreview() {
    KetukTheme(darkTheme = false) {
        KetukDarkModeCard()
    }
}

@Preview(name = "Mode Gelap", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun KetukDarkModeCardDarkPreview() {
    KetukTheme(darkTheme = true) {
        KetukDarkModeCard()
    }
}
