package com.chat.ketukprojectmap.ui.screens.lokasi

import androidx.lifecycle.ViewModel
import com.chat.ketukprojectmap.data.model.LokasiData
import com.chat.ketukprojectmap.data.model.LokasiSampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LokasiViewModel : ViewModel() {
    private val _lokasiData = MutableStateFlow(LokasiSampleData.data)
    val lokasiData: StateFlow<LokasiData> = _lokasiData.asStateFlow()
}
