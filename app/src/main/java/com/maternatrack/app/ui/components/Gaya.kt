package com.maternatrack.app.ui.components

import androidx.compose.ui.graphics.Color
import com.maternatrack.app.R
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.WaktuMakan
import com.maternatrack.app.ui.theme.MtColor

/** Tiga tingkat warna satu kategori: kuat (garis), mid (latar), deep (ikon/teks). */
data class WarnaKategori(val kuat: Color, val mid: Color, val deep: Color)

val Lemon = WarnaKategori(MtColor.Lemon, MtColor.LemonMid, MtColor.LemonDeep)
val Melon = WarnaKategori(MtColor.Melon, MtColor.MelonMid, MtColor.MelonDeep)
val Peach = WarnaKategori(MtColor.Peach, MtColor.PeachMid, MtColor.PeachDeep)
val Lime = WarnaKategori(MtColor.Lime, MtColor.LimeMid, MtColor.LimeDeep)
val Grape = WarnaKategori(MtColor.Grape, MtColor.GrapeMid, MtColor.GrapeDeep)
val Sky = WarnaKategori(MtColor.Sky, MtColor.SkyMid, MtColor.SkyDeep)
val Cookie = WarnaKategori(MtColor.Cookie, MtColor.CookieMid, MtColor.CookieDeep)

// Gambar dan warna tiap kelompok makanan (dipisah dari model agar model tetap Kotlin murni)
val KelompokMakanan.gambar: Int
    get() = when (this) {
        KelompokMakanan.POKOK -> R.drawable.art_pokok
        KelompokMakanan.HEWANI -> R.drawable.art_hewani
        KelompokMakanan.NABATI -> R.drawable.art_nabati
        KelompokMakanan.SAYUR -> R.drawable.art_sayur
        KelompokMakanan.BUAH -> R.drawable.art_buah
    }

val KelompokMakanan.warna: WarnaKategori
    get() = when (this) {
        KelompokMakanan.POKOK -> Lemon
        KelompokMakanan.HEWANI -> Melon
        KelompokMakanan.NABATI -> Peach
        KelompokMakanan.SAYUR -> Lime
        KelompokMakanan.BUAH -> Grape
    }

val WaktuMakan.gambar: Int
    get() = when (this) {
        WaktuMakan.SARAPAN -> R.drawable.art_sarapan
        WaktuMakan.SIANG -> R.drawable.art_siang
        WaktuMakan.MALAM -> R.drawable.art_malam
        WaktuMakan.CAMILAN -> R.drawable.art_camilan
    }

/** Nama yang dipakai di kalimat, misalnya "Makan malam tercatat". */
val WaktuMakan.namaLengkap: String
    get() = when (this) {
        WaktuMakan.SARAPAN -> "Sarapan"
        WaktuMakan.SIANG -> "Makan siang"
        WaktuMakan.MALAM -> "Makan malam"
        WaktuMakan.CAMILAN -> "Camilan"
    }

val WaktuMakan.warna: WarnaKategori
    get() = when (this) {
        WaktuMakan.SARAPAN -> Peach
        WaktuMakan.SIANG -> Lemon
        WaktuMakan.MALAM -> Grape
        WaktuMakan.CAMILAN -> Cookie
    }

/** Suplemen bukan kelompok makanan, tapi tampil berdampingan dengan warna langit. */
object Suplemen {
    const val LABEL = "Suplemen"
    val GAMBAR = R.drawable.art_suplemen
    val warna = Sky
}
