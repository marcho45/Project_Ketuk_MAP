# Prompt untuk Cline

Cara pakai: salin satu prompt, tempel ke Cline, mulai dengan mode **Plan**, baca rencananya, lalu ubah ke **Act**.
Kerjakan berurutan. Commit ke Git setelah tiap prompt berhasil di-build.

## Prompt 0: Fondasi (kerjakan dulu, tanpa layar)

```
Baca .clinerules, docs/SCREENS.md, docs/ASSETS.md, lalu lihat semua gambar di docs/figma/.

Tugas: siapkan FONDASI saja, jangan buat layar dulu.
1. Tambah dependency yang diizinkan di .clinerules (navigation-compose, material-icons-extended, lifecycle-viewmodel-compose).
   Pakai version catalog kalau project memakainya. Jangan ubah versi lain.
2. Font Inter sudah ada di res/font. Buat FontFamily Inter di ui/theme/Type.kt dan pasang ke Typography Material 3.
   Tambahkan style khusus untuk layar lansia (36sp bold, 24sp medium, 28sp bold).
3. Isi ui/theme/Color.kt dengan palet dari .clinerules, dan ubah Theme.kt supaya memakai palet itu (light only, tanpa dynamic color).
   Ubah nama theme bawaan template menjadi KetukTheme dan perbarui MainActivity.
4. Buat komponen dasar di ui/components: KetukButton (pill, terakota), KetukSecondaryButton, KetukTextField, StatusChip.
5. Buat navigation/Routes.kt dan KetukNavHost.kt dengan rute: splash, onboarding, login, role, home. Isi tiap rute dengan layar kosong sementara.
6. Pastikan build berhasil.

Jangan sentuh AndroidManifest, jangan buat layar sungguhan, jangan tambah library lain.
Di akhir laporkan file yang dibuat dan hal yang perlu saya putuskan.
```

## Prompt 1: Loading + Onboarding

```
Baca .clinerules dan docs/SCREENS.md bagian 1 sampai 5, dan lihat docs/figma/initial_phase.png (layar 1 sampai 5).
Buat layar Loading dan Onboarding (4 halaman) sesuai desain, memakai aset dari drawable-nodpi sesuai docs/ASSETS.md.

- Loading: animasi logo muncul perlahan, otomatis lanjut ke Onboarding setelah sekitar 2 detik.
- Onboarding: HorizontalPager, panel krem melengkung menimpa gambar atas, indikator 4 pil yang dibuat di kode dan beranimasi,
  Skip (ke Login) dan tombol panah, halaman terakhir tombol "Mulai". Halaman 3 berisi daftar 4 langkah yang muncul bertahap.
- Salin teks persis dari docs/SCREENS.md.
- Jangan pakai gambar indikator_onboarding atau btn_panah, buat di Compose.

Tambahkan @Preview. Pastikan build berhasil. Lapor: file yang dibuat dan yang belum sesuai desain.
```

## Prompt 2: Login + Pilih Peran

```
Baca .clinerules dan docs/SCREENS.md bagian 6 dan 7, dan lihat docs/figma/initial_phase.png (layar 6) serta docs/figma/select_role_page.png.
Buat layar Login dan Pilih Peran sesuai desain.

- Login: tampilan saja, tombol Masuk dan Google hanya lanjut ke Pilih Peran. Ikon mata untuk tampil/sembunyi password.
- Pilih Peran: tiga kartu berwarna, pilihan ditandai dengan animasi, tombol "Lanjutkan" aktif setelah memilih.
  Peran Wali menuju Home. Dua peran lain menuju layar placeholder sederhana (judul + tombol kembali).
- Judul kartu 2 di Figma kemungkinan salah salin: tulis "Saya penjaga" dan tandai dengan komentar TODO.
- Pakai KetukTextField dan KetukButton dari ui/components. Tidak boleh hardcode warna.

Tambahkan @Preview. Pastikan build berhasil. Lapor hasilnya.
```

## Prompt 3: Home Wali + Bottom bar

```
Baca .clinerules dan docs/SCREENS.md bagian 8, dan lihat docs/figma/home_page_wali.png.
Buat layar Home Wali dengan bottom bar 4 tab (Home, Riwayat, Notifikasi, Profil).

- Gunakan aset nav_home, nav_riwayat, nav_notifikasi, nav_profil untuk ikon tab. Tab aktif punya latar pil yang berpindah dengan animasi.
- Data contoh di ViewModel (nama wali, nama Oma, status "Sehat"), bukan hardcode di composable.
- Tab selain Home: layar placeholder sederhana.
- Kartu "Status hari ini": 3 baris data contoh (Aktivitas, HP, Sapa).
- Tombol lonceng di header membuka tab Notifikasi.

Tambahkan @Preview. Pastikan build berhasil. Lapor hasilnya.
```

## Prompt berikutnya
Setelah layar lain didesain di Figma, kirim screenshot-nya ke saya, nanti saya buatkan bagian baru untuk docs/SCREENS.md dan prompt-nya.
