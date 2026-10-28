package com.maternatrack.app.ui.components

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.maternatrack.app.logic.formatPanjang
import com.maternatrack.app.model.CatatanMakan
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.MtRadius
import com.maternatrack.app.ui.theme.isi
import com.maternatrack.app.ui.theme.judul
import java.time.LocalDate
import kotlin.math.roundToInt

/** Data toast setelah simpan: catatan yang baru disimpan dan persentase tanggalnya sesudah disimpan. */
data class InfoSimpan(val catatan: CatatanMakan, val persen: Int)

/**
 * Kartu yang turun dari atas layar setelah catatan disimpan: waktu makan, tanggalnya, dan
 * persentase isi piring di tanggal itu. Bisa diusap ke atas untuk menutup lebih cepat.
 */
@Composable
fun ToastSimpan(info: InfoSimpan, hariIni: LocalDate, onTutup: () -> Unit, modifier: Modifier = Modifier) {
    val catatan = info.catatan
    var geser by remember { mutableFloatStateOf(0f) }
    val batasTutup = with(LocalDensity.current) { 32.dp.toPx() }

    val keterangan = if (catatan.tanggal == hariIni) "Untuk hari ini" else "Untuk ${formatPanjang(catatan.tanggal)}"

    Column(
        modifier
            .fillMaxWidth()
            .offset { IntOffset(0, geser.roundToInt()) }
            .draggable(
                orientation = Orientation.Vertical,
                // Hanya bisa ditarik ke atas; tarikan ke bawah diredam
                state = rememberDraggableState { d -> geser = (geser + if (d > 0) d * 0.2f else d).coerceAtMost(12f) },
                onDragStopped = {
                    if (geser < -batasTutup) onTutup()
                    else animate(geser, 0f, animationSpec = spring()) { v, _ -> geser = v }
                }
            )
            .semantics { liveRegion = LiveRegionMode.Polite }
            .background(MtColor.Surface, RoundedCornerShape(MtRadius.Lg))
            .border(2.dp, MtColor.LimeMid, RoundedCornerShape(MtRadius.Lg))
            .padding(start = 12.dp, end = 14.dp, top = 12.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(52.dp).background(catatan.waktu.warna.mid, RoundedCornerShape(MtRadius.Md)),
                contentAlignment = Alignment.Center
            ) {
                Image(painterResource(catatan.waktu.gambar), null, Modifier.size(40.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${catatan.waktu.namaLengkap} tercatat", style = judul(16), color = MtColor.Ink)
                    Spacer(Modifier.width(6.dp))
                    CentangHijau(ukuran = 20.dp)
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    keterangan,
                    style = isi(13, 700),
                    color = MtColor.InkMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(10.dp))
            CincinProgres(info.persen, ukuran = 46.dp, tebal = 6.dp, gayaTeks = judul(12))
        }
        Spacer(Modifier.height(8.dp))
        // Pegangan kecil sebagai petunjuk bahwa kartu bisa diusap ke atas
        Box(Modifier.size(width = 36.dp, height = 4.dp).background(MtColor.Line, CircleShape))
    }
}
