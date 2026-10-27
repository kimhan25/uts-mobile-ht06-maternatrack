package com.maternatrack.app.logic

import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.RingkasanHarian

const val DISCLAIMER = "Info edukasi umum, bukan saran medis."

/**
 * Pesan edukatif umum berdasarkan kelompok yang belum terpenuhi.
 * Urutan prioritas: sayur, buah, protein (hewani dan nabati sama-sama kosong), pokok.
 * Tidak memberi target gizi, dosis, atau diagnosis.
 */
fun pesanEdukasi(ringkasan: RingkasanHarian?): String {
    val terpenuhi = ringkasan?.kelompokTerpenuhi ?: emptySet()
    return when {
        KelompokMakanan.SAYUR !in terpenuhi ->
            "Sayur dan buah beragam warna membawa vitamin dan mineral yang berbeda. Coba variasikan warnanya tiap hari!"
        KelompokMakanan.BUAH !in terpenuhi ->
            "Buah bisa jadi camilan segar di sela waktu makan. Pilih buah dengan warna yang beragam."
        KelompokMakanan.HEWANI !in terpenuhi && KelompokMakanan.NABATI !in terpenuhi ->
            "Lauk hewani dan nabati sama-sama sumber protein. Coba variasikan ikan, telur, tahu, dan tempe."
        KelompokMakanan.POKOK !in terpenuhi ->
            "Makanan pokok tidak harus nasi. Jagung, ubi, dan kentang juga bisa jadi pilihan."
        else ->
            "Isi piring Bunda sudah beragam. Pertahankan variasinya, dan konsultasikan kebutuhan khusus dengan tenaga kesehatan."
    }
}
