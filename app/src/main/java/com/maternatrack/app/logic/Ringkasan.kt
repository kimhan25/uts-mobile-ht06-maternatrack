package com.maternatrack.app.logic

import com.maternatrack.app.model.CatatanMakan
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.RingkasanHarian
import com.maternatrack.app.model.StatusHarian
import com.maternatrack.app.model.WaktuMakan
import java.time.LocalDate
import kotlin.math.roundToInt

/** Klasifikasi status dari persentase kelengkapan. */
fun statusDari(persen: Int): StatusHarian = when {
    persen <= 0 -> StatusHarian.MULAI
    persen < 60 -> StatusHarian.KURANG
    persen < 100 -> StatusHarian.HAMPIR
    else -> StatusHarian.LENGKAP
}

/**
 * Menggabungkan semua catatan pada [tanggal] menjadi satu ringkasan.
 * Komponen harian = 5 kelompok makanan + suplemen (suplemen hanya dihitung jika ada arahan nakes).
 */
fun ringkasanHari(
    tanggal: LocalDate,
    catatan: List<CatatanMakan>,
    adaArahanSuplemen: Boolean
): RingkasanHarian {
    val catatanHariIni = catatan
        .filter { it.tanggal == tanggal }
        .sortedBy { it.waktu.ordinal }

    // Gabungan kelompok dari semua waktu makan (Set otomatis membuang duplikat)
    val kelompok = mutableSetOf<KelompokMakanan>()
    for (c in catatanHariIni) {
        kelompok.addAll(c.porsi.keys)
    }
    val suplemen = adaArahanSuplemen && catatanHariIni.any { it.minumSuplemen }

    val jumlahKomponen = if (adaArahanSuplemen) 6 else 5
    val jumlahTerpenuhi = kelompok.size + (if (suplemen) 1 else 0)
    val persen = (jumlahTerpenuhi * 100.0 / jumlahKomponen).roundToInt()

    return RingkasanHarian(
        tanggal = tanggal,
        catatan = catatanHariIni,
        kelompokTerpenuhi = kelompok,
        suplemenTerpenuhi = suplemen,
        jumlahTerpenuhi = jumlahTerpenuhi,
        jumlahKomponen = jumlahKomponen,
        persentase = persen,
        status = statusDari(persen)
    )
}

/** Ringkasan untuk setiap tanggal yang punya catatan, tanggal terbaru lebih dulu. */
fun ringkasHarian(catatan: List<CatatanMakan>, adaArahanSuplemen: Boolean): List<RingkasanHarian> =
    catatan
        .map { it.tanggal }
        .distinct()
        .sortedDescending()
        .map { ringkasanHari(it, catatan, adaArahanSuplemen) }

/** Total porsi per kelompok pada satu hari, dipakai di sheet Detail Hari. */
fun totalPorsi(ringkasan: RingkasanHarian): Map<KelompokMakanan, Int> {
    val total = mutableMapOf<KelompokMakanan, Int>()
    for (c in ringkasan.catatan) {
        for ((kelompok, porsi) in c.porsi) {
            total[kelompok] = (total[kelompok] ?: 0) + porsi
        }
    }
    return total
}

/**
 * Waktu makan yang otomatis dipilih: Sarapan, Siang, lalu Malam yang belum dicatat.
 * Jika ketiganya sudah dicatat, Camilan (boleh berulang).
 */
fun waktuBerikutnya(tanggal: LocalDate, catatan: List<CatatanMakan>): WaktuMakan {
    val sudah = catatan.filter { it.tanggal == tanggal }.map { it.waktu }
    return WaktuMakan.entries.firstOrNull { !it.bolehBerulang && it !in sudah } ?: WaktuMakan.CAMILAN
}
