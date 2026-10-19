package com.chat.ketukprojectmap.ui.screens.guardian

import androidx.lifecycle.ViewModel
import com.chat.ketukprojectmap.data.dummy.ActivityCategory
import com.chat.ketukprojectmap.data.dummy.ActivityItem
import com.chat.ketukprojectmap.data.dummy.EscalationLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class GuardianCheckStatus {
    IDLE,
    SAFE_CONFIRMED,
    HELP_REQUESTED
}

data class GuardianHomeUiState(
    val checkStatus: GuardianCheckStatus = GuardianCheckStatus.IDLE,
    val historyItems: List<ActivityItem> = listOf(
        ActivityItem(
            id = "guard_act_initial",
            title = "Pengecekan berhasil - Oma Diyas",
            type = ActivityCategory.HP,
            level = EscalationLevel.NORMAL,
            timestamp = "13 hari yang lalu",
            status = "Pengecekan berhasil",
            description = "Penjaga Markolintang telah mengecek ke rumah Oma Diyas dan memastikan kondisi aman."
        )
    ),
    val unreadNotificationsCount: Int = 2
)

class GuardianViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GuardianHomeUiState())
    val uiState: StateFlow<GuardianHomeUiState> = _uiState.asStateFlow()

    fun confirmSafe(onDone: (String) -> Unit = {}) {
        val message = "Simulasi Mockup: Status 'Oma aman' terkirim ke Nanda (Wali)"
        _uiState.update { current ->
            val newItem = ActivityItem(
                id = "guard_act_${System.currentTimeMillis()}",
                title = "Pengecekan selesai - Oma Aman",
                type = ActivityCategory.HP,
                level = EscalationLevel.NORMAL,
                timestamp = "Baru saja",
                status = "Aman",
                description = "Penjaga Markolintang mengonfirmasi Oma Diyas dalam keadaan aman."
            )
            current.copy(
                checkStatus = GuardianCheckStatus.SAFE_CONFIRMED,
                historyItems = listOf(newItem) + current.historyItems
            )
        }
        onDone(message)
    }

    fun requestHelp(onDone: (String) -> Unit = {}) {
        val message = "Simulasi Mockup: Peringatan darurat dikirim ke Nanda (Wali) & Kontak Darurat!"
        _uiState.update { current ->
            val newItem = ActivityItem(
                id = "guard_act_${System.currentTimeMillis()}",
                title = "Bantuan darurat diminta",
                type = ActivityCategory.HP,
                level = EscalationLevel.L3,
                timestamp = "Baru saja",
                status = "Perlu Bantuan",
                description = "Penjaga Markolintang melaporkan Oma membutuhkan bantuan medis/keluarga segera."
            )
            current.copy(
                checkStatus = GuardianCheckStatus.HELP_REQUESTED,
                historyItems = listOf(newItem) + current.historyItems
            )
        }
        onDone(message)
    }
}
