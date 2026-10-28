# Penggunaan AI

AI dipakai sebagai asisten selama pengerjaan. Arah aplikasi, keputusan desain dan logika, pengujian, serta pengumpulan tetap dikendalikan kelompok.

## Alat yang dipakai

| Alat | Dipakai untuk |
|---|---|
| Asisten AI untuk coding | Menulis kode Kotlin/Compose, unit test, dan draf dokumentasi sesuai arahan kelompok |
| Alat desain dengan bantuan AI | Membantu membuat wireframe dan ilustrasi aplikasi |

## Peran kelompok

- Menentukan masalah, pengguna, fitur, model data, dan aturan validasi.
- Mengarahkan tampilan dan meminta revisi berulang sampai sesuai (navbar, notifikasi simpan, warna, pop-up, ikon aplikasi).
- Mencoba setiap perubahan di emulator, menemukan bug, dan memutuskan apa yang masuk ke commit.
- Memahami seluruh kode untuk defense dan live modification.

## Peran AI

- Menulis kode dan unit test berdasarkan arahan dan revisi dari kelompok.
- Membantu menganalisis dan memperbaiki bug yang ditemukan kelompok.
- Menyusun draf README dan test case untuk diperiksa kelompok.

## Contoh perintah ke AI

- "Bantu analisa bug ini dan mari kita perbaiki: kartu waktu makan yang dipilih tetap membesar setelah diketuk"
- "Mari benahi UI ini, konten tertutup status bar saat digulir"
- "Tambahkan validasi porsi supaya huruf dan angka negatif tidak membuat aplikasi crash"

## Verifikasi oleh kelompok

- Aplikasi diuji di emulator dengan 21 skenario di `docs/test-cases.md`.
- 25 unit test dijalankan dengan `./gradlew testDebugUnitTest`.

## Batasan

AI tidak dipakai di dalam aplikasi. Aplikasi tidak memberi diagnosis, target gizi, atau dosis suplemen.
