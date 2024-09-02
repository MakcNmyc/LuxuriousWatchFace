package com.shishkin.luxuriouswatchface.usersstyles

import android.content.res.Resources
import androidx.wear.watchface.style.UserStyleSchema
import com.shishkin.luxuriouswatchface.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsSchema @Inject constructor() {

    val backgroundColor = UserStyleSettingDescription(
        UserSettings::backGroundColor,
        R.string.setting_background_color_name,
        R.string.setting_background_color_description
    )

    fun createUserStyleSchema(resources: Resources) =
        UserStyleSchema(
            listOf(
                backgroundColor.create(resources),
            )
        )
}