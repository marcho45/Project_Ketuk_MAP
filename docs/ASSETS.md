# Aset Ketuk

Lokasi: `app/src/main/res/drawable-nodpi/` (datar, tanpa subfolder). Pakai `R.drawable.<nama>`.
Nama file di Android sudah final dan mengikuti konvensi snake_case. Kolom "Nama asli" hanya untuk pelacakan dari Figma.

## Tabel Aset (diurutkan berdasarkan pemakaian)

| Nama di Android | Format | Isi | Dipakai di | Catatan |
|---|---|---|---|---|
| logo_ketuk | PNG | Logo pintu dengan sinar | Loading, Login | - |
| tok_melambai | PNG | Maskot Tok melambai | Onboarding 1-4 | - |
| tok_memeriksa | PNG | Tok dengan kaca pembesar | Login, Register | - |
| bg_desa | PNG | Latar pemandangan desa | Onboarding (bagian atas) | Transparansi ~75%, letakkan di atas latar krem |
| rumah_besar | PNG | Rumah besar dengan semak | Loading (pojok kiri bawah) | - |
| pagar_bunga | PNG | Pagar kayu dengan bunga | Loading (pojok kanan bawah) | - |
| wali_dengan_tok | PNG | Wali perempuan bersama Tok | Pilih Peran (kartu 1) | - |
| rumah_desa | PNG | Dua rumah dan semak | Pilih Peran (kartu 2) | - |
| oma_daun | PNG | Oma tersenyum dengan daun | Pilih Peran (kartu 3) | - |
| oma_avatar | PNG | Avatar Oma (lingkaran) | Home Wali (kartu Oma) | Ukuran kecil (~83px) |
| nav_home | PNG | Ikon rumah | Bottom bar | Ukuran kecil (~30-40px) |
| nav_riwayat | PNG | Ikon jam | Bottom bar | Ukuran kecil (~30-40px) |
| nav_notifikasi | PNG | Ikon lonceng | Bottom bar | Ukuran kecil (~30-40px) |
| nav_profil | PNG | Ikon profil | Bottom bar | Ukuran kecil (~30-40px) |
| nav_lokasi | PNG | Ikon pin lokasi | Bottom bar (Home Penjaga) | Ukuran kecil (~30-40px) |
| btn_panah | PNG | Tombol pill panah | Referensi | **Jangan dipakai langsung**, buat di Compose |
| indikator_onboarding | PNG | 4 pil indikator | Referensi | **Jangan dipakai langsung**, buat di Compose agar beranimasi |
| logo_google | PNG | Logo Google (Sign In) | Login | Untuk tombol "Lanjut dengan Google" |
| penjaga_lintang | JPG | Avatar Penjaga Lintang | Home Wali (kartu Penjaga) | Format JPG (bukan PNG) |
| peta_lokasi | PNG | Ilustrasi peta dengan pin | Home Penjaga (?) | Perlu konfirmasi pemakaian |

## Catatan Penting

### Aset Referensi (Jangan Dipakai Langsung)
- **`btn_panah`**: Tombol panah harus dibuat di Compose dengan `Canvas` atau `Icon` supaya bisa berubah warna saat ditekan dan mengikuti tema.
- **`indikator_onboarding`**: Indikator halaman harus dibuat di Compose dengan animasi transisi antar halaman.

### Ukuran dan Kualitas
- Ikon navbar (`nav_*`) dan avatar berukuran kecil (30-83 px). Kalau terlihat buram di HP, export ulang dari Figma @3x dengan nama yang sama lalu timpa file.
- `bg_desa` memiliki transparansi seragam sekitar 75%. Selalu letakkan di atas latar krem agar terlihat sesuai desain.
- `pagar_bunga` untuk elemen dekoratif pojok. Jangan diregangkan berlebihan, gunakan ukuran asli atau scale proporsional.

### Format File
- Hampir semua aset menggunakan format PNG untuk mendukung transparansi.
- **Pengecualian**: `penjaga_lintang.jpg` menggunakan format JPG (tidak ada transparansi).

### Ikon Lain
- Ikon sistem (panah kembali, mata password, telepon, kunci, centang, dst) gunakan **Material Icons Rounded** dari Compose, jangan export dari Figma.

## Perubahan dari Versi Sebelumnya
- Ditambahkan: `logo_google`, `nav_lokasi`, `penjaga_lintang`, `peta_lokasi`
- Format file diseragamkan: semua sudah `.png` kecuali `penjaga_lintang.jpg`
- Nama file sudah sesuai konvensi Android: snake_case huruf kecil
- Dihapus kolom "Nama asli" karena nama sudah final dan kolom tersebut tidak membantu developer

---
**Terakhir diperbarui:** 2026-10-13 (disesuaikan dengan isi aktual `drawable-nodpi/`)
