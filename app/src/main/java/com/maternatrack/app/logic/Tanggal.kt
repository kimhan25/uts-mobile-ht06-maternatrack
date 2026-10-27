package com.maternatrack.app.logic

import java.time.LocalDate

private val NAMA_HARI = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
private val HARI_PENDEK = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
private val NAMA_BULAN = listOf(
    "Januari", "Februari", "Maret", "April", "Mei", "Juni",
    "Juli", "Agustus", "September", "Oktober", "November", "Desember"
)
private val BULAN_PENDEK = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")

// dayOfWeek.value: Senin = 1 ... Minggu = 7, monthValue: Januari = 1
fun namaHari(t: LocalDate) = NAMA_HARI[t.dayOfWeek.value - 1]
fun hariPendek(t: LocalDate) = HARI_PENDEK[t.dayOfWeek.value - 1]

/** "Selasa, 27 Oktober" */
fun formatPanjang(t: LocalDate) = "${namaHari(t)}, ${t.dayOfMonth} ${NAMA_BULAN[t.monthValue - 1]}"

/** "Senin, 26 Okt" atau "Hari ini, 27 Okt" */
fun formatKartu(t: LocalDate, hariIni: LocalDate): String {
    val depan = if (t == hariIni) "Hari ini" else namaHari(t)
    return "$depan, ${t.dayOfMonth} ${BULAN_PENDEK[t.monthValue - 1]}"
}

/** "Okt 2026" */
fun formatBulan(t: LocalDate) = "${BULAN_PENDEK[t.monthValue - 1]} ${t.year}"
