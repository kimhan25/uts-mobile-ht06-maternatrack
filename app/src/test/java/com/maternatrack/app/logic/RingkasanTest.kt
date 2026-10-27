package com.maternatrack.app.logic

import com.maternatrack.app.model.CatatanMakan
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.KelompokMakanan.BUAH
import com.maternatrack.app.model.KelompokMakanan.HEWANI
import com.maternatrack.app.model.KelompokMakanan.NABATI
import com.maternatrack.app.model.KelompokMakanan.POKOK
import com.maternatrack.app.model.KelompokMakanan.SAYUR
import com.maternatrack.app.model.StatusHarian
import com.maternatrack.app.model.WaktuMakan
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class RingkasanTest {

    private fun catat(
        tgl: String,
        waktu: WaktuMakan,
        vararg kelompok: KelompokMakanan,
        suplemen: Boolean = false,
        porsi: Int = 1
    ) = CatatanMakan(
        id = 0,
        tanggal = LocalDate.parse(tgl),
        waktu = waktu,
        porsi = kelompok.associateWith { porsi },
        minumSuplemen = suplemen,
        cerita = ""
    )

    private val tgl27 = LocalDate.parse("2026-10-27")

    @Test
    fun statusDariPersentase() {
        assertEquals(StatusHarian.MULAI, statusDari(0))
        assertEquals(StatusHarian.KURANG, statusDari(1))
        assertEquals(StatusHarian.KURANG, statusDari(59))
        assertEquals(StatusHarian.HAMPIR, statusDari(60))
        assertEquals(StatusHarian.HAMPIR, statusDari(99))
        assertEquals(StatusHarian.LENGKAP, statusDari(100))
    }

    @Test
    fun empatDariEnamKomponenMenjadi67Persen() {
        val data = listOf(catat("2026-10-27", WaktuMakan.SARAPAN, POKOK, HEWANI, BUAH, suplemen = true))
        val hasil = ringkasanHari(tgl27, data, adaArahanSuplemen = true)
        assertEquals(4, hasil.jumlahTerpenuhi)
        assertEquals(6, hasil.jumlahKomponen)
        assertEquals(67, hasil.persentase)
        assertEquals(StatusHarian.HAMPIR, hasil.status)
    }

    @Test
    fun kelompokDigabungTanpaDuplikat() {
        val data = listOf(
            catat("2026-10-27", WaktuMakan.SARAPAN, POKOK),
            catat("2026-10-27", WaktuMakan.SIANG, SAYUR, POKOK)
        )
        val hasil = ringkasanHari(tgl27, data, adaArahanSuplemen = true)
        assertEquals(setOf(POKOK, SAYUR), hasil.kelompokTerpenuhi)
        assertEquals(33, hasil.persentase)
    }

    @Test
    fun semuaKomponenMenjadi100Persen() {
        val data = listOf(catat("2026-10-27", WaktuMakan.SIANG, *KelompokMakanan.entries.toTypedArray(), suplemen = true))
        val hasil = ringkasanHari(tgl27, data, adaArahanSuplemen = true)
        assertEquals(100, hasil.persentase)
        assertEquals(StatusHarian.LENGKAP, hasil.status)
    }

    @Test
    fun tanpaArahanSuplemenPenyebutnyaLima() {
        val data = listOf(catat("2026-10-27", WaktuMakan.SIANG, *KelompokMakanan.entries.toTypedArray()))
        val hasil = ringkasanHari(tgl27, data, adaArahanSuplemen = false)
        assertEquals(5, hasil.jumlahKomponen)
        assertEquals(100, hasil.persentase)
    }

    @Test
    fun suplemenTidakDihitungJikaTidakAdaArahan() {
        val data = listOf(catat("2026-10-27", WaktuMakan.SARAPAN, POKOK, suplemen = true))
        val hasil = ringkasanHari(tgl27, data, adaArahanSuplemen = false)
        assertEquals(false, hasil.suplemenTerpenuhi)
        assertEquals(20, hasil.persentase)
    }

    @Test
    fun hariTanpaCatatanBernilaiNol() {
        val hasil = ringkasanHari(tgl27, emptyList(), adaArahanSuplemen = true)
        assertEquals(0, hasil.persentase)
        assertEquals(StatusHarian.MULAI, hasil.status)
    }

    @Test
    fun ringkasHarianUrutTanggalTerbaru() {
        val data = listOf(
            catat("2026-10-25", WaktuMakan.SARAPAN, POKOK),
            catat("2026-10-27", WaktuMakan.SARAPAN, POKOK),
            catat("2026-10-26", WaktuMakan.SARAPAN, POKOK),
            catat("2026-10-27", WaktuMakan.SIANG, SAYUR)
        )
        val hasil = ringkasHarian(data, adaArahanSuplemen = true)
        assertEquals(listOf("2026-10-27", "2026-10-26", "2026-10-25"), hasil.map { it.tanggal.toString() })
        assertEquals(2, hasil.first().catatan.size)
    }

    @Test
    fun totalPorsiDijumlahkanPerKelompok() {
        val data = listOf(
            catat("2026-10-27", WaktuMakan.SARAPAN, POKOK, SAYUR, porsi = 2),
            catat("2026-10-27", WaktuMakan.MALAM, POKOK, NABATI, porsi = 1)
        )
        val total = totalPorsi(ringkasanHari(tgl27, data, adaArahanSuplemen = true))
        assertEquals(mapOf(POKOK to 3, SAYUR to 2, NABATI to 1), total)
    }

    @Test
    fun waktuBerikutnyaMelewatiYangSudahDicatat() {
        assertEquals(WaktuMakan.SARAPAN, waktuBerikutnya(tgl27, emptyList()))
        val data = listOf(catat("2026-10-27", WaktuMakan.SARAPAN, POKOK))
        assertEquals(WaktuMakan.SIANG, waktuBerikutnya(tgl27, data))
        val penuh = data + catat("2026-10-27", WaktuMakan.SIANG, POKOK) + catat("2026-10-27", WaktuMakan.MALAM, POKOK)
        assertEquals(WaktuMakan.CAMILAN, waktuBerikutnya(tgl27, penuh))
    }
}
