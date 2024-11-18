package com.shishkin.luxuriouswatchface.data

import android.content.Context
import android.graphics.Color
import com.shishkin.luxuriouswatchface.models.ColorPickElement
import javax.inject.Inject

class ColorPickerRepository @Inject constructor() {

    fun getBackGroundsColors(context: Context) =
         arrayListOf(
             ColorPickElement(
                 Color.WHITE,
             ),
             ColorPickElement(
                 Color.BLACK,
             ),
             ColorPickElement(
                 Color.TRANSPARENT,
             ),
            ColorPickElement(
                Color.RED,
            ),
            ColorPickElement(
                Color.BLUE,
            ),
            ColorPickElement(
                Color.GREEN,
            ),
            ColorPickElement(
                Color.YELLOW,
            ),

        )

}