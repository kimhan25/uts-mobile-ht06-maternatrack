# Presentasi MaternaTrack

Durasi disesuaikan dengan ketentuan dosen. Kedua anggota menyiapkan semua bagian karena defense bersifat individual.

## 1. Pembuka (1 menit)

- Studi kasus 6, HT-06: MaternaTrack, edukasi gizi ibu hamil.
- Masalah: sulit memantau keragaman isi piring harian selama kehamilan.
- Pengguna: ibu hamil, kader kesehatan, peserta edukasi maternal.
- Batas etika: data simulasi, bukan alat diagnosis, tidak menentukan dosis.

## 2. Desain (1 menit)

- Wireframe satu layar: `docs/wireframe.png`.
- Alur: Header → Piring hari ini → Catat Makan → Riwayat Makan → Edukasi Gizi, ditambah floating navbar.

## 3. Demo (4 menit)

Urutan demo mengikuti skenario uji di `docs/test-cases.md`:

1. Buka aplikasi: empty state dan ringkasan 0% (uji 1).
2. Tekan Simpan tanpa isi: pesan "Pilih waktu makan" dan "Pilih minimal satu isi piring" (uji 3).
3. Pilih Sarapan, tambah Pokok, ketik porsi "a" lalu "0" lalu "-1": pesan porsi berbeda-beda, aplikasi tidak crash (uji 4, 5).
4. Reset: semua kembali ke kondisi awal (uji 8).
5. Isi Sarapan valid: toast tersimpan, persentase naik, Siang otomatis terpilih (uji 2, 7).
6. Ketuk Sarapan lagi: terkunci "Sarapan tanggal ini sudah dicatat" (uji 9).
7. Tambah catatan di tanggal lain lewat strip tanggal: riwayat per hari di LazyColumn (uji 6).
8. Lihat detail hari: total porsi dan cerita Bunda.
9. Profil Bunda: matikan arahan suplemen, persentase memakai penyebut 5 (uji 12).
10. Lengkapi hari ini: confetti dan "Hebat, Bunda!" (uji 13).

## 4. Penjelasan kode (3 menit)

| Topik | File | Hal yang dijelaskan |
|---|---|---|
| Data class & collection | `model/Model.kt` | `CatatanMakan` (Long, LocalDate, enum, Map, Boolean, String), enum dengan properti |
| Logika & percabangan | `logic/Ringkasan.kt` | `ringkasanHari` (filter, Set gabungan, persentase), `statusDari` (when) |
| Validasi | `logic/Validasi.kt` | `validasiPorsi` dengan `toIntOrNull()`, `validasiCatatan` mengembalikan peta pesan |
| State | `ui/MaternaTrackScreen.kt` | `mutableStateListOf`, `mutableStateMapOf`, `derivedStateOf`, fungsi `simpan()` dan `reset()` |
| LazyColumn & Card | `ui/MaternaTrackScreen.kt`, `ui/components/Riwayat.kt` | `items(harian, key = ...)`, `animateItem()` |
| Unit test | `app/src/test/` | 24 test, contoh `empatDariEnamKomponenMenjadi67Persen` |

## 5. Penutup (1 menit)

- Batasan UTS: data in-memory, satu layar, belum ada ubah/hapus.
- Rencana UAS: Room + CRUD, layar Riwayat dengan filter dan rekap mingguan, layar Edukasi (`docs/wireframe/uas-*.png`).

## Persiapan live modification

Contoh perubahan kecil yang mungkin diminta dan letak kodenya:

| Permintaan | Yang diubah |
|---|---|
| Batas porsi menjadi 1–3 | `PORSI_MAKS` di `logic/Validasi.kt` (pesan ikut berubah) |
| Batas cerita menjadi 50 karakter | `CERITA_MAKS` di `logic/Validasi.kt` |
| Ambang status "Sedikit lagi" mulai 50% | `statusDari` di `logic/Ringkasan.kt` |
| Tambah kelompok makanan baru | `KelompokMakanan` di `model/Model.kt` + gambar dan warna di `ui/components/Gaya.kt` |
| Camilan tidak boleh berulang | `CAMILAN("Camilan", false)` di `model/Model.kt` |
| Ganti warna utama | `MtColor.Primary` di `ui/theme/Color.kt` |
