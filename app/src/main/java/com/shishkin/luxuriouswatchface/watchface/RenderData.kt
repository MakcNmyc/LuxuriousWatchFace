package com.shishkin.luxuriouswatchface.watchface

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.util.fromDimension
import kotlin.math.roundToInt

data class RenderData(
    val context: Context,
    val backgroundImageProvider: ScaledImageProvider,
    val hourHandData: HandData,
    val topTextData: TextData,
    val bottomTextData: TextData,
){

    private var isInit = false

    fun init(renderDataInitProvider : () -> RenderDataInit){
        synchronized(this) {
            if (!isInit) {
                val renderDataInit = renderDataInitProvider()
                val canvas = renderDataInit.canvas

                backgroundImageProvider.init(context, canvas.width, canvas.height)
                hourHandData.init(context, renderDataInit)

                isInit = true
            }
        }
    }
}

data class RenderDataInit(
    val canvas: Canvas
)

fun createRenderData(context: Context) : RenderData{

    val font = ResourcesCompat.getFont(context, R.font.afterlife_regular)!!

    return RenderData(
        context,
        backgroundImageProvider = ScaledImageProvider(
            R.drawable.background_square,
            R.drawable.background_round
        ),
        HandData(
            imageProvider = ScaledImageProvider(
                R.drawable.hour_hand
            ),
            widthPercent = 10,
            heightPercent = 45
        ),
        TextData(
            "qwerty uddiopas",
            R.dimen.top_text_size.fromDimension(context),
            R.dimen.top_y_offset.fromDimension(context).toInt(),
            font,
            Color.BLACK,
        ),
        TextData(
            "zxczxczxc zxasdqwe",
            R.dimen.top_text_size.fromDimension(context),
            20,
            font,
            Color.BLACK
        )
    )
}

class ScaledImageProvider(private val resourceId: Int, private val roundResourceId: Int? = null){

    private lateinit var image: Bitmap

    fun init(context: Context, width: Int, height: Int) {
        val resources = context.resources
        val backgroundResourceId =
            if (roundResourceId != null && resources.configuration.isScreenRound) {
                roundResourceId
            } else {
                resourceId
            }
        image = BitmapFactory.decodeResource(resources, backgroundResourceId)
            .let {
                Bitmap.createScaledBitmap(it, width, height, true)
            }
    }

    operator fun invoke() = image
}

data class HandData(
    val imageProvider: ScaledImageProvider,
    val widthPercent: Int,
    val heightPercent: Int
) {

    var width = 0
    var height = 0

    fun init(context: Context, renderDataInit: RenderDataInit) {
        width =  computeMeasurement(renderDataInit.canvas.width, widthPercent)
        height = computeMeasurement(renderDataInit.canvas.height, heightPercent)

        imageProvider.init(context, width, height)
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