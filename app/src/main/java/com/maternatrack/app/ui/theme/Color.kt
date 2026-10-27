package com.maternatrack.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp

/** Token warna dari wireframe docs/maternatrack.pen. */
object MtColor {
    val Bg = Color(0xFFF4EFFF)
    val BgSheet = Color(0xFFFBF9FF)
    val Surface = Color(0xFFFFFFFF)
    val Line = Color(0xFFE6DEF2)
    val Ink = Color(0xFF2F2532)
    val InkMuted = Color(0xFF857A88)
    val Primary = Color(0xFFF2577C)
    val PinkSoft = Color(0xFFFFE3EA)
    val Header = Color(0xFFFFD95C)
    val Error = Color(0xFFE04F64)
    val DayEmpty = Color(0xFFEEEAF3)
    val LimeTint = Color(0xFFF1FAE6)

    // Warna kategori: kuat, mid (latar), deep (ikon/teks)
    val Lemon = Color(0xFFFFC93C)
    val LemonMid = Color(0xFFFFE58F)
    val LemonDeep = Color(0xFFB27A00)
    val Melon = Color(0xFFFF6F8E)
    val MelonMid = Color(0xFFFFC9D5)
    val MelonDeep = Color(0xFFD63A5E)
    val Peach = Color(0xFFFFA65C)
    val PeachMid = Color(0xFFFFD5B0)
    val PeachDeep = Color(0xFFC9661A)
    val Lime = Color(0xFF7FCB45)
    val LimeMid = Color(0xFFCDEFA8)
    val LimeDeep = Color(0xFF4C8F1E)
    val Grape = Color(0xFFA88BFA)
    val GrapeMid = Color(0xFFDCCFFF)
    val GrapeDeep = Color(0xFF7452D6)
    val Sky = Color(0xFF5EBEF0)
    val SkyMid = Color(0xFFC8EAFB)
    val SkyDeep = Color(0xFF1F86BE)
}

object MtRadius {
    val Lg = 24.dp
    val Md = 18.dp
    val Sm = 12.dp
    val Xs = 8.dp
}

// Titik skala warna progres: pink (0%) -> coral -> kuning-oranye -> hijau-kuning -> hijau (100%)
private val titikProgres = listOf(
    0 to Color(0xFFF2577C),
    40 to Color(0xFFFF7F66),
    65 to Color(0xFFFFAA3B),
    85 to Color(0xFFB9CC3A),
    100 to Color(0xFF7FCB45)
)

/** Warna cincin/bar progres sesuai persentase, diinterpolasi antar titik skala. */
fun warnaProgres(persen: Int): Color {
    val p = persen.coerceIn(0, 100)
    for (i in 1 until titikProgres.size) {
        val (batasAtas, warnaAtas) = titikProgres[i]
        if (p <= batasAtas) {
            val (batasBawah, warnaBawah) = titikProgres[i - 1]
            val fraksi = (p - batasBawah).toFloat() / (batasAtas - batasBawah)
            return lerp(warnaBawah, warnaAtas, fraksi)
        }
    }
    return titikProgres.last().second
}

/** Warna lintasan cincin: warna progres dicampur 78% putih. */
fun warnaTrack(persen: Int): Color = lerp(warnaProgres(persen), Color.White, 0.78f)

/** Warna teks chip status: warna progres dicampur 35% Ink agar terbaca. */
fun warnaTeksStatus(persen: Int): Color = lerp(warnaProgres(persen), MtColor.Ink, 0.35f)
