package com.chat.ketukprojectmap.ui.screens.riwayat

import androidx.lifecycle.ViewModel
import com.chat.ketukprojectmap.data.dummy.ActivityCategory
import com.chat.ketukprojectmap.data.dummy.ActivityItem
import com.chat.ketukprojectmap.data.dummy.DummyData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class RiwayatFilter {
    SEMUA,
    AKTIVITAS,
    HP,
    SAPA
}

class RiwayatViewModel : ViewModel() {
    private val _selectedFilter = MutableStateFlow(RiwayatFilter.SEMUA)
    val selectedFilter: StateFlow<RiwayatFilter> = _selectedFilter.asStateFlow()

    private val _allItems = MutableStateFlow(DummyData.initialActivities)

    private val _filteredItems = MutableStateFlow(DummyData.initialActivities)
    val filteredItems: StateFlow<List<ActivityItem>> = _filteredItems.asStateFlow()

    fun selectFilter(filter: RiwayatFilter) {
        _selectedFilter.value = filter
        updateFilteredItems()
    }

    private fun updateFilteredItems() {
        val filtered = when (_selectedFilter.value) {
            RiwayatFilter.SEMUA -> _allItems.value
            RiwayatFilter.AKTIVITAS -> _allItems.value.filter { it.type == ActivityCategory.AKTIVITAS }
            RiwayatFilter.HP -> _allItems.value.filter { it.type == ActivityCategory.HP }
            RiwayatFilter.SAPA -> _allItems.value.filter { it.type == ActivityCategory.SAPA }
        }
        _filteredItems.value = filtered
    }
}

