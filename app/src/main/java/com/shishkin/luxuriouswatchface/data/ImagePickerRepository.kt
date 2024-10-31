package com.shishkin.luxuriouswatchface.data

import android.content.Context
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.models.ImagePickElement
import javax.inject.Inject

class ImagePickerRepository @Inject constructor(){

    fun getBackgroundImages(context: Context) =
        arrayListOf(
            ImagePickElement(
                R.drawable.red_ring,
                R.drawable.red_ring,
            ),
            ImagePickElement(
                R.drawable.background,
                R.drawable.background,
            ),
            ImagePickElement(
                R.drawable.preview,
                R.drawable.preview,
            ),
            ImagePickElement(
                R.drawable.splash_icon,
                R.drawable.splash_icon,
            ),
        )
}