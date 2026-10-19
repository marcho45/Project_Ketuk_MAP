package com.chat.ketukprojectmap.data.dummy

/**
 * Paket Dummy Data Terpusat untuk Mode Demo UTS IF570L.
 * Seluruh nama, umur, alamat, dan aktivitas disinkronkan dengan desain Figma 01-05.
 */
object DummyData {

    // ===== 1. Akun Demo Resmi (Sandi: Ketuk123) =====
    val demoAccounts: List<DemoAccount> = listOf(
        DemoAccount(
            email = "wali@ketuk.app",
            password = "Ketuk123",
            fullName = "Nanda Valeri",
            phone = "081234567890",
            roleDbValue = "wali",
            roleLabel = "Wali (Keluarga)"
        ),
        DemoAccount(
            email = "oma@ketuk.app",
            password = "Ketuk123",
            fullName = "Sri Diyas Anjarwati",
            phone = "081298765432",
            roleDbValue = "oma_opa",
            roleLabel = "Oma/Opa"
        ),
        DemoAccount(
            email = "penjaga@ketuk.app",
            password = "Ketuk123",
            fullName = "Markolintang",
            phone = "081355557777",
            roleDbValue = "penjaga",
            roleLabel = "Penjaga Terdekat"
        )
    )

    // ===== 2. Profil Wali & Oma/Opa (Sesuai Figma 02, 04, 05) =====
    const val defaultWaliName: String = "Nanda Valeri"
    const val defaultWaliGreetingName: String = "Nanda"

    val defaultElder: Elder = Elder(
        id = "elder_oma_diyas",
        fullName = "Sri Diyas Anjarwati",
        nickname = "Oma Diyas",
        gender = ElderGender.OMA,
        age = 67, // Disamakan 67 tahun sesuai keputusan tim & Figma 02
        area = "Gading Serpong",
        address = "Jl. Syeckh Nawawi Albantani 13",
        healthBadge = "Sehat",
        safetyBadge = "Aman - Aktivitas Normal",
        activitySummary = "Aktivitas normal",
        batteryPercent = 78,
        lastLocationUpdate = "Hari ini, 18.55"
    )

    // ===== 3. Anggota Circle Keluarga (Sesuai Figma 02: 3 Anggota) =====
    val initialCircleMembers: List<CircleMember> = listOf(
        CircleMember(
            id = "circle_1",
            name = "Nanda (Wali)",
            roleLabel = "Ketua",
            isLeader = true,
            phone = "+62 812 3456 7890"
        ),
        CircleMember(
            id = "circle_2",
            name = "Papa",
            roleLabel = "Anggota",
            isLeader = false,
            phone = "+62 811 2233 4455"
        ),
        CircleMember(
            id = "circle_3",
            name = "Mama",
            roleLabel = "Anggota",
            isLeader = false,
            phone = "+62 811 6677 8899"
        )
    )

    // ===== 4. Daftar Calon Penjaga & Penjaga Utama (Sesuai Figma 02, 04, 05) =====
    val primaryGuardian: Guardian = Guardian(
        id = "guard_lintang",
        name = "Markolintang",
        displayAlias = "Lintang Marko",
        phone = "+62 813 5555 7777",
        distanceText = "170 m",
        isPrimary = true
    )

    val candidateGuardians: List<Guardian> = listOf(
        Guardian(
            id = "guard_1",
            name = "Pak Andy",
            displayAlias = "Pak Andy",
            phone = "+62 984 5674 3250",
            distanceText = "120 m"
        ),
        Guardian(
            id = "guard_2",
            name = "Ibu Imas",
            displayAlias = "Ibu Imas",
            phone = "+62 812 4987 1153",
            distanceText = "150 m"
        ),
        Guardian(
            id = "guard_3",
            name = "Adik Adam",
            displayAlias = "Adik Adam",
            phone = "+62 655 7789 0122",
            distanceText = "190 m",
            isPrimary = true // Default terpilih di Figma 02
        ),
        Guardian(
            id = "guard_4",
            name = "Pak RT",
            displayAlias = "Pak RT",
            phone = "+62 777 3442 8561",
            distanceText = "230 m"
        )
    )

    // ===== 5. Daftar Aktivitas & Riwayat (8 Item Mencakup Semua Filter & Figma 04/05) =====
    val initialActivities: List<ActivityItem> = listOf(
        ActivityItem(
            id = "act_1",
            title = "Oma Diyas terdeteksi aktif",
            type = ActivityCategory.AKTIVITAS,
            level = EscalationLevel.NORMAL,
            timestamp = "2 jam yang lalu",
            status = "Normal",
            description = "Sensor gerak HP Oma Diyas mendeteksi langkah kaki dan penggunaan layar seperti pola harian biasanya di rumah."
        ),
        ActivityItem(
            id = "act_2",
            title = "Notifikasi sapa dari anda terkirim",
            type = ActivityCategory.SAPA,
            level = EscalationLevel.L1,
            timestamp = "Hari ini, 09:17",
            status = "L1 - Perhatian",
            description = "Pesan sapaan L1 (\"Oma baik-baik saja?\") telah dikirim ke layar HP Oma Diyas karena tidak ada aktivitas selama 2 jam. Menunggu respons Oma."
        ),
        ActivityItem(
            id = "act_3",
            title = "Oma Diyas minum obat",
            type = ActivityCategory.AKTIVITAS,
            level = EscalationLevel.NORMAL,
            timestamp = "Hari ini, 08:12",
            status = "Selesai",
            description = "Oma Diyas telah mengonfirmasi pengingat jadwal minum obat pagi tepat waktu."
        ),
        ActivityItem(
            id = "act_4",
            title = "Aktivitas jalan pagi",
            type = ActivityCategory.AKTIVITAS,
            level = EscalationLevel.NORMAL,
            timestamp = "Hari ini, 06:45",
            status = "Normal",
            description = "Pergerakan langkah pagi terdeteksi di sekitar halaman rumah Jl. Syeckh Nawawi Albantani 13 selama 20 menit."
        ),
        ActivityItem(
            id = "act_5",
            title = "Oma Diyas sudah bangun!",
            type = ActivityCategory.AKTIVITAS,
            level = EscalationLevel.NORMAL,
            timestamp = "Hari ini, 06:44",
            status = "Normal",
            description = "Layar HP Oma Diyas dibuka pertama kali pagi ini dan gerakan aktivitas pagi terdeteksi normal."
        ),
        ActivityItem(
            id = "act_6",
            title = "Baterai HP 78%",
            type = ActivityCategory.HP,
            level = EscalationLevel.NORMAL,
            timestamp = "Hari ini, 06:44",
            status = "Aktif",
            description = "Kondisi baterai HP Oma Diyas berada di level aman (78%), jaringan seluler dan koneksi internet aktif."
        ),
        ActivityItem(
            id = "act_7",
            title = "Pesan dari Oma Diyas",
            type = ActivityCategory.SAPA,
            level = EscalationLevel.NORMAL,
            timestamp = "Kemarin, 19:52",
            status = "Dibalas",
            description = "Oma Diyas membalas kartu sapaan sore dari keluarga dengan kondisi sehat.",
            quote = "Terima kasih sudah selalu ada"
        ),
        // TODO(tim): Item ke-8 untuk melengkapi skenario eskalasi L2/L3 & filter HP
        ActivityItem(
            id = "act_8",
            title = "HP Oma hening - Permintaan cek penjaga",
            type = ActivityCategory.HP,
            level = EscalationLevel.L3,
            timestamp = "13 hari yang lalu",
            status = "Pengecekan berhasil",
            description = "HP Oma Diyas tidak merespons sapaan L1 dan panggilan Wali (L2). Penjaga terdekat (Markolintang) telah mengetuk pintu dan mengonfirmasi Oma Diyas aman."
        )
    )

    // ===== 6. Daftar Notifikasi (5 Item - TODO(tim): layar tanpa frame Figma khusus) =====
    val initialNotifications: List<NotificationItem> = listOf(
        NotificationItem(
            id = "notif_1",
            title = "Sapa L1: Menunggu respons Oma Diyas",
            message = "Notifikasi sapaan telah dikirim pukul 09:17. Ketuk untuk melihat detail atau menghubungi Oma.",
            timestamp = "Hari ini, 09:17",
            level = EscalationLevel.L1,
            isUnread = true,
            relatedActivityId = "act_2"
        ),
        NotificationItem(
            id = "notif_2",
            title = "Jadwal Minum Obat Terpenuhi",
            message = "Oma Diyas sudah mengonfirmasi minum obat pagi pukul 08:12.",
            timestamp = "Hari ini, 08:12",
            level = EscalationLevel.NORMAL,
            isUnread = true,
            relatedActivityId = "act_3"
        ),
        NotificationItem(
            id = "notif_3",
            title = "Oma Diyas Sudah Bangun",
            message = "Aktivitas HP pertama terdeteksi pukul 06:44 dengan baterai 78%.",
            timestamp = "Hari ini, 06:44",
            level = EscalationLevel.NORMAL,
            isUnread = false,
            relatedActivityId = "act_5"
        ),
        NotificationItem(
            id = "notif_4",
            title = "Pesan Hangat dari Oma Diyas",
            message = "\"Terima kasih sudah selalu ada\"",
            timestamp = "Kemarin, 19:52",
            level = EscalationLevel.NORMAL,
            isUnread = false,
            relatedActivityId = "act_7"
        ),
        NotificationItem(
            id = "notif_5",
            title = "Laporan Penjaga: Oma Diyas Aman",
            message = "Markolintang telah menyelesaikan pengecekan ke rumah Oma Diyas.",
            timestamp = "13 hari yang lalu",
            level = EscalationLevel.L3,
            isUnread = false,
            relatedActivityId = "act_8"
        )
    )

    // ===== 7. Riwayat Lokasi 7 Hari Terakhir (Untuk Layar Riwayat Lokasi Penjaga) =====
    // TODO(tim): Data riwayat pergerakan 7 hari terakhir (dibuka dari tombol Riwayat Lokasi di Figma 05)
    val locationHistory7Days: List<LocationHistoryItem> = listOf(
        LocationHistoryItem(
            id = "loc_1",
            dayLabel = "Hari ini",
            dateText = "19 Okt",
            timeText = "18.55 WIB",
            placeName = "Di rumah - Rumah Oma Diyas",
            address = "Jl. Syeckh Nawawi Albantani 13, Gading Serpong",
            distanceFromHome = "0 m (Di rumah)",
            statusNote = "Aman - Aktivitas Normal"
        ),
        LocationHistoryItem(
            id = "loc_2",
            dayLabel = "Hari ini",
            dateText = "19 Okt",
            timeText = "06.45 WIB",
            placeName = "Taman Kompleks Gading Serpong",
            address = "Sekitar Jl. Syeckh Nawawi Albantani, Gading Serpong",
            distanceFromHome = "85 m dari rumah",
            statusNote = "Jalan pagi rutin (20 menit)"
        ),
        LocationHistoryItem(
            id = "loc_3",
            dayLabel = "Kemarin",
            dateText = "18 Okt",
            timeText = "17.30 WIB",
            placeName = "Di rumah - Rumah Oma Diyas",
            address = "Jl. Syeckh Nawawi Albantani 13, Gading Serpong",
            distanceFromHome = "0 m (Di rumah)",
            statusNote = "Aman - Aktivitas Normal"
        ),
        LocationHistoryItem(
            id = "loc_4",
            dayLabel = "2 hari lalu",
            dateText = "17 Okt",
            timeText = "09.10 WIB",
            placeName = "Klinik Pratama Gading",
            address = "Gading Serpong Sektor 1A",
            distanceFromHome = "450 m dari rumah",
            statusNote = "Cek tensi rutin bersama Nanda"
        ),
        LocationHistoryItem(
            id = "loc_5",
            dayLabel = "3 hari lalu",
            dateText = "16 Okt",
            timeText = "07.00 WIB",
            placeName = "Warung Sayur Bu RT",
            address = "Jl. Syeckh Nawawi Albantani 05, Gading Serpong",
            distanceFromHome = "110 m dari rumah",
            statusNote = "Belanja pagi, kembali ke rumah pukul 07.35"
        ),
        LocationHistoryItem(
            id = "loc_6",
            dayLabel = "5 hari lalu",
            dateText = "14 Okt",
            timeText = "16.15 WIB",
            placeName = "Di rumah - Rumah Oma Diyas",
            address = "Jl. Syeckh Nawawi Albantani 13, Gading Serpong",
            distanceFromHome = "0 m (Di rumah)",
            statusNote = "Aman - Aktivitas Normal"
        ),
        LocationHistoryItem(
            id = "loc_7",
            dayLabel = "7 hari lalu",
            dateText = "12 Okt",
            timeText = "08.00 WIB",
            placeName = "Di rumah - Rumah Oma Diyas",
            address = "Jl. Syeckh Nawawi Albantani 13, Gading Serpong",
            distanceFromHome = "0 m (Di rumah)",
            statusNote = "Aman - Aktivitas Normal"
        )
    )

    fun findActivityById(itemId: String): ActivityItem? =
        initialActivities.find { it.id == itemId }
}
