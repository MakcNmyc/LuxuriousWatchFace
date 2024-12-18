package com.shishkin.luxuriouswatchface.data.usersstyles

import android.content.res.Resources
import android.graphics.Color
import androidx.wear.watchface.style.UserStyleSchema
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.ui.util.allProperties
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KProperty1

@Singleton
class SettingsSchema @Inject constructor() {

    val backgroundColor = UserStyleSettingDescription(
        UserSettings::backgroundColor,
        R.string.setting_background_color_name,
        R.string.setting_background_color_description,
        DEFAULT_BACKGROUND_COLOR
    )

    val backgroundImage = UserStyleSettingDescription(
        UserSettings::backgroundImage,
        R.string.setting_background_image,
        R.string.setting_background_image_description,
        DEFAULT_BACKGROUND_IMAGE
    )

    val indicatorsImage = UserStyleSettingDescription(
        UserSettings::indicatorsImage,
        R.string.setting_indicators_image_name,
        R.string.setting_indicators_image_description,
        DEFAULT_INDICATORS_IMAGE
    )

    val customData = UserStyleSettingDescription(
        UserSettings::customData,
        R.string.setting_custom_data_name,
        R.string.setting_custom_data_description,
        CustomData(DEFAULT_TOP_TEXT, DEFAULT_BOTTOM_TEXT)
    )

    fun createUserStyleSchema(resources: Resources): UserStyleSchema {

        val descriptions = SettingsSchema::class.allProperties()
            .filterIsInstance<KProperty1<SettingsSchema, UserStyleSettingDescription<*>>>()
            .map { it.get(this) }

        descriptions.map { it.property }.also {
            if (!it.containsAll(UserSettings::class.allProperties())) throw NotEqualsSettingsPropertiesException()
        }

        return UserStyleSchema(descriptions.map { it.create(resources) })
    }

    private class NotEqualsSettingsPropertiesException : Exception("All properties in ${UserSettings::class.simpleName} class should have description in ${SettingsSchema::class.simpleName} class")

    // There store default settings
    companion object{
        val DEFAULT_BACKGROUND_IMAGE = R.drawable.gold_background
        val DEFAULT_INDICATORS_IMAGE = R.drawable.indicators_silver
        val DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT

        const val DEFAULT_TOP_TEXT = ""
        const val DEFAULT_BOTTOM_TEXT = ""

        fun createDefaultUserSettings() =
            UserSettings(
                DEFAULT_BACKGROUND_COLOR,
                DEFAULT_BACKGROUND_IMAGE,
                DEFAULT_INDICATORS_IMAGE,
                createDefaultCustomData(),
            )

        private fun createDefaultCustomData() =
            CustomData(
                DEFAULT_TOP_TEXT,
                DEFAULT_BOTTOM_TEXT,
            )
    }

}