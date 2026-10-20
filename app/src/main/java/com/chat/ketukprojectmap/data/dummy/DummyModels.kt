package com.chat.ketukprojectmap.data.dummy

/**
 * Model data terpusat untuk aplikasi Ketuk (Mode Demo UTS & referensi UI).
 */

enum class ActivityCategory(val label: String) {
    AKTIVITAS("Aktivitas"),
    HP("HP"),
    SAPA("Sapa")
}

enum class EscalationLevel(val code: String, val label: String) {
    NORMAL("NORMAL", "Normal"),
    L1("L1", "L1 - Perhatian"),
    L2("L2", "L2 - Peringatan Wali"),
    L3("L3", "L3 - Ketuk Pintu Penjaga")
}

enum class ElderGender(val label: String) {
    OPA("Opa"),
    OMA("Oma")
}

data class DemoAccount(
    val email: String,
    val password: String,
    val fullName: String,
    val phone: String,
    val roleDbValue: String,
    val roleLabel: String
)

data class Elder(
    val id: String,
    val fullName: String,
    val nickname: String,
    val gender: ElderGender,
    val age: Int,
    val area: String,
    val address: String,
    val healthBadge: String,
    val safetyBadge: String,
    val activitySummary: String,
    val batteryPercent: Int,
    val lastLocationUpdate: String
)

data class CircleMember(
    val id: String,
    val name: String,
    val roleLabel: String,
    val isLeader: Boolean,
    val phone: String
)

data class Guardian(
    val id: String,
    val name: String,
    val displayAlias: String,
    val phone: String,
    val distanceText: String,
    val isPrimary: Boolean = false
)

data class ActivityItem(
    val id: String,
    val title: String,
    val type: ActivityCategory,
    val level: EscalationLevel,
    val timestamp: String,
    val status: String,
    val description: String,
    val quote: String? = null
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val level: EscalationLevel,
    val isUnread: Boolean = false,
    val relatedActivityId: String? = null
)

data class LocationHistoryItem(
    val id: String,
    val dayLabel: String,
    val dateText: String,
    val timeText: String,
    val placeName: String,
    val address: String,
    val distanceFromHome: String,
    val statusNote: String
)
