package com.chat.ketukprojectmap.ui.screens.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VerificationUiState(
    val code: String = "",
    val resendSeconds: Int = 0,        // 0 = boleh kirim ulang
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isCodeComplete: Boolean
        get() = code.length == VerificationViewModel.CODE_LENGTH
    val canResend: Boolean
        get() = resendSeconds == 0 && !isLoading
}

class VerificationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(VerificationUiState())
    val uiState: StateFlow<VerificationUiState> = _uiState.asStateFlow()

    private var countdownJob: Job? = null

    /** Hanya menerima angka, maksimal CODE_LENGTH digit. */
    fun onCodeChange(newCode: String) {
        if (newCode.length <= CODE_LENGTH && newCode.all { it.isDigit() }) {
            _uiState.update { it.copy(code = newCode, errorMessage = null) }
        }
    }

    /** Verifikasi kode. onSuccess dipanggil kalau kode benar. */
    fun onConfirm(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!state.isCodeComplete || state.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            delay(800) // TODO: ganti dengan panggilan verifikasi ke backend
            val isValid = true // TODO: hasil dari backend

            if (isValid) {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } else {
                _uiState.update {
                    it.copy(isLoading = false, code = "", errorMessage = "Kode salah, coba lagi")
                }
            }
        }
    }

    /** Kirim ulang kode, lalu tunggu RESEND_DELAY_SECONDS detik sebelum boleh lagi. */
    fun onResend() {
        if (!_uiState.value.canResend) return

        // TODO: panggil backend untuk mengirim ulang kode
        _uiState.update { it.copy(code = "", errorMessage = null) }

        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (second in RESEND_DELAY_SECONDS downTo 1) {
                _uiState.update { it.copy(resendSeconds = second) }
                delay(1000)
            }
            _uiState.update { it.copy(resendSeconds = 0) }
        }
    }

    companion object {
        const val CODE_LENGTH = 4
        const val RESEND_DELAY_SECONDS = 30
    }
}