package com.chat.ketukprojectmap.data.model

data class LokasiData(
    val elderName: String,
    val elderAge: Int,
    val elderLocation: String,
    val status: String,
    val guardianName: String,
    val guardianDistance: String
)

object LokasiSampleData {
    val data = LokasiData(
        elderName = "Oma Diyas",
        elderAge = 72,
        elderLocation = "Gading Serpong",
        status = "Aman - Aktivitas Normal",
        guardianName = "Lintang Marko",
        guardianDistance = "170 m"
    )
}
