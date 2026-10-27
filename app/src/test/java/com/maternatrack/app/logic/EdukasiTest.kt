package com.maternatrack.app.logic

import com.maternatrack.app.model.CatatanMakan
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.KelompokMakanan.BUAH
import com.maternatrack.app.model.KelompokMakanan.HEWANI
import com.maternatrack.app.model.KelompokMakanan.NABATI
import com.maternatrack.app.model.KelompokMakanan.POKOK
import com.maternatrack.app.model.KelompokMakanan.SAYUR
import com.maternatrack.app.model.WaktuMakan
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class EdukasiTest {

    private val tgl = LocalDate.parse("2026-10-27")

    private fun hari(vararg kelompok: KelompokMakanan) = ringkasanHari(
        tgl,
        listOf(CatatanMakan(1, tgl, WaktuMakan.SIANG, kelompok.associateWith { 1 }, false, "")),
        adaArahanSuplemen = true
    )

    @Test
    fun belumAdaCatatanMembahasSayur() {
        assertTrue(pesanEdukasi(null).startsWith("Sayur"))
    }

    @Test
    fun belumAdaBuah() {
        assertTrue(pesanEdukasi(hari(POKOK, HEWANI, SAYUR)).startsWith("Buah"))
    }

    @Test
    fun belumAdaProtein() {
        assertTrue(pesanEdukasi(hari(SAYUR, BUAH, POKOK)).startsWith("Lauk hewani dan nabati"))
    }

    @Test
    fun satuSumberProteinSudahCukupUntukPesanProtein() {
        assertTrue(pesanEdukasi(hari(SAYUR, BUAH, NABATI)).startsWith("Makanan pokok"))
    }

    @Test
    fun semuaKelompokTerpenuhi() {
        assertTrue(pesanEdukasi(hari(*KelompokMakanan.entries.toTypedArray())).startsWith("Isi piring Bunda sudah beragam"))
    }
}
