# Spesifikasi layar (dari Figma)

Gambar acuan ada di docs/figma/. Kerjakan sesuai gambar. Teks di bawah disalin dari Figma.
Layar yang belum didesain (Pairing, Alarm Aktif, Tugas Penjaga, Riwayat, Sinkron, dst) JANGAN dibuat dulu.

## Alur
Loading -> Onboarding (4 halaman) -> Login -> Pilih Peran -> (Wali) Home Wali dengan bottom bar.

## 1. Loading (docs/figma/initial_phase.png, layar 1)
- Latar krem. Logo (logo_ketuk) di tengah, teks "Ketuk" besar warna terakota di bawah logo,
  tagline kecil terakota: "Kalau HP orang tersayang hening, ada yang mengetuk pintu."
- Pojok kiri bawah: rumah_besar. Pojok kanan bawah: pagar_bunga.
- Animasi: logo muncul perlahan (fade + scale kecil), lalu otomatis lanjut ke Onboarding setelah sekitar 2 detik.

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

## 6. Login (docs/figma/initial_phase.png, layar 6)
- Atas: tok_memeriksa, lalu logo kecil + "Ketuk", lalu "Halo! Senang melihatmu kembali".
- Field "Nomor HP" (ikon telepon, hint "Contoh: 0867 6767 6767"), field "Password" (ikon kunci, hint "Masukkan password", ikon mata untuk tampil/sembunyi).
- Tombol "Masuk" (pill terakota, penuh lebar). Pemisah garis dengan teks "atau".
- Tombol outline "Lanjut dengan Google". Bawah: "Belum punya akun? Daftar disini" (tautan hijau).
- Tahap 1: tombol hanya lanjut ke Pilih Peran, belum ada autentikasi sungguhan.
- CATATAN TIM: blueprint menulis Email + OTP, Figma memakai Nomor HP + Password. Ikuti Figma, tandai TODO.

## 7. Pilih Peran (docs/figma/select_role_page.png)
- Panah kembali kiri atas. Judul "Siapa kamu?", subjudul "Pilih peranmu untuk melanjutkan".
- Tiga kartu bersudut membulat dengan warna latar berbeda (peach, kuning muda, hijau sage), masing-masing: ilustrasi di kiri atas,
  judul tebal, deskripsi, dan tombol bulat berisi panah di kanan bawah.
  1. wali_dengan_tok. "Saya wali Oma dan Opa". "Saya adalah anak, cucu, atau keluarga yang merawat Oma dan Opa."
  2. rumah_desa. Judul di Figma tertulis "Saya wali Oma dan Opa" (kemungkinan salah salin; tanya tim, usulan: "Saya penjaga").
     "Saya adalah tetangga atau orang yang paling dekat dari rumah Oma dan Opa."
  3. oma_daun. "Saya adalah Oma/Opa". "Saya ingin memantau kesehatan dan aktivitas saya sendiri."
- Memilih kartu menandai kartu itu (animasi border/skala), lalu tombol "Lanjutkan" (pill terakota, penuh lebar) di bawah.
- Tahap 1: peran 1 (Wali) -> Home Wali. Dua peran lain -> layar placeholder sederhana.

## 8. Home Wali (docs/figma/home_page_wali.png)
- Header: "Halo, Nandaa" (judul tebal) + "Oma kamu hari ini bagaimana?"; kanan atas tombol lonceng dengan titik merah.
  (Nama "Nandaa" kemungkinan salah ketik; ambil dari data contoh `userName`.)
- Kartu Oma: oma_avatar (bulat), "Oma Diyas", "Aktivitas normal", chip hijau "Sehat" di kanan.
- Judul "Status hari ini" lalu kartu besar dengan baris-baris kosong (placeholder dari desain). Isi dengan 3 baris data contoh:
  Aktivitas, HP, Sapa (ikon + teks + status), tanpa membuat fitur sungguhan.
- Bottom bar 4 tab: Home (nav_home), Riwayat (nav_riwayat), Notifikasi (nav_notifikasi), Profil (nav_profil).
  Tab aktif diberi latar pil krem gelap. Tab selain Home cukup layar placeholder.
- Animasi: pergantian tab halus (crossfade), pil tab aktif berpindah dengan animasi.
