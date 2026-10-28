# Test Case MaternaTrack

Lingkungan uji: emulator Pixel 7, Android 16 (API 36), build debug. Tanggal perangkat: Selasa, 27 Oktober 2026.

## Pengujian minimum (sesuai ketentuan UTS)

| No | Skenario | Langkah | Hasil yang diharapkan | Hasil aktual | Status | Bukti |
|---|---|---|---|---|---|---|
| 1 | Aplikasi pertama dibuka | Buka aplikasi | Aplikasi berjalan dan menampilkan empty state | Tampil judul, sapaan, kartu "Piring hari ini" 0% (0 dari 6), form, dan empty state "Belum ada catatan makan" | Lulus | [01](screenshots/01-kondisi-awal.png), [02](screenshots/02-riwayat-kosong.png) |
| 2 | Seluruh input valid | Pilih 27 Okt, Sarapan, tambah Pokok, Hewani, Buah, centang Suplemen, Simpan | Data berhasil ditambahkan dan ringkasan berubah | Toast "Sarapan tersimpan", kartu hari muncul di riwayat, "Piring hari ini" menjadi 67% (4 dari 6), Siang otomatis terpilih | Lulus | [05](screenshots/05-toast-tersimpan.png), [06](screenshots/06-beranda-terisi.png) |
| 3 | Input wajib kosong | Tanpa mengisi apa pun, tekan Simpan Catatan | Pesan kesalahan ditampilkan | Layar menggulir ke Waktu makan, muncul "Pilih waktu makan" dan "Pilih minimal satu isi piring", data tidak tersimpan | Lulus | [03](screenshots/03-validasi-kosong.png) |
| 4 | Input angka berisi huruf | Pilih Sarapan, tambah Pokok, ganti angka porsi menjadi "a", Simpan | Aplikasi tidak crash dan menampilkan kesalahan | Aplikasi tetap berjalan, muncul "Porsi harus berupa angka" di bawah kartu Makanan pokok | Lulus | — |
| 5 | Nilai negatif/nol | Ketik porsi "0", Simpan; lalu ketik "-1", Simpan | Data ditolak apabila tidak sesuai konteks | "0" ditolak dengan "Porsi 1 sampai 5"; "-1" ditolak dengan "Porsi tidak boleh negatif" | Lulus | [04](screenshots/04-porsi-nol.png) |
| 6 | Menambahkan beberapa data | Simpan 6 catatan: 25 Okt (Sarapan, Siang), 26 Okt (Sarapan, Siang, Malam), 27 Okt (Sarapan) | Seluruh data muncul dalam LazyColumn | Riwayat menampilkan 3 kartu hari (27, 26, 25 Okt) berisi baris tiap waktu makan, chip "6 catatan" | Lulus | [07](screenshots/07-riwayat.png) |
| 7 | Memeriksa ringkasan | Bandingkan persentase dengan data no. 6 | Perhitungan sesuai dengan data | 27 Okt: 4/6 = 67%; 26 Okt: 6/6 = 100%; 25 Okt: 5/6 = 83%; detail 26 Okt: Pokok 4 porsi (1+2+1), Sayur 3 porsi (2+1) | Lulus | [07](screenshots/07-riwayat.png), [08](screenshots/08-detail-hari.png) |
| 8 | Tombol reset | Isi beberapa field (waktu, porsi "a"), tekan Reset | Input kembali ke kondisi awal | Tanggal kembali ke hari ini, waktu makan dan isi piring kosong, cerita kosong, pesan kesalahan hilang | Lulus | — |

## Pengujian tambahan

| No | Skenario | Langkah | Hasil yang diharapkan | Hasil aktual | Status | Bukti |
|---|---|---|---|---|---|---|
| 9 | Waktu makan dobel | Setelah Sarapan 27 Okt tersimpan, ketuk kartu Sarapan lagi | Ditolak | Kartu Sarapan bercentang hijau dan terkunci, muncul "Sarapan tanggal ini sudah dicatat" | Lulus | [10](screenshots/10-sarapan-sudah-dicatat.png) |
| 10 | Camilan berulang | Simpan Camilan dua kali di tanggal yang sama | Diterima | Keduanya tersimpan: riwayat "2 catatan", kartu hari "2 waktu makan dicatat" (juga unit test `camilanBolehBerulang`) | Lulus | — |
| 11 | Tanggal masa depan | Buka kalender lewat tombol "Okt 2026" | Tanggal setelah hari ini tidak bisa dipilih | Tanggal 28–31 Oktober tampil abu-abu dan tidak bisa diketuk | Lulus | [11](screenshots/11-kalender.png) |
| 12 | Tanpa arahan suplemen | Buka Profil Bunda, matikan switch arahan suplemen | Suplemen tidak dihitung | Kartu Suplemen di form menjadi "Tidak diresepkan" dan nonaktif; kartu Piring hari ini menjadi "1 dari 5 kelompok gizi terpenuhi" | Lulus | [09](screenshots/09-profil-bunda.png) |
| 13 | Isi piring hari ini lengkap | Lengkapi 5 kelompok + suplemen untuk hari ini | Ada umpan balik | Confetti, sapaan berubah menjadi "Hebat, Bunda!", cincin 100% hijau | Lulus | [12](screenshots/12-hari-lengkap.png) |
| 14 | Cerita lebih dari 100 karakter | Ketik teks panjang di Cerita Bunda | Dibatasi | Ketikan berhenti di 100 karakter, penghitung "100/100" | Lulus | — |

## Unit test

`./gradlew testDebugUnitTest` → 24 test, 0 gagal.

| Kelas | Jumlah | Cakupan |
|---|---|---|
| `RingkasanTest` | 10 | status dari persen, gabungan kelompok, 67%/100%, penyebut 5, urutan tanggal, total porsi, waktu makan berikutnya |
| `ValidasiTest` | 9 | porsi kosong/huruf/negatif/nol/lebih dari 5, tanggal masa depan, waktu wajib, Sarapan dobel, Camilan berulang, isi piring minimal satu |
| `EdukasiTest` | 5 | urutan pesan edukasi sayur, buah, protein, pokok, lengkap |
