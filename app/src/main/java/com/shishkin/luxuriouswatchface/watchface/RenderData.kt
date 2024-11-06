package com.shishkin.luxuriouswatchface.watchface

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import kotlin.math.roundToInt

data class RenderData(
    val context: Context,
    val backgroundImageProvider: ImageProvider,
    val hourHandData: HandData,
    val minuteHandData: HandData,
    val secondHandData: HandData,
    val topTextData: TextData,
    val middleTextData: TextData,
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
                minuteHandData.init(context, data)
                secondHandData.init(context, data)

                isInit = true
            }
        }
    }

    data class RenderDataInit(
        val canvas: Canvas
    )

    open class ImageProvider(val imageProvider: (ImageProviderData) -> Bitmap){

        private lateinit var image: Bitmap

        fun init(data: ImageProviderData) {
            image = imageProvider(data)
        }

        operator fun invoke() = image
    }

    data class ImageProviderData(
        val context: Context,
        val width: Int,
        val height: Int,
    )

    class ScaledResourceImageProvider(private val resourceId: Int, private val roundResourceId: Int? = null):
        ImageProvider(
            { data ->
                getBitmapFromResource(data.context, resourceId, roundResourceId).scaleImage(data.width, data.height)
            }
        )

    data class HandData(
        val imageProvider: ScaledResourceImageProvider,
        val widthPercent: Int,
        val heightPercent: Int,
        val heightPaddingPercent: Int = 0
    ) {

        var width = 0
        var height = 0
        var heightPadding = 0

        fun init(context: Context, renderDataInit: RenderDataInit) {
            width =  computePercent(renderDataInit.canvas.width, widthPercent)
            height = computePercent(renderDataInit.canvas.height, heightPercent)
            heightPadding = computePercent(height, heightPaddingPercent)

            imageProvider.init(
                ImageProviderData(context,
                    width,
                    height))
        }

        private fun computePercent(measurement : Int, percent: Int): Int =
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

