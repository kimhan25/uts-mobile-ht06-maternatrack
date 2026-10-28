package com.maternatrack.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maternatrack.app.model.CatatanMakan
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.judul
import kotlin.math.sin
import kotlin.random.Random

/** Kartu kecil di atas navbar setelah catatan berhasil disimpan. */
@Composable
fun ToastSimpan(catatan: CatatanMakan, modifier: Modifier = Modifier) {
    val warna = catatan.waktu.warna
    Row(
        modifier
            .fillMaxWidth()
            .background(MtColor.Surface, RoundedCornerShape(22.dp))
            .border(2.dp, MtColor.LimeMid, RoundedCornerShape(22.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(56.dp).background(warna.mid, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(painterResource(catatan.waktu.gambar), null, Modifier.size(40.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${catatan.waktu.label} tersimpan", style = judul(16), color = MtColor.Ink)
                Spacer(Modifier.width(6.dp))
                CentangHijau(ukuran = 22.dp)
            }
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (k in KelompokMakanan.entries.filter { it in catatan.porsi }) {
                    ThumbPorsi(k.gambar, k.warna.mid, catatan.porsi.getValue(k), ukuran = 28)
                }
                if (catatan.minumSuplemen) ThumbPorsi(Suplemen.GAMBAR, Suplemen.warna.mid, 1, ukuran = 28)
            }
        }
    }
}

private class Partikel(val x: Float, val kecepatan: Float, val goyang: Float, val putar: Float, val warna: Int)

private val warnaConfetti = listOf(
    MtColor.Primary, MtColor.Lemon, MtColor.Lime, MtColor.Sky, MtColor.Grape, MtColor.Peach
)

/**
 * Confetti sederhana di Canvas, berjalan 1,5 detik setiap [pemicu] bertambah.
 * Pemicu 0 berarti belum pernah dijalankan.
 */
@Composable
fun Confetti(pemicu: Int, modifier: Modifier = Modifier) {
    if (pemicu == 0) return
    val progres = remember(pemicu) { Animatable(0f) }
    val partikel = remember(pemicu) {
        List(70) {
            Partikel(
                x = Random.nextFloat(),
                kecepatan = 0.6f + Random.nextFloat() * 0.6f,
                goyang = Random.nextFloat() * 6.28f,
                putar = Random.nextFloat() * 360f,
                warna = Random.nextInt(warnaConfetti.size)
            )
        }
    }
    LaunchedEffect(pemicu) { progres.animateTo(1f, tween(1500, easing = LinearEasing)) }
    if (progres.value >= 1f) return

    Canvas(modifier.fillMaxSize()) {
        val p = progres.value
        for (q in partikel) {
            val y = -20f + p * size.height * q.kecepatan
            val x = q.x * size.width + sin(q.goyang + p * 10f) * 18f
            rotate(q.putar + p * 540f, pivot = Offset(x, y)) {
                drawRect(
                    color = warnaConfetti[q.warna],
                    topLeft = Offset(x - 5f, y - 9f),
                    size = Size(10f, 18f),
                    alpha = 1f - p * p
                )
            }
        }
    }
}
