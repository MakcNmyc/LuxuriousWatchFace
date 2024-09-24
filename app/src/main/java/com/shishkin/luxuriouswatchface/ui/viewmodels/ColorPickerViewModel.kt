package com.shishkin.luxuriouswatchface.ui.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.shishkin.luxuriouswatchface.data.ColorPickerRepository
import com.shishkin.luxuriouswatchface.data.SettingsRepository
import com.shishkin.luxuriouswatchface.models.ColorPickElement
import com.shishkin.luxuriouswatchface.ui.util.toPagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ColorPickerViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    @Inject
    lateinit var colorsPickerRepository: ColorPickerRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    val settingsId = savedStateHandle.getStateFlow(SETTINGS_ID_ARGUMENT_NAME, "")

    private var _colorsData: MutableStateFlow<PagingData<ColorPickElement>?> = MutableStateFlow(null)
    var colorsData = _colorsData.asStateFlow()

    fun initColorsData(context: Context){
        if(_colorsData.value == null)
            _colorsData.value = createPagedData(context)
    }

    private fun createPagedData(context: Context) =
        colorsPickerRepository.getBackGroundsColors(context).toPagingData()

    fun saveToSetting(id: String, value: Int){
        settingsRepository.setSetting(id, value)
    }

    companion object{
        const val SETTINGS_ID_ARGUMENT_NAME = "settingsId"
    }

}