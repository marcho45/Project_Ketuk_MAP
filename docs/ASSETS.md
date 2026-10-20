# Aset Ketuk

Lokasi: `app/src/main/res/drawable-nodpi/` (datar, tanpa subfolder). Pakai `R.drawable.<nama>`.
Nama file di Android sudah final dan mengikuti konvensi snake_case.

## Tabel Aset Lengkap (`app/src/main/res/drawable-nodpi/`)

| Nama di Android | Format | Isi | Dipakai di | Catatan |
|---|---|---|---|---|
| `logo_ketuk` | PNG | Logo pintu dengan sinar | Loading (Splash), Login, Register | - |
| `tok_melambai` | PNG | Maskot Tok melambai | Onboarding 1-4 | - |
| `tok_memeriksa` | PNG | Tok dengan kaca pembesar | Login, Register | - |
| `maskot_hp` | PNG | Maskot Tok memegang HP | Verifikasi OTP (`VerificationScreen`) | - |
| `asset_awan` | PNG | Dekorasi awan latar | Login, Register | - |
| `asset_ketuk_daun` | PNG | Ikon daun kecil dekorasi | Login, Register | - |
| `bg_desa` | PNG | Latar pemandangan desa | Onboarding 1-4, Verifikasi OTP | Transparansi ~75%, letakkan di atas latar krem |
| `rumah_besar` | PNG | Rumah besar dengan semak | Loading (pojok kiri bawah) | - |
| `pagar_bunga` | PNG | Pagar kayu dengan bunga | Loading (bagian bawah berulang) | - |
| `role_wali` | PNG | Ilustrasi kartu peran Wali | Pilih Peran (`RoleSelectScreen` kartu 1) | Menggantikan pemakaian `wali_dengan_tok` di kartu |
| `role_penjaga` | PNG | Ilustrasi kartu peran Penjaga | Pilih Peran (`RoleSelectScreen` kartu 2) | Menggantikan pemakaian `rumah_desa` di kartu |
| `role_opa` | PNG | Ilustrasi kartu peran Oma/Opa | Pilih Peran (`RoleSelectScreen` kartu 3) | Menggantikan pemakaian `oma_daun` di kartu |
| `wali_dengan_tok` | PNG | Wali perempuan bersama Tok | Cadangan / referensi Pilih Peran | - |
| `rumah_desa` | PNG | Dua rumah dan semak | Cadangan / referensi Pilih Peran | - |
| `oma_daun` | PNG | Oma tersenyum dengan daun | Cadangan / referensi Pilih Peran | - |
| `oma_avatar` | PNG | Avatar Oma (lingkaran) | Beranda Wali, Lokasi Wali, Profil Wali, Pilih Penjaga, Lokasi Penjaga | Ukuran ~83px |
| `penjaga_lintang` | JPG | Avatar Penjaga Lintang | Lokasi Wali (`LokasiScreen` - kartu Penjaga terdekat) | Sudah dipasang menggantikan placeholder huruf `"L"` |
| `peta_lokasi` | PNG | Ilustrasi peta dengan pin rumah & radius | Lokasi Wali (M9), Beranda & Lokasi Penjaga (M10) | Mockup peta statis |
| `qr_pairing` | PNG | Gambar kode QR | Circle Keluarga (`CircleKeluargaScreen`), QR Wali (`QrWaliScreen`), Pindai QR (M4) | Dipakai untuk tampilan QR & simulasi scan |
| `qr_pairing_blur` | PNG | Gambar latar QR blur | Loading Pairing Wali (`QrWaliScreen`) & Oma (M4) | Aman untuk minSdk < 31 tanpa `Modifier.blur` |
| `loading_card_bg` | PNG | Latar kartu loading pairing | Loading Pairing | - |
| `ic_spinner` | PNG | Ikon spinner biru | Loading Pairing Wali (`QrWaliScreen`) & Oma (M4) | - |
| `nav_home` | PNG | Ikon rumah | Bottom bar (Wali & Penjaga) | - |
| `nav_riwayat` | PNG | Ikon jam | Bottom bar (Wali) | - |
| `nav_lokasi` | PNG | Ikon pin lokasi | Bottom bar (Wali & Penjaga) | - |
| `nav_notifikasi` | PNG | Ikon lonceng | Header Beranda Wali (`HomeScreen`) | - |
| `nav_profil` | PNG | Ikon profil | Bottom bar (Wali & Penjaga) | - |
| `btn_panah` | PNG | Tombol pill panah | Referensi saja | **Jangan dipakai langsung**, buat di Compose |
| `indikator_onboarding` | PNG | 4 pil indikator | Referensi saja | **Jangan dipakai langsung**, buat di Compose agar beranimasi |
| `logo_google` | PNG | Logo Google (Sign In) | Login, Register | Untuk tombol "Lanjut dengan Google" |

## Daftar Aset Kurang (Belum Ada di `res/drawable-nodpi/`)

1. **`scan_bg`** (Foto latar tangan memegang HP versi tajam untuk layar `Pindai QR` di `03. Initial Phase oma.png`, sekitar `393x470 px` @3x format PNG/JPG). Saat ini hanya tersedia versi blur (`qr_pairing_blur.png`), sehingga layar `Pindai QR` akan menggunakan mockup rapi dengan `qr_pairing.png` + bingkai scan.
2. **Avatar khusus anggota Circle & kontak Penjaga** (`Papa`, `Mama`, `Pak Andy`, `Ibu Imas`, `Adik Adam`, `Pak RT` di `02. Initial Phase Wali.png`). Diimplementasikan di `WaliSetupScreens.kt` menggunakan avatar lingkaran (`oma_avatar` & inisial nama berwarna hangat) secara konsisten.
3. **Ikon mahkota ketua Circle** di `02. Initial Phase Wali.png`. Diimplementasikan di `CircleKeluargaScreen` menggunakan ikon bintang/mahkota (`Icons.Rounded.Star`) berwarna kuning madu.

---
**Terakhir diperbarui:** 2026-10-19 (Tahap 4 - Pemasangan aset `peta_lokasi`, `oma_avatar`, `nav_home`, `nav_lokasi`, `nav_profil` pada alur Penjaga).

