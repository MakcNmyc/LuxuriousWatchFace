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

    private val settingsId = savedStateHandle.getStateFlow(SETTINGS_ID_ARGUMENT_NAME, SettingsRepository.SETTINGS_ID_NOT_SET)

    private val _colorsData: MutableStateFlow<PagingData<ColorPickElement>?> = MutableStateFlow(null)
    val colorsData = _colorsData.asStateFlow()

    fun initBackgroundColorsData(context: Context){
        initColorsData(context){
            colorsPickerRepository.getBackGroundsColors(context).toPagingData()
        }
    }

    private inline fun initColorsData(context: Context, dataProducer: (Context) -> PagingData<ColorPickElement>){
        if(_colorsData.value == null)
            _colorsData.value = dataProducer(context)
    }

    fun saveToSetting(value: Int) = settingsRepository.saveToSetting(settingsId, value)

    companion object{
        const val SETTINGS_ID_ARGUMENT_NAME = "settingsId"
    }

}