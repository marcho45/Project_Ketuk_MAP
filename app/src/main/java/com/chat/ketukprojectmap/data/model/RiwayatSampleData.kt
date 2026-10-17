package com.chat.ketukprojectmap.data.model

data class RiwayatItem(
    val type: RiwayatType,
    val icon: RiwayatIcon,
    val title: String,
    val time: String
)

enum class RiwayatType {
    AKTIVITAS,
    HP,
    SAPA
}

enum class RiwayatIcon {
    CHECK,
    CHAT,
    BELL
}

object RiwayatSampleData {
    val items = listOf(
        RiwayatItem(
            type = RiwayatType.AKTIVITAS,
            icon = RiwayatIcon.CHECK,
            title = "Oma Diyas terdeteksi aktif",
            time = "2 jam yang lalu"
        ),
        RiwayatItem(
            type = RiwayatType.SAPA,
            icon = RiwayatIcon.CHAT,
            title = "Notifikasi sapa dari anda terkirim",
            time = "Hari ini, 09:17"
        ),
        RiwayatItem(
            type = RiwayatType.AKTIVITAS,
            icon = RiwayatIcon.CHECK,
            title = "Oma Diyas sudah bangun!",
            time = "Hari ini, 06:44"
        ),
        RiwayatItem(
            type = RiwayatType.HP,
            icon = RiwayatIcon.BELL,
            title = "Baterai HP 78%",
            time = "Hari ini, 06:44"
        )
    )
}
