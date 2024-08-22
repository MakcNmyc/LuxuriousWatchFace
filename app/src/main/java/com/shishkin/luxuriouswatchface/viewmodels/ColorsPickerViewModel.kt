package com.shishkin.luxuriouswatchface.viewmodels

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.models.ColorsElement
import com.shishkin.luxuriouswatchface.util.toPagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ColorsPickerViewModel @Inject constructor(@ApplicationContext context: Context, savedStateHandle: SavedStateHandle) : ViewModel() {

    val settingsId = savedStateHandle.getStateFlow(context.getString(R.string.settings_id_argument), "")

    private var _colorsData: MutableStateFlow<PagingData<ColorsElement>?> = MutableStateFlow(null)
    var colorsData = _colorsData.asStateFlow()

    fun initColorsData(context: Context){
        if(_colorsData.value == null)
            _colorsData.value = createPagedData(context)
    }

    private fun createPagedData(context: Context) =
        getData(context).toPagingData()

    private fun getData(context: Context) : List<ColorsElement>{
        return arrayListOf(
            ColorsElement(
                Color.RED,
            ),
            ColorsElement(
                Color.BLUE,
            ),
            ColorsElement(
                Color.GREEN,
            ),
            ColorsElement(
                Color.YELLOW,
            ),
        )
    }

}