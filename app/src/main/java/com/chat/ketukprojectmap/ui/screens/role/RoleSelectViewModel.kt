package com.chat.ketukprojectmap.ui.screens.role

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.ketukprojectmap.data.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** dbValue harus persis sama dengan enum app_role di database. */
enum class UserRole(val dbValue: String) {
    WALI("wali"),
    PENJAGA("penjaga"),
    OMA_OPA("oma_opa")
}

data class RoleSelectState(
    val isSaving: Boolean = false,
    val error: String? = null
)

class RoleSelectViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _selectedRole = MutableStateFlow<UserRole?>(null)
    val selectedRole: StateFlow<UserRole?> = _selectedRole.asStateFlow()

    private val _state = MutableStateFlow(RoleSelectState())
    val state: StateFlow<RoleSelectState> = _state.asStateFlow()

    fun selectRole(role: UserRole) {
        _selectedRole.value = role
        _state.update { it.copy(error = null) }
    }

    fun confirm(onSuccess: () -> Unit) {
        val role = _selectedRole.value ?: return
        if (_state.value.isSaving) return
        viewModelScope.launch {
            _state.update { RoleSelectState(isSaving = true) }
            try {
                repository.updateRole(role.dbValue)
                _state.update { RoleSelectState() }
                onSuccess()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val msg = e.message.orEmpty().lowercase()
                val text = if ("network" in msg || "unable to resolve" in msg || "timeout" in msg) {
                    "Tidak ada koneksi internet"
                } else {
                    "Gagal menyimpan peran, coba lagi"
                }
                _state.update { RoleSelectState(error = text) }
            }
        }
    }
}