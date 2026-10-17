# Spesifikasi layar (dari Figma)

Terakhir diperbarui: 2026-10-17

Gambar acuan ada di docs/figma/. Kerjakan sesuai gambar. Teks di bawah disalin dari Figma.
Kalau teks di file ini bertentangan dengan gambar, IKUTI GAMBAR lalu catat perbedaannya di bagian "Perbedaan dengan Figma".
Layar yang belum didesain (Pairing, Alarm Aktif, Tugas Penjaga, Riwayat, Sinkron, dst) JANGAN dibuat dulu.

## Pemetaan file Figma (nama asli di docs/figma/)

Nama file lama di versi sebelumnya sudah tidak dipakai. Gunakan nama asli berikut:

| File Figma asli | Isi | Fase | Catatan |
|---|---|---|---|
| 01. Initial Phase (2).png | Loading, Onboarding 1-4, Login, Pilih Peran (alur utama) | Initial Phase | Menggantikan `initial_phase.png` dan `select_role_page.png` |
| 02. Initial Phase Wali.png | Varian/lanjutan Initial Phase untuk peran Wali | Initial Phase | TODO: isi belum dipetakan, verifikasi dengan membuka gambar |
| 03. Initial Phase oma.png | Varian/lanjutan Initial Phase untuk peran Oma | Initial Phase | TODO: isi belum dipetakan, verifikasi dengan membuka gambar |
| 04. Core Phase Wali.png | Home Wali (dan layar inti Wali) | Core Phase | Menggantikan `home_page_wali.png`. Belum dikerjakan |
| 05. Core Phase Penjaga.png | Layar inti Penjaga | Core Phase | Belum dikerjakan |

Aturan baca: saat mengerjakan Initial Phase, file 04 dan 05 BOLEH dibaca hanya untuk konsistensi design system (warna, tipografi, komponen). Jangan dikerjakan.

## Status layar

| # | Layar | Sumber Figma | Fase | Status |
|---|---|---|---|---|
| 1 | Loading | 01 | Initial | Perlu perbaikan (pagar bawah tidak penuh) |
| 2-5 | Onboarding (4 halaman) | 01 | Initial | Audit dulu |
| 6 | Login | 01 | Initial | Audit dulu |
| 7 | Pilih Peran | 01 | Initial | Audit dulu (kartu peran horizontal belum cocok) |
| 6b | Register (Daftar) | - | Initial | **SELESAI** (2026-10-17) |

| - | OTP | 02 / 03 (TODO: pastikan) | Initial | Belum ada |
| 8 | Home Wali | 04 | Core | Ditunda. Pakai placeholder sederhana dulu |
| - | Layar Penjaga | 05 | Core | Ditunda |

Isi kolom Status dengan: belum ada / sebagian / sesuai figma. Perbarui setiap selesai tugas.

## Alur
Loading -> Onboarding (4 halaman) -> Login -> Pilih Peran -> (Wali) Home Wali dengan bottom bar.

Tahap Initial Phase: Pilih Peran -> placeholder sederhana untuk semua peran. Home Wali yang sebenarnya dikerjakan di Core Phase.

## 1. Loading (docs/figma/01. Initial Phase (2).png, layar 1)
- Latar krem. Logo (logo_ketuk) di tengah, teks "Ketuk" besar warna terakota di bawah logo,
  tagline kecil terakota: "Kalau HP orang tersayang hening, ada yang mengetuk pintu."
- Pojok kiri bawah: rumah_besar. Pojok kanan bawah: pagar_bunga.
- Animasi: logo muncul perlahan (fade + scale kecil), lalu otomatis lanjut ke Onboarding setelah sekitar 2 detik.

### Perbaikan yang diminta (pagar bawah)
- Masalah: area bawah tidak penuh. pagar_bunga (96x63) hanya satu potong sehingga ada ruang kosong antara rumah_besar dan tepi kanan layar.
- Target: pagar mengisi lebar layar dari ujung rumah_besar sampai tepi kanan, di semua ukuran layar.
- Solusi yang dipilih: tile/repeat pagar_bunga secara horizontal dengan jumlah dihitung dari lebar layar, bukan menambah satu segmen manual. Pastikan potongan terakhir tidak terpotong jelek.
- Kalau pagar_bunga tidak seamless saat disambung, catat persis asset apa yang perlu diekspor ulang dari Figma (nama, ukuran, format).

## 2-5. Onboarding (4 halaman, HorizontalPager)
- Bagian atas: bg_desa penuh lebar, dengan tok_melambai di tengah. Bagian bawah: panel krem dengan sudut atas membulat
  yang menimpa gambar sedikit.
- Di panel: judul (tebal), isi teks, indikator 4 pil (pil aktif lebih gelap/terakota, lainnya terakota muda; dibuat di kode,
  animasikan lebar/warna), lalu baris bawah: "Skip" di kiri dan tombol pill terakota berisi panah di kanan.
- Halaman terakhir: tombol "Mulai" (pill terakota) menggantikan Skip + panah, di tengah.
- "Skip" melompat ke Login.

Teks:
1. Judul "Ada yang mengetuk". Isi: "Ketuk menjaga dari jauh, tanpa Anda harus menelepon setiap jam."
2. Judul "HP yang memberi kabar". Isi: "Ketuk membaca langkah, gerak, dan buka layar di HP secara diam-diam. Tidak ada yang perlu dipencet."
3. Judul "Sapa dulu, baru ketuk". Daftar bernomor (lingkaran angka krem gelap, garis putus-putus penghubung):
    1. Hening: HP tidak menunjukkan aktivitas lebih lama dari biasanya.
    2. Sapa: Layar menyala: "Oma, sehat?" Cukup tekan hijau.
    3. Wali: Kalau tidak dijawab, Anda mendapat kabar (lewat notifikasi atau SMS).
    4. Ketuk: Kalau Anda minta atau tidak sempat merespons, tetangga terdekat yang Anda pilih datang mengecek.
       Animasi: tiap langkah muncul bertahap saat halaman ini terlihat.
4. Judul "Tanpa kuota, tetap jalan". Isi: "Tanpa internet, Ketuk mengirim SMS dari HP. Pastikan ada pulsa SMS."
   Disclaimer kecil di dekat bawah: "Ketuk bukan alat medis dan belum mendeteksi jatuh. Ketuk hanya tahu kalau aktivitas berhenti. Tetap hubungi 112/119 untuk keadaan gawat."

## 6. Login (docs/figma/01. Initial Phase (2).png, layar 6)
- Atas: tok_memeriksa, lalu logo kecil + "Ketuk", lalu "Halo! Senang melihatmu kembali".
- Field "Nomor HP" (ikon telepon, hint "Contoh: 0867 6767 6767"), field "Password" (ikon kunci, hint "Masukkan password", ikon mata untuk tampil/sembunyi).
- Tombol "Masuk" (pill terakota, penuh lebar). Pemisah garis dengan teks "atau".
- Tombol outline "Lanjut dengan Google". Bawah: "Belum punya akun? Daftar disini" (tautan hijau).
- Tahap 1: tombol hanya lanjut ke Pilih Peran, belum ada autentikasi sungguhan.
- CATATAN TIM: blueprint menulis Email + OTP, Figma memakai Nomor HP + Password. Ikuti Figma, tandai TODO.

## 7. Pilih Peran (docs/figma/01. Initial Phase (2).png; TODO: konfirmasi apakah layar ini ada di 01, 02, atau 03)
- Panah kembali kiri atas. Judul "Siapa kamu?", subjudul "Pilih peranmu untuk melanjutkan".
- Tiga kartu bersudut membulat dengan warna latar berbeda (peach, kuning muda, hijau sage), masing-masing: ilustrasi di kiri atas,
  judul tebal, deskripsi, dan tombol bulat berisi panah di kanan bawah.
    1. wali_dengan_tok. "Saya wali Oma dan Opa". "Saya adalah anak, cucu, atau keluarga yang merawat Oma dan Opa."
    2. rumah_desa. Judul di Figma tertulis "Saya wali Oma dan Opa" (kemungkinan salah salin; tanya tim, usulan: "Saya penjaga").
       "Saya adalah tetangga atau orang yang paling dekat dari rumah Oma dan Opa."
    3. oma_daun. "Saya adalah Oma/Opa". "Saya ingin memantau kesehatan dan aktivitas saya sendiri."
- Memilih kartu menandai kartu itu (animasi border/skala), lalu tombol "Lanjutkan" (pill terakota, penuh lebar) di bawah.
- Tahap 1: semua peran -> layar placeholder sederhana (Home Wali yang asli ada di Core Phase).
- Catatan: kartu peran di desain terbaru ditata horizontal. Kode saat ini belum cocok. Cocokkan dengan gambar.
- Sementara: judul kartu 2 pakai "Saya penjaga" dengan komentar TODO di kode. Jangan diubah diam-diam, catat di laporan.


### 6b. Register (Daftar)

**Tidak ada file Figma khusus. Mengikuti gaya visual LoginScreen.**

- Layout: maskot tok_memeriksa (140dp) + asset_ketuk_daun (32dp) + logo Ketuk + teks "Yuk, buat akun barumu"
- 5 field input dengan label di atas field (bukan floating label):
  1. Nama Lengkap (icon Person, placeholder "Masukkan nama lengkap")
  2. Email (icon Email, placeholder "Masukkan email", keyboard email)
  3. Nomor HP (icon Phone, placeholder "Masukkan nomor HP", keyboard phone, hanya terima digit)
  4. Password (icon Lock, placeholder "Masukkan password", toggle visibility)
  5. Konfirmasi Password (icon Lock, placeholder "Masukkan ulang password", toggle visibility)
- Validasi lokal (client-side):
  - Nama tidak boleh kosong
  - Email format valid (regex dengan @)
  - Nomor HP 9-13 digit angka
  - Password minimal 8 karakter
  - Konfirmasi password harus sama dengan password
  - Error message muncul di bawah field yang error
- Tombol "Daftar" (terakota, disabled jika ada field kosong atau loading)
- Pemisah "atau" dengan garis horizontal
- Tombol "Lanjut dengan Google" (outline, logo Google di kiri)
- Teks "Sudah punya akun? Masuk" (kata "Masuk" hijau underline, clickable)
- Alur: tombol Daftar sukses → VerificationScreen; tombol "Masuk" → popBackStack ke Login
- Warna: sama dengan Login (field bg peach 12%, border terakota, button terakota)
- Animasi: loading indicator saat proses register (1.5 detik simulasi)


## 8. Home Wali (docs/figma/04. Core Phase Wali.png) - DITUNDA (Core Phase)
Spesifikasi disimpan untuk fase berikutnya. Jangan dikerjakan di tugas Initial Phase.

- Header: "Halo, Nandaa" (judul tebal) + "Oma kamu hari ini bagaimana?"; kanan atas tombol lonceng dengan titik merah.
  (Nama "Nandaa" kemungkinan salah ketik; ambil dari data contoh `userName`.)
- Kartu Oma: oma_avatar (bulat), "Oma Diyas", "Aktivitas normal", chip hijau "Sehat" di kanan.
- Judul "Status hari ini" lalu kartu besar dengan baris-baris kosong (placeholder dari desain). Isi dengan 3 baris data contoh:
  Aktivitas, HP, Sapa (ikon + teks + status), tanpa membuat fitur sungguhan.
- Bottom bar 4 tab: Home (nav_home), Riwayat (nav_riwayat), Notifikasi (nav_notifikasi), Profil (nav_profil).
  Tab aktif diberi latar pil krem gelap. Tab selain Home cukup layar placeholder.
- Animasi: pergantian tab halus (crossfade), pil tab aktif berpindah dengan animasi.

## Asset per layar (rujuk ASSETS.md)

| Layar | Asset |
|---|---|
| Loading | logo_ketuk, rumah_besar (269x189), pagar_bunga (96x63) |
| Onboarding | bg_desa, tok_melambai (217x222) |
| Login | tok_memeriksa (132x143), logo_ketuk, logo_google, asset_awan |
| Register | tok_memeriksa (132x143), logo_ketuk, logo_google, asset_ketuk_daun, asset_awan |
| Pilih Peran | wali_dengan_tok (142x84), rumah_desa (171x68), oma_daun |
| Home Wali (ditunda) | oma_avatar, nav_home, nav_riwayat, nav_notifikasi, nav_profil |

Kalau ada asset di gambar figma yang belum ada di ASSETS.md atau res/, jangan mengarang. Catat nama, ukuran, dan format yang perlu diekspor.

## Perbedaan dengan Figma (isi saat audit)

Tulis di sini setiap kali gambar figma berbeda dari spesifikasi di atas. Format:
- [tanggal] Layar, apa bedanya, mana yang diikuti.

Sudah diketahui:
- [2026-10-12] Pilih Peran: kartu peran di figma terbaru horizontal, kode lama belum cocok. Ikuti figma.
- [2026-10-12] Nama file figma di versi lama SCREENS.md (`initial_phase.png`, `select_role_page.png`, `home_page_wali.png`) sudah usang. Diganti dengan nama asli di tabel pemetaan.
- [2026-10-12] Isi file 02 dan 03 belum dipetakan. Harus diverifikasi saat audit.

## Catatan teknis

- Folder drawable: ASSETS.md menulis `drawable-nodpi`. Pastikan ejaan folder aslinya di app/src/main/res/ benar (bukan salah eja). Kalau salah, laporkan dulu sebelum rename.
- Nama resource memakai snake_case huruf kecil tanpa spasi atau kurung.