package com.chat.ketukprojectmap.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Skema bentuk (Shapes) Material 3 custom proyek Ketuk.
 * - extraSmall / small: chip, badge, dan field input (8dp - 12dp)
 * - medium: kartu standar dan dialog (16dp)
 * - large: kartu utama profil/status dan panel atas (20dp - 24dp)
 * - extraLarge: bottom sheet dan tombol pill (28dp)
 */
val KetukShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)
