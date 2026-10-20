# ATURAN DOKUMENTASI DAN ALUR KERJA AGENT

## Sebelum mengerjakan tugas apa pun
- Baca docs/PROJECT_SUMMARY.md, docs/SCREENS.md, dan 10 entri terbaru docs/CHANGELOG_HISTORY.md.
- Baca docs/ASSETS.md kalau tugas menyangkut gambar/ikon/font.
- Fokus ke flavor "demo" (UTS). Jangan ubah flavor "full" kecuali diminta.

## Setelah setiap perubahan yang berhasil
- Tambahkan entri BARU di bagian PALING ATAS docs/CHANGELOG_HISTORY.md dengan format:
  ### [YYYY-MM-DD HH:mm] Judul singkat
  - Alasan/permintaan:
  - File diubah: (path dan ringkasan)
  - Dampak ke layar/fitur/rubrik:
  - Hasil build/lint:
- Update docs/SCREENS.md kalau ada layar ditambah, dihapus, atau berubah perilaku/navigasi.
- Update docs/ASSETS.md kalau ada aset ditambah/diganti.
- Update docs/PROJECT_SUMMARY.md kalau arsitektur, flavor, theme, atau fitur utama berubah.
- Jangan menghapus entri lama. Hanya menambah.
- Jangan menulis kredensial atau isi local.properties ke dokumen apa pun.

## Aturan umum
- Sebelum selesai, jalankan build yang relevan dan tulis hasilnya di changelog.
- Kalau tugas hanya membaca/audit (tanpa edit), tidak perlu update dokumen.
