package com.shishkin.luxuriouswatchface.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.data.ColorsPickerRepository
import com.shishkin.luxuriouswatchface.data.SettingsRepository
import com.shishkin.luxuriouswatchface.models.ColorsElement
import com.shishkin.luxuriouswatchface.util.toPagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ColorsPickerViewModel @Inject constructor(@ApplicationContext context: Context, savedStateHandle: SavedStateHandle) : ViewModel() {

    @Inject
    lateinit var colorsPickerRepository: ColorsPickerRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    val settingsId = savedStateHandle.getStateFlow(context.getString(R.string.settings_id_argument), "")

    private var _colorsData: MutableStateFlow<PagingData<ColorsElement>?> = MutableStateFlow(null)
    var colorsData = _colorsData.asStateFlow()

    fun initColorsData(context: Context){
        if(_colorsData.value == null)
            _colorsData.value = createPagedData(context)
    }

    private fun createPagedData(context: Context) =
        colorsPickerRepository.getBackGroundsColors(context).toPagingData()

    fun saveToSetting(id: String, value: Int){
        Log.e("customData", "ColorsPickerViewModel saveToSetting SettingsRepository - ${settingsRepository} id - $id value - $value")
        settingsRepository.setSetting(id, value)
    }
}