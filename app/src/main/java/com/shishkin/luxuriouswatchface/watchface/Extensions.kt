package com.shishkin.luxuriouswatchface.watchface

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas

fun Canvas.drawBitmap(bitmap: Bitmap){
    this.drawBitmap(
        bitmap,
        0F,
        0F,
        null
    )
}

fun createLayeredBitmap(width: Int, height: Int, vararg layers: Bitmap) =
    Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565).also {
        val c = Canvas(it)
        for(layer in layers){
            c.drawBitmap(layer)
        }
    }

fun createColorBitmap(width: Int, height: Int, color: Int) =
    Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565).also {
        Canvas(it).drawColor(color)
    }

fun getResourceImage(context: Context, resourceId: Int, roundResourceId: Int? = null): Bitmap =
    if (roundResourceId != null && context.resources.configuration.isScreenRound) {
        roundResourceId
    } else {
        resourceId
    }.let {
        BitmapFactory.decodeResource(context.resources, it)
    }

fun Bitmap.scaleImage(width: Int, height: Int): Bitmap = Bitmap.createScaledBitmap(
        this,
        width,
        height,
        true
)


//fun createBitmapWithBackgroundColor(bitmap: Bitmap, backgroundColor: Int, width: Int, height: Int) =
//    Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565).also {
//        val c = Canvas(it)
//        c.drawColor(backgroundColor)
//        c.drawBitmap(bitmap, 0F, 0F, null)
//    }
