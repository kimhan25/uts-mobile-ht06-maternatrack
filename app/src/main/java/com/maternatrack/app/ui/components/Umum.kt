package com.maternatrack.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.isi
import com.maternatrack.app.ui.theme.judul
import com.maternatrack.app.ui.theme.warnaProgres
import com.maternatrack.app.ui.theme.warnaTrack

/** Cincin persentase; busur dan warnanya beranimasi saat persentase berubah. */
@Composable
fun CincinProgres(
    persen: Int,
    modifier: Modifier = Modifier,
    ukuran: Dp = 52.dp,
    tebal: Dp = 7.dp,
    gayaTeks: TextStyle = judul(13)
) {
    val sapuan by animateFloatAsState(persen / 100f, tween(600), label = "sapuan")
    val warna by animateColorAsState(warnaProgres(persen), tween(600), label = "warna")
    val track by animateColorAsState(warnaTrack(persen), tween(600), label = "track")
    Box(modifier.size(ukuran), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(ukuran)) {
            val t = tebal.toPx()
            val kotak = Size(size.width - t, size.height - t)
            val awal = Offset(t / 2, t / 2)
            drawArc(track, 0f, 360f, false, awal, kotak, style = Stroke(t))
            drawArc(warna, -90f, 360f * sapuan, false, awal, kotak, style = Stroke(t, cap = StrokeCap.Butt))
        }
        Text("$persen%", style = gayaTeks, color = MtColor.Ink)
    }
}

/** Lingkaran berwarna berisi ilustrasi kecil (thumbnail kelompok/suplemen). */
@Composable
fun Bubble(
    gambar: Int,
    latar: Color,
    modifier: Modifier = Modifier,
    ukuran: Dp = 30.dp,
    ukuranGambar: Dp = ukuran * 0.62f,
    redup: Boolean = false
) {
    Box(
        modifier.size(ukuran).clip(CircleShape).background(latar),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(gambar),
            contentDescription = null,
            modifier = Modifier.size(ukuranGambar).alpha(if (redup) 0.45f else 1f)
        )
    }
}

/** Badge angka porsi (muncul jika porsi lebih dari 1). */
@Composable
fun BadgePorsi(porsi: Int, modifier: Modifier = Modifier) {
    Box(
        modifier.size(18.dp).clip(CircleShape).background(MtColor.Primary),
        contentAlignment = Alignment.Center
    ) {
        Text("$porsi", style = judul(11), color = MtColor.Surface)
    }
}

/**
 * Centang hijau: lingkaran Lime datar dengan centang putih yang "tergambar".
 * Path dari komponen "Centang hijau" di wireframe (viewBox 32).
 */
@Composable
fun CentangHijau(modifier: Modifier = Modifier, ukuran: Dp = 32.dp, animasi: Boolean = true) {
    val skala = remember { Animatable(if (animasi) 0f else 1f) }
    val goresan = remember { Animatable(if (animasi) 0f else 1f) }
    LaunchedEffect(Unit) {
        if (animasi) {
            skala.animateTo(1f, spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMedium))
            goresan.animateTo(1f, tween(300))
        }
    }
    Canvas(modifier.size(ukuran).graphicsLayer {
        scaleX = skala.value
        scaleY = skala.value
    }) {
        val k = size.width / 32f
        drawCircle(MtColor.Lime, radius = 14f * k)
        val centang = Path().apply {
            moveTo(10f * k, 16.5f * k)
            lineTo(14f * k, 20.5f * k)
            lineTo(22f * k, 12f * k)
        }
        val ukur = PathMeasure().apply { setPath(centang, false) }
        val sebagian = Path()
        ukur.getSegment(0f, ukur.length * goresan.value, sebagian, true)
        drawPath(sebagian, Color.White, style = Stroke(3.4f * k, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

/**
 * Pesan kesalahan di bawah field: ikon + teks merah, bergetar setiap [pemicu] berubah.
 * Tidak menampilkan apa pun jika [pesan] null.
 */
@Composable
fun FieldError(pesan: String?, pemicu: Int, modifier: Modifier = Modifier) {
    if (pesan == null) return
    val geser = remember { Animatable(0f) }
    LaunchedEffect(pemicu, pesan) {
        for (x in listOf(10f, -8f, 6f, -4f, 0f)) geser.animateTo(x, tween(45))
    }
    Row(
        modifier.offset { IntOffset(geser.value.dp.roundToPx(), 0) }.padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Rounded.ErrorOutline, null, tint = MtColor.Error, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(pesan, style = isi(12, 800), color = MtColor.Error)
    }
}
