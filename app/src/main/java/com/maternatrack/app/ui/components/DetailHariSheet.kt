package com.maternatrack.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maternatrack.app.R
import com.maternatrack.app.logic.formatPanjang
import com.maternatrack.app.logic.pesanEdukasi
import com.maternatrack.app.logic.totalPorsi
import com.maternatrack.app.model.CatatanMakan
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.RingkasanHarian
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.MtRadius
import com.maternatrack.app.ui.theme.isi
import com.maternatrack.app.ui.theme.judul
import com.maternatrack.app.ui.theme.warnaTeksStatus
import com.maternatrack.app.ui.theme.warnaTrack

/** Lembar bawah berisi rincian satu hari: total porsi per kelompok dan catatan per waktu makan. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailHariSheet(ringkasan: RingkasanHarian, adaArahanSuplemen: Boolean, onTutup: () -> Unit) {
    val total = totalPorsi(ringkasan)
    ModalBottomSheet(
        onDismissRequest = onTutup,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MtColor.BgSheet,
        dragHandle = { PegangSheet() }
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CincinProgres(ringkasan.persentase, ukuran = 68.dp, tebal = 9.dp, gayaTeks = judul(17))
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(formatPanjang(ringkasan.tanggal), style = judul(20), color = MtColor.Ink)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        ringkasan.status.label,
                        style = isi(12, 800),
                        color = warnaTeksStatus(ringkasan.persentase),
                        modifier = Modifier
                            .background(warnaTrack(ringkasan.persentase), RoundedCornerShape(MtRadius.Xs))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Isi piring hari itu: total porsi per kelompok
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MtColor.Surface, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Isi piring hari itu", style = judul(16), color = MtColor.Ink, modifier = Modifier.weight(1f))
                    Text(
                        "${ringkasan.jumlahTerpenuhi} dari ${ringkasan.jumlahKomponen}",
                        style = isi(13, 800),
                        color = warnaTeksStatus(ringkasan.persentase)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (k in KelompokMakanan.entries) {
                        val porsi = total[k]
                        ItemTotal(k.gambar, k.labelPendek, k.warna.mid, if (porsi == null) "Belum" else "$porsi porsi", porsi != null)
                    }
                    if (adaArahanSuplemen) {
                        val sudah = ringkasan.suplemenTerpenuhi
                        ItemTotal(Suplemen.GAMBAR, Suplemen.LABEL, Suplemen.warna.mid, if (sudah) "Sudah" else "Belum", sudah)
                    }
                }
            }

            Text("Per waktu makan", style = judul(16), color = MtColor.Ink)
            for (c in ringkasan.catatan) KartuWaktuDetail(c)

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MtColor.LemonMid, RoundedCornerShape(20.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painterResource(R.drawable.art_bintang), null, Modifier.size(56.dp))
                Spacer(Modifier.width(12.dp))
                Text(pesanEdukasi(ringkasan), style = isi(14, 700), color = MtColor.Ink)
            }

            TombolUtama("Tutup", onTutup)
        }
    }
}

@Composable
private fun ItemTotal(gambar: Int, label: String, warnaMid: Color, keterangan: String, menyala: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Bubble(gambar, if (menyala) warnaMid else MtColor.DayEmpty, ukuran = 44.dp, ukuranGambar = 28.dp, redup = !menyala)
        Spacer(Modifier.height(6.dp))
        Text(label, style = isi(11, 800), color = if (menyala) MtColor.Ink else MtColor.InkMuted)
        Text(keterangan, style = isi(11, 700), color = MtColor.InkMuted, textAlign = TextAlign.Center)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun KartuWaktuDetail(c: CatatanMakan) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MtColor.Surface, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(c.waktu.gambar), null, Modifier.size(30.dp))
            Spacer(Modifier.width(10.dp))
            Text(c.waktu.label, style = judul(17), color = MtColor.Ink)
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for (k in KelompokMakanan.entries.filter { it in c.porsi }) {
                ChipIsi(k.gambar, "${k.labelPendek} ${c.porsi.getValue(k)}", k.warna.mid)
            }
            if (c.minumSuplemen) ChipIsi(Suplemen.GAMBAR, Suplemen.LABEL, Suplemen.warna.mid)
        }
        if (c.cerita.isNotBlank()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MtColor.Bg, RoundedCornerShape(MtRadius.Sm))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.ChatBubbleOutline, null, tint = MtColor.Primary, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(10.dp))
                Text(
                    "“${c.cerita}”",
                    style = isi(13, 700).copy(fontStyle = FontStyle.Italic),
                    color = MtColor.Ink
                )
            }
        }
    }
}

@Composable
private fun ChipIsi(gambar: Int, label: String, warnaMid: Color) {
    Row(
        Modifier
            .background(warnaMid, CircleShape)
            .padding(start = 4.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Bubble(gambar, MtColor.Surface, ukuran = 24.dp)
        Spacer(Modifier.width(6.dp))
        Text(label, style = isi(13, 800), color = MtColor.Ink)
    }
}
