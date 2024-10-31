package com.shishkin.luxuriouswatchface.ui.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.content.res.AppCompatResources
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.watchface.getResourceImage
import com.shishkin.luxuriouswatchface.watchface.scaleImage

interface ImageCreator {
    fun create(context: Context) : Drawable
}

class ImageResourceCreator(private val  imageId: Int): ImageCreator{
    override fun create(context: Context) =
        getResourceImage(context, imageId)
            .scaleImage(
                context.resources.getDimension(R.dimen.settings_text_with_image_width).toInt(),
                context.resources.getDimension(R.dimen.settings_text_with_image_height).toInt()
            )
            .let {
                BitmapDrawable(context.resources, it)
            }
}

fun Int.toImageResourceCreator() =
    if(this == 0) null
    else ImageResourceCreator(this)

class ColorImageCreator(private val colorValue: Int) : ImageCreator {
    override fun create(context: Context) =
        (AppCompatResources.getDrawable(context, R.drawable.round_color) as GradientDrawable)
            .apply { color = ColorStateList.valueOf(colorValue) }
}

fun Int.toColorImageCreator() =
    if(this == 0) null
    else ColorImageCreator(this)