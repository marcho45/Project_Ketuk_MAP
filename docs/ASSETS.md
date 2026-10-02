# Aset Ketuk

Lokasi: `app/src/main/res/drawable-nodpi/` (datar, tanpa subfolder). Pakai `R.drawable.<nama>`.
Aset sudah diberi nama final di folder ini. Kolom "Nama asli" hanya untuk pelacakan dari Figma.

| Nama di Android | Nama asli | Isi | Dipakai di |
|---|---|---|---|
| logo_ketuk | logoketuk_1.png | Logo pintu dengan sinar | Loading, Login |
| tok_melambai | Asset_Ketuk (2).png | Maskot Tok melambai | Onboarding 1-4 |
| tok_memeriksa | Asset_Ketuk_3 (2).png | Tok dengan kaca pembesar | Login |
| wali_dengan_tok | Asset_Ketuk_3 (3).png | Wali perempuan bersama Tok | Pilih Peran (kartu 1) |
| rumah_desa | Asset_Ketuk_4.png | Dua rumah dan semak | Pilih Peran (kartu 2) |
| oma_daun | Asset_Ketuk_5.png | Oma tersenyum dengan daun | Pilih Peran (kartu 3) |
| oma_avatar | OMAKETUK_1 (1).png | Avatar Oma (lingkaran) | Home Wali (kartu Oma) |
| bg_desa | Asset_Ketuk_2 (2).png | Latar pemandangan desa | Onboarding (bagian atas) |
| rumah_besar | assetketuk_1.png | Rumah besar dengan semak | Loading (pojok kiri bawah) |
| pagar_bunga | assetketuk_3.png | Pagar kayu dengan bunga | Loading (pojok kanan bawah) |
| nav_home | NAVBARKETUK_1 (2).png | Ikon rumah | Bottom bar |
| nav_riwayat | NAVBARKETUK_2.png | Ikon jam | Bottom bar |
| nav_notifikasi | NAVBARKETUK_3 (1).png | Ikon lonceng | Bottom bar |
| nav_profil | NAVBARKETUK_4.png | Ikon profil | Bottom bar |
| btn_panah | rename.png | Tombol pill panah | Referensi (lebih baik dibuat di kode) |
| indikator_onboarding | Progress_Bar.png | 4 pil indikator | Referensi (dibuat di kode agar beranimasi) |

## Catatan
- `indikator_onboarding` dan `btn_panah` jangan dipakai sebagai gambar: buat di Compose supaya aktif/nonaktif dan animasinya jalan.
- Ikon navbar dan avatar berukuran kecil (30-83 px). Kalau terlihat buram di HP, export ulang dari Figma @3x dengan nama yang sama lalu timpa file.
- `bg_desa` punya transparansi seragam sekitar 75%. Letakkan di atas latar krem.
- Ikon lain (panah kembali, mata password, telepon, kunci, centang, dst) pakai Material Icons Rounded.
