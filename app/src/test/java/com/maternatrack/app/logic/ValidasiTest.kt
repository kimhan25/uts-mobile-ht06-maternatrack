package com.maternatrack.app.logic

import com.maternatrack.app.model.CatatanMakan
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.KelompokMakanan.POKOK
import com.maternatrack.app.model.KelompokMakanan.SAYUR
import com.maternatrack.app.model.WaktuMakan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ValidasiTest {

    private val hariIni = LocalDate.parse("2026-10-27")

    private fun form(
        tanggal: LocalDate = hariIni,
        waktu: WaktuMakan? = WaktuMakan.SARAPAN,
        porsi: Map<KelompokMakanan, String> = mapOf(POKOK to "1"),
        suplemen: Boolean = false
    ) = FormInput(tanggal, waktu, porsi, suplemen, "")

    private val sarapan27 = CatatanMakan(1, hariIni, WaktuMakan.SARAPAN, mapOf(POKOK to 1), false, "")

    @Test
    fun porsiTidakValid() {
        assertEquals("Porsi wajib diisi", validasiPorsi(""))
        assertEquals("Porsi harus berupa angka", validasiPorsi("a"))
        assertEquals("Porsi harus berupa angka", validasiPorsi("2a"))
        assertEquals("Porsi tidak boleh negatif", validasiPorsi("-1"))
        assertEquals("Porsi 1 sampai 5", validasiPorsi("0"))
        assertEquals("Porsi 1 sampai 5", validasiPorsi("9"))
    }

    @Test
    fun porsiValid() {
        assertNull(validasiPorsi("3"))
        assertNull(validasiPorsi(" 2 "))
    }

    @Test
    fun tanggalMasaDepanDitolak() {
        val hasil = validasiCatatan(form(tanggal = hariIni.plusDays(1)), emptyList(), hariIni)
        assertEquals("Tanggal tidak boleh di masa depan", hasil.errorField[FieldForm.TANGGAL])
        assertTrue(validasiCatatan(form(), emptyList(), hariIni).valid)
    }

    @Test
    fun tanggalLebihDariTujuhHariDitolak() {
        val hasil = validasiCatatan(form(tanggal = hariIni.minusDays(7)), emptyList(), hariIni)
        assertEquals("Pilih tanggal dalam 7 hari terakhir", hasil.errorField[FieldForm.TANGGAL])
        // 6 hari lalu masih termasuk 7 hari terakhir
        assertTrue(validasiCatatan(form(tanggal = hariIni.minusDays(6)), emptyList(), hariIni).valid)
    }

    @Test
    fun waktuMakanWajibDipilih() {
        val hasil = validasiCatatan(form(waktu = null), emptyList(), hariIni)
        assertEquals("Pilih waktu makan", hasil.errorField[FieldForm.WAKTU])
    }

    @Test
    fun sarapanTidakBolehDobelDiTanggalSama() {
        val hasil = validasiCatatan(form(), listOf(sarapan27), hariIni)
        assertEquals("Sarapan tanggal ini sudah dicatat", hasil.errorField[FieldForm.WAKTU])
        // tanggal lain boleh
        assertTrue(validasiCatatan(form(tanggal = hariIni.minusDays(1)), listOf(sarapan27), hariIni).valid)
    }

    @Test
    fun camilanBolehBerulang() {
        val camilan = sarapan27.copy(waktu = WaktuMakan.CAMILAN)
        assertTrue(validasiCatatan(form(waktu = WaktuMakan.CAMILAN), listOf(camilan), hariIni).valid)
    }

    @Test
    fun isiPiringMinimalSatu() {
        val kosong = validasiCatatan(form(porsi = emptyMap()), emptyList(), hariIni)
        assertEquals("Pilih minimal satu isi piring", kosong.errorField[FieldForm.ISI_PIRING])
        // hanya suplemen tetap boleh
        assertTrue(validasiCatatan(form(porsi = emptyMap(), suplemen = true), emptyList(), hariIni).valid)
    }

    @Test
    fun porsiHurufMembuatFormTidakValid() {
        val hasil = validasiCatatan(form(porsi = mapOf(SAYUR to "a")), emptyList(), hariIni)
        assertEquals("Porsi harus berupa angka", hasil.errorPorsi[SAYUR])
        assertFalse(hasil.valid)
    }

    @Test
    fun formValidMenjadiCatatan() {
        val catatan = form(porsi = mapOf(POKOK to " 2 ")).copy(cerita = "  mual berkurang ").keCatatan(7)
        assertEquals(7L, catatan.id)
        assertEquals(mapOf(POKOK to 2), catatan.porsi)
        assertEquals("mual berkurang", catatan.cerita)
    }
}
