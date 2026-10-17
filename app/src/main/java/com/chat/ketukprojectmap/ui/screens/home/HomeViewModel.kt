package com.chat.ketukprojectmap.ui.screens.home

import androidx.lifecycle.ViewModel
import com.chat.ketukprojectmap.data.model.HomeData
import com.chat.ketukprojectmap.data.model.HomeSampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _homeData = MutableStateFlow(HomeSampleData.homeData)
    val homeData: StateFlow<HomeData> = _homeData.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }
}
