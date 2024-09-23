package com.shishkin.luxuriouswatchface.watchface

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import kotlin.math.roundToInt

data class RenderData(
    val context: Context,
    val backgroundImageProvider: ScaledImageProvider,
    val hourHandData: HandData,
    val minuteHandColor: Int,
    val topTextData: TextData,
    val bottomTextData: TextData,
){

    private var isInit = false

    fun init(data: RenderDataInit){
        synchronized(this) {
            if (!isInit) {
                val canvas = data.canvas

                backgroundImageProvider.init(
                    ImageProviderData(
                        context,
                        canvas.width,
                        canvas.height
                    )
                )
                hourHandData.init(context, data)

                isInit = true
            }
        }
    }

    data class RenderDataInit(
        val canvas: Canvas
    )

    open class ScaledImageProvider(val imageProvider: (ImageProviderData) -> Bitmap){

        private lateinit var image: Bitmap

        fun init(data: ImageProviderData) {
            image = imageProvider(data).scaleImage(data.width, data.height)
        }

        operator fun invoke() = image
    }

    data class ImageProviderData(
        val context: Context,
        val width: Int,
        val height: Int,
    )

    class ScaledResourceImageProvider(private val resourceId: Int, private val roundResourceId: Int? = null):
        ScaledImageProvider(
            { data ->
                getResourceImage(data.context, resourceId, roundResourceId)
            }
        )

    data class HandData(
        val imageProvider: ScaledResourceImageProvider,
        val widthPercent: Int,
        val heightPercent: Int
    ) {

        var width = 0
        var height = 0

        fun init(context: Context, renderDataInit: RenderDataInit) {
            width =  computeMeasurement(renderDataInit.canvas.width, widthPercent)
            height = computeMeasurement(renderDataInit.canvas.height, heightPercent)

            imageProvider.init(
                ImageProviderData(context,
                    width,
                    height))
        }

        private fun computeMeasurement(measurement : Int, percent: Int): Int =
            ((measurement * percent).toFloat() / 100).roundToInt()
    }

    data class TextData(
        val text: String,
        val textSize: Float,
        val yCenterOffset: Int,
        val font: Typeface,
        val color: Int
    )
}

