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
        Color.BLACK
    )

    val accessoriesColor = UserStyleSettingDescription(
        UserSettings::accessoriesColor,
        R.string.setting_accessories_color_name,
        R.string.setting_accessories_color_description,
        Color.BLUE
    )

    val customData = UserStyleSettingDescription(
        UserSettings::customData,
        R.string.setting_custom_data_name,
        R.string.setting_custom_data_description,
        CustomData()
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

}