package com.shishkin.luxuriouswatchface.watchface

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

fun Canvas.drawBitmap(bitmap: Bitmap){
    this.drawBitmap(
        bitmap,
        0F,
        0F,
        null
    )
}

fun createLayeredBitmap(width: Int, height: Int, vararg layers: Bitmap): Bitmap =
    Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565).also {
        val c = Canvas(it)
        for(layer in layers){
            c.drawBitmap(layer)
        }
    }

fun createColorBitmap(width: Int, height: Int, color: Int): Bitmap =
    Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565).also {
        Canvas(it).drawColor(color)
    }

fun getBitmapFromResource(context: Context, resourceId: Int, roundResourceId: Int? = null): Bitmap =
    if (roundResourceId != null && context.resources.configuration.isScreenRound) {
        roundResourceId
    } else {
        resourceId
    }.let {
        BitmapFactory.decodeResource(context.resources, it)
    }

fun getDrawableFromResource(context: Context, resourceId: Int): Drawable =
   getBitmapFromResource(context, resourceId).toDrawable(context.resources)

fun Bitmap.toDrawable(resources: Resources): Drawable = BitmapDrawable(resources, this)

fun Bitmap.scaleImage(width: Int, height: Int): Bitmap = Bitmap.createScaledBitmap(
        this,
        width,
        height,
        true
)
