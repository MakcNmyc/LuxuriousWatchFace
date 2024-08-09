package com.shishkin.luxuriouswatchface.viewmodels

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.models.ColorsElement
import com.shishkin.luxuriouswatchface.util.asPagedList
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ColorsPickerViewModel @Inject constructor(@ApplicationContext context: Context, savedStateHandle: SavedStateHandle) : ViewModel() {

    val settingsId = savedStateHandle.getStateFlow(context.getString(R.string.settings_id_argument), "")

    val pagedList = createColorsList(context).asPagedList()

    private fun createColorsList(context: Context) : Array<ColorsElement>{

        return arrayOf(
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