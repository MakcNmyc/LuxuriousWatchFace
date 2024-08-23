package com.shishkin.luxuriouswatchface.usersstyles

import android.content.Context
import android.graphics.Color
import com.shishkin.luxuriouswatchface.models.ColorsElement
import javax.inject.Inject

class ColorsPickerRepository @Inject constructor() {

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