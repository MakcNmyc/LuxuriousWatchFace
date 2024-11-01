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

fun createRenderData(context: Context, schema: SettingsSchema, settings: UserSettings? = null) : RenderData{

    val renderSettings = settings ?: SettingsSchema.createDefaultUserSettings()

    val font = ResourcesCompat.getFont(context, R.font.afterlife_regular)!!

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
            R.dimen.top_y_offset.fromDimension(context).toInt(),
            font,
            Color.BLACK,
        ),
        TextData(
            renderSettings.customData.topText,
            R.dimen.top_text_size.fromDimension(context),
            20,
            font,
            Color.BLACK
        )
    )
}

private fun createBackgroundImageProvider(settings: UserSettings) : ImageProvider =
     ImageProvider{ data ->
        val colorLayer = createColorBitmap(data.width, data.height, Color.BLUE)
            .scaleImage(data.width, data.height)

        val mainLayer = getResourceImage(
            data.context,
            settings.backgroundImage,
        ).scaleImage(data.width, data.height)

        createLayeredBitmap(data.width, data.height, colorLayer, mainLayer)
    }



