package com.maternatrack.app.logic

import com.maternatrack.app.model.CatatanMakan
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.WaktuMakan
import java.time.LocalDate

const val PORSI_MIN = 1
const val PORSI_MAKS = 5
const val CERITA_MAKS = 100

/** Catatan boleh untuk hari ini dan 6 hari sebelumnya (7 hari terakhir, sama dengan strip tanggal). */
const val HARI_MUNDUR_MAKS = 6L

/**
 * Isi formulir apa adanya. Porsi disimpan sebagai teks agar ketikan seperti "a" atau "-1"
 * tidak membuat aplikasi crash; baru diubah ke Int setelah lolos validasi.
 */
data class FormInput(
    val tanggal: LocalDate,
    val waktu: WaktuMakan?,
    val porsiTeks: Map<KelompokMakanan, String>,
    val minumSuplemen: Boolean,
    val cerita: String
)

enum class FieldForm { TANGGAL, WAKTU, ISI_PIRING }

data class HasilValidasi(
    val errorField: Map<FieldForm, String>,
    val errorPorsi: Map<KelompokMakanan, String>
) {
    val valid: Boolean get() = errorField.isEmpty() && errorPorsi.isEmpty()
}

/** Mengembalikan pesan kesalahan porsi, atau null jika porsi valid. */
fun validasiPorsi(teks: String): String? {
    val bersih = teks.trim()
    val angka = bersih.toIntOrNull()
    return when {
        bersih.isEmpty() -> "Porsi wajib diisi"
        angka == null -> "Porsi harus berupa angka"
        angka < 0 -> "Porsi tidak boleh negatif"
        angka < PORSI_MIN || angka > PORSI_MAKS -> "Porsi $PORSI_MIN sampai $PORSI_MAKS"
        else -> null
    }
}

fun validasiCatatan(input: FormInput, semua: List<CatatanMakan>, hariIni: LocalDate): HasilValidasi {
    val error = mutableMapOf<FieldForm, String>()

    if (input.tanggal.isAfter(hariIni)) {
        error[FieldForm.TANGGAL] = "Tanggal tidak boleh di masa depan"
    } else if (input.tanggal.isBefore(hariIni.minusDays(HARI_MUNDUR_MAKS))) {
        error[FieldForm.TANGGAL] = "Pilih tanggal dalam ${HARI_MUNDUR_MAKS + 1} hari terakhir"
    }

    val waktu = input.waktu
    if (waktu == null) {
        error[FieldForm.WAKTU] = "Pilih waktu makan"
    } else if (!waktu.bolehBerulang &&
        semua.any { it.tanggal == input.tanggal && it.waktu == waktu }
    ) {
        error[FieldForm.WAKTU] = "${waktu.label} tanggal ini sudah dicatat"
    }

    if (input.porsiTeks.isEmpty() && !input.minumSuplemen) {
        error[FieldForm.ISI_PIRING] = "Pilih minimal satu isi piring"
    }

    val errorPorsi = mutableMapOf<KelompokMakanan, String>()
    for ((kelompok, teks) in input.porsiTeks) {
        val pesan = validasiPorsi(teks)
        if (pesan != null) errorPorsi[kelompok] = pesan
    }

    return HasilValidasi(error, errorPorsi)
}

/** Membuat object CatatanMakan dari form. Dipanggil hanya setelah validasi lolos. */
fun FormInput.keCatatan(id: Long): CatatanMakan = CatatanMakan(
    id = id,
    tanggal = tanggal,
    waktu = requireNotNull(waktu),
    porsi = porsiTeks.mapValues { it.value.trim().toInt() },
    minumSuplemen = minumSuplemen,
    cerita = cerita.trim()
)
