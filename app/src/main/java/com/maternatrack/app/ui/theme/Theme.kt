package com.maternatrack.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val skemaWarna = lightColorScheme(
    primary = MtColor.Primary,
    onPrimary = MtColor.Surface,
    primaryContainer = MtColor.PinkSoft,
    onPrimaryContainer = MtColor.Ink,
    secondary = MtColor.SkyDeep,
    background = MtColor.Bg,
    onBackground = MtColor.Ink,
    surface = MtColor.Surface,
    onSurface = MtColor.Ink,
    surfaceContainerLow = MtColor.BgSheet,
    surfaceContainerHigh = MtColor.Surface,
    onSurfaceVariant = MtColor.InkMuted,
    outline = MtColor.Line,
    error = MtColor.Error
)

private val dasar = Typography()

// Semua gaya teks bawaan Material 3 memakai Nunito, judul memakai Fredoka
private val tipografi = Typography(
    displayLarge = dasar.displayLarge.copy(fontFamily = Fredoka),
    displayMedium = dasar.displayMedium.copy(fontFamily = Fredoka),
    displaySmall = dasar.displaySmall.copy(fontFamily = Fredoka),
    headlineLarge = dasar.headlineLarge.copy(fontFamily = Fredoka),
    headlineMedium = dasar.headlineMedium.copy(fontFamily = Fredoka),
    headlineSmall = dasar.headlineSmall.copy(fontFamily = Fredoka),
    titleLarge = dasar.titleLarge.copy(fontFamily = Fredoka),
    titleMedium = dasar.titleMedium.copy(fontFamily = Nunito),
    titleSmall = dasar.titleSmall.copy(fontFamily = Nunito),
    bodyLarge = dasar.bodyLarge.copy(fontFamily = Nunito),
    bodyMedium = dasar.bodyMedium.copy(fontFamily = Nunito),
    bodySmall = dasar.bodySmall.copy(fontFamily = Nunito),
    labelLarge = dasar.labelLarge.copy(fontFamily = Nunito),
    labelMedium = dasar.labelMedium.copy(fontFamily = Nunito),
    labelSmall = dasar.labelSmall.copy(fontFamily = Nunito)
)

@Composable
fun MaternaTrackTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = skemaWarna, typography = tipografi, content = content)
}

/** Gaya teks Fredoka (judul dan angka). */
fun judul(ukuran: Int, berat: Int = 600) =
    TextStyle(fontFamily = Fredoka, fontSize = ukuran.sp, fontWeight = FontWeight(berat))

/** Gaya teks Nunito (isi). */
fun isi(ukuran: Int, berat: Int = 600) =
    TextStyle(fontFamily = Nunito, fontSize = ukuran.sp, fontWeight = FontWeight(berat))
