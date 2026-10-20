package com.chat.ketukprojectmap.data.model

data class HomeData(
    val userName: String,
    val elderName: String,
    val elderStatus: String,
    val elderActivity: String,
    val statusItems: List<StatusItem>,
    val quickActions: List<QuickAction>,
    val recentActivities: List<RecentActivity>
)

data class StatusItem(
    val type: StatusType,
    val title: String,
    val status: String,
    val statusColor: String // "HijauDaun" atau "KuningMadu"
)

enum class StatusType {
    AKTIVITAS,
    HP,
    SAPA
}

data class QuickAction(
    val type: QuickActionType,
    val title: String,
    val description: String
)

enum class QuickActionType {
    AKTIVITAS,
    SAPA,
    HP
}

data class RecentActivity(
    val id: String,
    val type: ActivityIconType,
    val title: String,
    val quote: String? = null,
    val time: String
)

enum class ActivityIconType {
    CHECK,
    BELL,
    CHAT
}

object HomeSampleData {
    val homeData = HomeData(
        userName = "Nanda",
        elderName = "Oma Diyas",
        elderStatus = "Sehat",
        elderActivity = "Aktivitas normal",
        statusItems = listOf(
            StatusItem(
                type = StatusType.AKTIVITAS,
                title = "Aktivitas",
                status = "Normal",
                statusColor = "HijauDaun"
            ),
            StatusItem(
                type = StatusType.HP,
                title = "HP",
                status = "Aktif",
                statusColor = "HijauDaun"
            ),
            StatusItem(
                type = StatusType.SAPA,
                title = "Sapa",
                status = "L1 - Perhatian",
                statusColor = "KuningMadu"
            )
        ),
        quickActions = listOf(
            QuickAction(
                type = QuickActionType.AKTIVITAS,
                title = "Aktivitas",
                description = "Lihat dan ingatkan jadwal wajib Oma"
            ),
            QuickAction(
                type = QuickActionType.SAPA,
                title = "Sapa",
                description = "Kirim pesan hangat untuk Oma"
            ),
            QuickAction(
                type = QuickActionType.HP,
                title = "HP",
                description = "Cek pulsa dan pesan dari Oma"
            )
        ),
        recentActivities = listOf(
            RecentActivity(
                id = "act_3",
                type = ActivityIconType.CHECK,
                title = "Oma Diyas minum obat",
                time = "Hari ini, 08:12"
            ),
            RecentActivity(
                id = "act_4",
                type = ActivityIconType.BELL,
                title = "Aktivitas jalan pagi",
                time = "Hari ini, 06:45"
            ),
            RecentActivity(
                id = "act_7",
                type = ActivityIconType.CHAT,
                title = "Pesan dari Oma Diyas",
                quote = "Terima kasih sudah selalu ada",
                time = "Kemarin, 19:52"
            )
        )
    )
}
