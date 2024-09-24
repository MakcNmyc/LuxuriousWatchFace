package com.shishkin.luxuriouswatchface.data

import android.content.Context
import android.graphics.Color
import com.shishkin.luxuriouswatchface.models.ColorElement
import javax.inject.Inject

class ColorPickerRepository @Inject constructor() {

    fun getBackGroundsColors(context: Context) : List<ColorElement>{
        return arrayListOf(
            ColorElement(
                Color.RED,
            ),
            ColorElement(
                Color.BLUE,
            ),
            ColorElement(
                Color.GREEN,
            ),
            ColorElement(
                Color.YELLOW,
            ),
        )
    }

}