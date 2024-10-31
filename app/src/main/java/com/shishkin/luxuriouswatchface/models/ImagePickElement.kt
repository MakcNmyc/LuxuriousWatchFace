package com.shishkin.luxuriouswatchface.models

import androidx.annotation.DrawableRes

data class ImagePickElement(
    override val id: Int,
    @DrawableRes val image: Int
) : ListElementModel<Int>