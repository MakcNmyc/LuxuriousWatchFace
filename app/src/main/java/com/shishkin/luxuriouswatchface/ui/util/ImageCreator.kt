package com.shishkin.luxuriouswatchface.ui.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.content.res.AppCompatResources
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.watchface.getBitmapFromResource
import com.shishkin.luxuriouswatchface.watchface.getDrawableFromResource
import com.shishkin.luxuriouswatchface.watchface.scaleImage
import com.shishkin.luxuriouswatchface.watchface.toDrawable

interface ImageCreator {
    fun create(context: Context) : Drawable
    fun create(context: Context, newWidth: Int, newHeight: Int) : Drawable = create(context)
}

class ImageResourceCreator(private val  imageId: Int): ImageCreator{
    override fun create(context: Context) =
        getDrawableFromResource(context, imageId)

    override fun create(context: Context, newWidth: Int, newHeight: Int) =
        getBitmapFromResource(context, imageId).scaleImage(newWidth, newHeight).toDrawable(context.resources)
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