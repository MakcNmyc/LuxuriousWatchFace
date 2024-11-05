package com.shishkin.luxuriouswatchface.data

import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.models.ImagePickElement
import javax.inject.Inject

class ImagePickerRepository @Inject constructor(){

    fun getBackgroundImages() =
        arrayListOf(
            ImagePickElement(
                R.drawable.silver_background,
            ),
            ImagePickElement(
                R.drawable.gold_background,
            ),
            ImagePickElement(
                R.drawable.blue_background,
            ),
            ImagePickElement(
                R.drawable.green_background,
            ),
            ImagePickElement(
                R.drawable.purple_background,
            ),
        )

    fun getIndicatorsImages() =
        arrayListOf(
            ImagePickElement(
                R.drawable.indicators_gold,
            ),
            ImagePickElement(
                R.drawable.indicators_silver,
            ),
        )
}