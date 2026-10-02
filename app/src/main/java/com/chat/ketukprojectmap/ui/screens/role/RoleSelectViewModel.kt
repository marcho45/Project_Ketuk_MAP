package com.chat.ketukprojectmap.ui.screens.role

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class UserRole {
    WALI,
    PENJAGA,
    OMA_OPA
}

class RoleSelectViewModel : ViewModel() {
    private val _selectedRole = MutableStateFlow<UserRole?>(null)
    val selectedRole: StateFlow<UserRole?> = _selectedRole.asStateFlow()

    fun selectRole(role: UserRole) {
        _selectedRole.value = role
    }
}
