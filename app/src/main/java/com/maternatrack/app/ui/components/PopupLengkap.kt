package com.maternatrack.app.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maternatrack.app.R
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.isi
import com.maternatrack.app.ui.theme.judul
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Pop-up perayaan saat isi piring hari ini menjadi lengkap. Tampil di tengah layar di mana pun
 * posisi gulir Bunda, dengan confetti yang meledak dari tengah. Dipanggil di dalam AnimatedVisibility.
 */
@Composable
fun AnimatedVisibilityScope.PopupHariLengkap(adaArahanSuplemen: Boolean, onTutup: () -> Unit) {
    BackHandler(onBack = onTutup)
    val tanpaRiak = remember { MutableInteractionSource() }
    Box(
        Modifier
            .fillMaxSize()
            .background(MtColor.Ink.copy(alpha = 0.6f))
            .clickable(interactionSource = tanpaRiak, indication = null, onClick = onTutup),
        contentAlignment = Alignment.Center
    ) {
        // Confetti hanya diputar saat pop-up masuk, bukan saat keluar
        if (transition.targetState == EnterExitState.Visible) LedakanConfetti(Modifier.fillMaxSize())
        KartuLengkap(
            adaArahanSuplemen = adaArahanSuplemen,
            onTutup = onTutup,
            modifier = Modifier
                .padding(horizontal = 28.dp)
                .animateEnterExit(
                    enter = scaleIn(spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow), initialScale = 0.7f) +
                        fadeIn(),
                    exit = scaleOut(targetScale = 0.9f) + fadeOut()
                )
                // Ketukan di dalam kartu tidak ikut menutup pop-up
                .clickable(interactionSource = tanpaRiak, indication = null, onClick = {})
        )
    }
}

@Composable
private fun KartuLengkap(adaArahanSuplemen: Boolean, onTutup: () -> Unit, modifier: Modifier = Modifier) {
    val jumlah = if (adaArahanSuplemen) 6 else 5
    Column(
        modifier
            .widthIn(max = 360.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(MtColor.Surface)
            .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeroMaskot()
        Column(
            Modifier.padding(start = 22.dp, end = 22.dp, top = 20.dp, bottom = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .background(MtColor.LimeMid, CircleShape)
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.CheckCircle, null, tint = MtColor.LimeDeep, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("$jumlah dari $jumlah terpenuhi", style = isi(13, 800), color = MtColor.LimeDeep)
            }
            Spacer(Modifier.height(12.dp))
            Text("Hebat, Bunda!", style = judul(30), color = MtColor.Ink)
            Spacer(Modifier.height(6.dp))
            Text(
                if (adaArahanSuplemen) {
                    "Isi piring hari ini lengkap. Lima kelompok gizi dan suplemen sudah terpenuhi."
                } else {
                    "Isi piring hari ini lengkap. Kelima kelompok gizi sudah terpenuhi."
                },
                style = isi(15, 600),
                color = MtColor.InkMuted,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(18.dp))
            BarisKelompok(adaArahanSuplemen)
            Spacer(Modifier.height(20.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MtColor.Lime)
                    .clickable(onClick = onTutup),
                contentAlignment = Alignment.Center
            ) {
                Text("Hore!", style = judul(17), color = MtColor.Ink)
            }
        }
    }
}

/** Latar kuning seperti kartu sapaan, maskot melompat masuk, dan kilau yang berkelip. */
@Composable
private fun HeroMaskot() {
    val lompat = remember { Animatable(70f) }
    LaunchedEffect(Unit) {
        lompat.animateTo(0f, spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow))
    }
    val kelip = rememberInfiniteTransition(label = "kelip")
    Box(
        Modifier
            .fillMaxWidth()
            .height(176.dp)
            .background(MtColor.Header)
    ) {
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-6).dp)
                .size(220.dp)
                .background(MtColor.LemonMid, CircleShape)
        )
        Image(
            painterResource(R.drawable.art_maskot),
            contentDescription = "Maskot Bunda bersorak",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 2.dp)
                .size(200.dp)
                .graphicsLayer { translationY = lompat.value.dp.toPx() }
        )
        val posisi = listOf(
            Triple(Alignment.TopStart, Offset(40f, 40f), 26.dp),
            Triple(Alignment.TopEnd, Offset(-44f, 30f), 20.dp),
            Triple(Alignment.TopEnd, Offset(-26f, 110f), 28.dp),
            Triple(Alignment.TopStart, Offset(30f, 120f), 18.dp)
        )
        posisi.forEachIndexed { i, (rataan, geser, ukuran) ->
            val alfa = kelip.animateFloat(
                initialValue = 0.25f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse, StartOffset(i * 350)),
                label = "kilau$i"
            )
            Kilau(
                ukuran = ukuran,
                warna = if (i % 2 == 0) MtColor.Surface else MtColor.LemonMid,
                modifier = Modifier
                    .align(rataan)
                    .offset(x = geser.x.dp, y = geser.y.dp)
                    .graphicsLayer { alpha = alfa.value }
            )
        }
    }
}

@Composable
private fun Kilau(ukuran: Dp, warna: Color, modifier: Modifier) {
    Icon(Icons.Rounded.AutoAwesome, null, tint = warna, modifier = modifier.size(ukuran))
}

/** Kelompok yang terpenuhi muncul satu per satu, masing-masing dengan centang. */
@Composable
private fun BarisKelompok(adaArahanSuplemen: Boolean) {
    val daftar = KelompokMakanan.entries.map { Triple(it.gambar, it.warna.mid, it.labelPendek) } +
        listOfNotNull(Triple(Suplemen.GAMBAR, Suplemen.warna.mid, Suplemen.LABEL).takeIf { adaArahanSuplemen })
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        daftar.forEachIndexed { i, (gambar, latar, label) ->
            val skala = remember { Animatable(0f) }
            LaunchedEffect(Unit) {
                delay(250L + i * 90L)
                skala.animateTo(1f, spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMedium))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer {
                    scaleX = skala.value
                    scaleY = skala.value
                }
            ) {
                Box {
                    Bubble(gambar, latar, ukuran = 40.dp)
                    CentangHijau(Modifier.align(Alignment.TopEnd).offset(x = 5.dp, y = (-5).dp), ukuran = 16.dp)
                }
                Spacer(Modifier.height(4.dp))
                Text(label, style = isi(11, 800), color = MtColor.Ink)
            }
        }
    }
}

private class PartikelLedak(val sudut: Float, val kecepatan: Float, val putar: Float, val warna: Int)

private val warnaConfetti = listOf(
    MtColor.Primary, MtColor.Lemon, MtColor.Lime, MtColor.Sky, MtColor.Grape, MtColor.Peach
)

/** Confetti meledak dari tengah layar lalu jatuh perlahan, berjalan sekali selama 2,8 detik. */
@Composable
private fun LedakanConfetti(modifier: Modifier) {
    val progres = remember { Animatable(0f) }
    val partikel = remember {
        List(130) {
            PartikelLedak(
                sudut = Random.nextFloat() * 2f * PI.toFloat(),
                kecepatan = 0.35f + Random.nextFloat() * 0.65f,
                putar = Random.nextFloat() * 360f,
                warna = Random.nextInt(warnaConfetti.size)
            )
        }
    }
    LaunchedEffect(Unit) { progres.animateTo(1f, tween(2800, easing = LinearEasing)) }

    // Nilai progres hanya dibaca di fase gambar agar tidak memicu recompose per frame
    Canvas(modifier) {
        val t = progres.value
        if (t >= 1f) return@Canvas
        val pusat = Offset(size.width / 2f, size.height * 0.4f)
        val jangkau = size.minDimension * 0.85f
        val keluar = 1f - (1f - t) * (1f - t) * (1f - t)
        val lebar = 7.dp.toPx()
        val tinggi = 12.dp.toPx()
        for (q in partikel) {
            val x = pusat.x + cos(q.sudut) * jangkau * q.kecepatan * keluar
            val y = pusat.y + sin(q.sudut) * jangkau * q.kecepatan * keluar + size.height * 0.3f * t * t
            rotate(q.putar + t * 720f, pivot = Offset(x, y)) {
                drawRect(
                    color = warnaConfetti[q.warna],
                    topLeft = Offset(x - lebar / 2f, y - tinggi / 2f),
                    size = Size(lebar, tinggi),
                    alpha = 1f - t * t * t
                )
            }
        }
    }
}
