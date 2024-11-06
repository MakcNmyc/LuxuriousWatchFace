package com.shishkin.luxuriouswatchface.watchface

import android.content.Context
import android.graphics.Color
import androidx.core.content.res.ResourcesCompat
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.data.usersstyles.SettingsSchema
import com.shishkin.luxuriouswatchface.data.usersstyles.UserSettings
import com.shishkin.luxuriouswatchface.ui.util.fromDimension
import com.shishkin.luxuriouswatchface.watchface.RenderData.HandData
import com.shishkin.luxuriouswatchface.watchface.RenderData.ImageProvider
import com.shishkin.luxuriouswatchface.watchface.RenderData.ScaledResourceImageProvider
import com.shishkin.luxuriouswatchface.watchface.RenderData.TextData

fun createRenderData(context: Context, settings: UserSettings? = null) : RenderData{

    val renderSettings = settings ?: SettingsSchema.createDefaultUserSettings()

    val font = ResourcesCompat.getFont(context, R.font.watchface__uesr_text_font)!!

    return RenderData(
        context,
        backgroundImageProvider = createBackgroundImageProvider(renderSettings),
        HandData(
            imageProvider = ScaledResourceImageProvider(
                R.drawable.hour_hand_bronze
            ),
            widthPercent = 5,
            heightPercent = 45
        ),
        HandData(
            imageProvider = ScaledResourceImageProvider(
                R.drawable.minute_hand_bronze
            ),
            widthPercent = 5,
            heightPercent = 45
        ),
        HandData(
            imageProvider = ScaledResourceImageProvider(
                R.drawable.second_hand_bronze
            ),
            widthPercent = 5,
            heightPercent = 60,
            heightPaddingPercent = 20
        ),
        TextData(
            renderSettings.customData.topText,
            R.dimen.top_text_size.fromDimension(context),
            R.dimen.top_text_y_offset.fromDimension(context).toInt(),
            font,
            Color.BLACK,
        ),
        TextData(
            renderSettings.customData.middleText,
            R.dimen.middle_text_size.fromDimension(context),
            R.dimen.middle_text_y_offset.fromDimension(context).toInt(),
            font,
            Color.BLACK,
        ),
        TextData(
            renderSettings.customData.bottomText,
            R.dimen.bottom_text_size.fromDimension(context),
            R.dimen.bottom_text_y_offset.fromDimension(context).toInt(),
            font,
            Color.BLACK
        )
    )
}

private fun createBackgroundImageProvider(settings: UserSettings) : ImageProvider =
    ImageProvider { data ->
        val colorLayer = createColorBitmap(data.width, data.height, settings.backgroundColor)
            .scaleImage(data.width, data.height)

        val indicatorsLayer = getBitmapFromResource(
            data.context,
            settings.indicatorsImage,
        ).scaleImage(data.width, data.height)

        val mainLayer = getBitmapFromResource(
            data.context,
            settings.backgroundImage,
        ).scaleImage(data.width, data.height)

        createLayeredBitmap(data.width, data.height, colorLayer, mainLayer, indicatorsLayer)
    }



