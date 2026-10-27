package com.maternatrack.app.model

import java.time.LocalDate

/** Waktu makan dalam sehari. Sarapan, Siang, dan Malam hanya boleh dicatat sekali per tanggal. */
enum class WaktuMakan(val label: String, val bolehBerulang: Boolean) {
    SARAPAN("Sarapan", false),
    SIANG("Siang", false),
    MALAM("Malam", false),
    CAMILAN("Camilan", true)
}

/** Lima kelompok makanan Isi Piringku yang bisa dicatat beserta porsinya. */
enum class KelompokMakanan(val label: String, val labelPendek: String, val contoh: String) {
    POKOK("Makanan pokok", "Pokok", "Nasi, jagung, ubi"),
    HEWANI("Lauk hewani", "Hewani", "Ikan, telur, ayam"),
    NABATI("Lauk nabati", "Nabati", "Tahu, tempe, kacang"),
    SAYUR("Sayur", "Sayur", "Bayam, brokoli, wortel"),
    BUAH("Buah", "Buah", "Jeruk, pisang, pepaya")
}

/**
 * Satu catatan makan yang disimpan pengguna.
 * [porsi] hanya berisi kelompok yang dipilih, nilainya 1 sampai 5.
 */
data class CatatanMakan(
    val id: Long,
    val tanggal: LocalDate,
    val waktu: WaktuMakan,
    val porsi: Map<KelompokMakanan, Int>,
    val minumSuplemen: Boolean,
    val cerita: String
)

/** Status kelengkapan isi piring dalam satu hari, ditentukan dari persentase. */
enum class StatusHarian(val label: String) {
    MULAI("Belum ada isi piring"),
    KURANG("Yuk dilengkapi"),
    HAMPIR("Sedikit lagi lengkap"),
    LENGKAP("Hebat! Semua terpenuhi")
}

/** Hasil olahan semua catatan pada satu tanggal. */
data class RingkasanHarian(
    val tanggal: LocalDate,
    val catatan: List<CatatanMakan>,
    val kelompokTerpenuhi: Set<KelompokMakanan>,
    val suplemenTerpenuhi: Boolean,
    val jumlahTerpenuhi: Int,
    val jumlahKomponen: Int,
    val persentase: Int,
    val status: StatusHarian
)
