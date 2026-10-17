package com.chat.ketukprojectmap.ui.screens.riwayat

import androidx.lifecycle.ViewModel
import com.chat.ketukprojectmap.data.model.RiwayatItem
import com.chat.ketukprojectmap.data.model.RiwayatSampleData
import com.chat.ketukprojectmap.data.model.RiwayatType
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

    private val _allItems = MutableStateFlow(RiwayatSampleData.items)
    
    val filteredItems: StateFlow<List<RiwayatItem>> = MutableStateFlow(
        RiwayatSampleData.items
    )

    fun selectFilter(filter: RiwayatFilter) {
        _selectedFilter.value = filter
        updateFilteredItems()
    }

    private fun updateFilteredItems() {
        val filtered = when (_selectedFilter.value) {
            RiwayatFilter.SEMUA -> _allItems.value
            RiwayatFilter.AKTIVITAS -> _allItems.value.filter { it.type == RiwayatType.AKTIVITAS }
            RiwayatFilter.HP -> _allItems.value.filter { it.type == RiwayatType.HP }
            RiwayatFilter.SAPA -> _allItems.value.filter { it.type == RiwayatType.SAPA }
        }
        (filteredItems as MutableStateFlow).value = filtered
    }
}
