package com.shishkin.luxuriouswatchface.data

import android.content.Context
import android.graphics.Color
import com.shishkin.luxuriouswatchface.models.ColorsElement
import javax.inject.Inject

class ColorPickerRepository @Inject constructor() {

    fun getBackGroundsColors(context: Context) : List<ColorsElement>{
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