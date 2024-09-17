package com.shishkin.luxuriouswatchface.watchface

import android.content.Context
import android.graphics.Color
import androidx.core.content.res.ResourcesCompat
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.data.usersstyles.CustomData
import com.shishkin.luxuriouswatchface.data.usersstyles.SettingsSchema
import com.shishkin.luxuriouswatchface.data.usersstyles.UserSettings
import com.shishkin.luxuriouswatchface.ui.util.fromDimension

fun createRenderData(context: Context, schema: SettingsSchema, settings: UserSettings? = null) : RenderData{

    val font = ResourcesCompat.getFont(context, R.font.afterlife_regular)!!

    return RenderData(
        context,
        backgroundImageProvider = createBackgroundImageProvider(),
        HandData(
            imageProvider = ScaledResourceImageProvider(
                R.drawable.hour_hand
            ),
            widthPercent = 10,
            heightPercent = 45
        ),
        settings?.backgroundColor ?: schema.backgroundColor.defaultValue,
        TextData(
            settings?.customData?.topText ?:  CustomData.DEFAULT_TOP_TEXT,
            R.dimen.top_text_size.fromDimension(context),
            R.dimen.top_y_offset.fromDimension(context).toInt(),
            font,
            Color.BLACK,
        ),
        TextData(
            settings?.customData?.topText ?:  CustomData.DEFAULT_BOTTOM_TEXT,
            R.dimen.top_text_size.fromDimension(context),
            20,
            font,
            Color.BLACK
        )
    )
}

private fun createBackgroundImageProvider() : ScaledImageProvider =
     ScaledImageProvider{ data ->
        val colorLayer = createColorBitmap(data.width, data.height, Color.BLUE)
            .scaleImage(data.width, data.height)

         val testLayer = getResourceImage(
             data.context,
             R.drawable.red_ring
         ).scaleImage(data.width, data.height)

        val mainLayer = getResourceImage(
            data.context,
            R.drawable.background_square,
            R.drawable.background_round
        ).scaleImage(data.width / 2, data.height / 2)

        createLayeredBitmap(data.width, data.height, colorLayer, testLayer, mainLayer)
    }



