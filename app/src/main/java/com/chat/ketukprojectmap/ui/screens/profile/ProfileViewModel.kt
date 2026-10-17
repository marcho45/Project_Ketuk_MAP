package com.chat.ketukprojectmap.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.ketukprojectmap.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut = _isLoggingOut.asStateFlow()

    fun logout(onDone: () -> Unit) {
        if (_isLoggingOut.value) return // Cegah double-click

        viewModelScope.launch {
            _isLoggingOut.value = true
            try {
                authRepository.signOut()
            } catch (_: Exception) {
                // Tetap lanjut logout walau gagal
            } finally {
                _isLoggingOut.value = false
                onDone()
            }
        }
    }
}
