package com.shishkin.luxuriouswatchface.ui.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.shishkin.luxuriouswatchface.data.ImagePickerRepository
import com.shishkin.luxuriouswatchface.data.SettingsRepository
import com.shishkin.luxuriouswatchface.models.ImagePickElement
import com.shishkin.luxuriouswatchface.ui.util.toPagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ImagePickerViewModel @Inject constructor(savedStateHandle: SavedStateHandle): ViewModel(){

    @Inject
    lateinit var imagePickerRepository: ImagePickerRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val settingsId = savedStateHandle.getStateFlow(SETTINGS_ID_ARGUMENT_NAME, SettingsRepository.SETTINGS_ID_NOT_SET)

    private val _imagesData: MutableStateFlow<PagingData<ImagePickElement>?> = MutableStateFlow(null)
    val imagesData = _imagesData.asStateFlow()

    fun initBackgroundImagesData(context: Context){
        initImagesData(context) {
            imagePickerRepository.getBackgroundImages(context).toPagingData()
        }
    }

    private inline fun initImagesData(context: Context, dataProducer: (Context) -> PagingData<ImagePickElement>){
        if(_imagesData.value == null)
            _imagesData.value = dataProducer(context)
    }

    fun saveToSetting(value: Int) = settingsRepository.saveToSetting(settingsId, value)

    companion object{
        const val SETTINGS_ID_ARGUMENT_NAME = "settingsId"
    }
}