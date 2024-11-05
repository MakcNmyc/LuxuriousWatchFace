package com.shishkin.luxuriouswatchface.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.shishkin.luxuriouswatchface.data.ImagePickerRepository
import com.shishkin.luxuriouswatchface.data.SettingsRepository
import com.shishkin.luxuriouswatchface.data.usersstyles.UserSettings
import com.shishkin.luxuriouswatchface.models.ImagePickElement
import com.shishkin.luxuriouswatchface.ui.util.toId
import com.shishkin.luxuriouswatchface.ui.util.toPagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagePickerViewModel @Inject constructor(savedStateHandle: SavedStateHandle): ViewModel(){

    @Inject
    lateinit var imagePickerRepository: ImagePickerRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val settingsId = savedStateHandle.getStateFlow(SETTINGS_ID_ARGUMENT_NAME, SettingsRepository.SETTINGS_ID_NOT_SET)

    private val _imagesData: MutableStateFlow<PagingData<ImagePickElement>> = MutableStateFlow(ArrayList<ImagePickElement>().toPagingData())
    val imagesData = _imagesData.asStateFlow()
    private var imagesDataIsInit = false

    fun initImagesData(){
        if (!imagesDataIsInit)
            viewModelScope.launch {
                settingsId.collectLatest { v ->
                    initImagesData(v)
                }
            }
        imagesDataIsInit = true
    }

    private fun initImagesData(settingsIdValue: String){
        when (settingsIdValue) {
            UserSettings::indicatorsImage.toId() ->
                _imagesData.value = imagePickerRepository.getIndicatorsImages().toPagingData()
            UserSettings::backgroundImage.toId() ->
                _imagesData.value = imagePickerRepository.getBackgroundImages().toPagingData()
        }
    }

    fun saveToSetting(value: Int) = settingsRepository.saveToSetting(settingsId, value)

    companion object{
        const val SETTINGS_ID_ARGUMENT_NAME = "settingsId"
    }
}