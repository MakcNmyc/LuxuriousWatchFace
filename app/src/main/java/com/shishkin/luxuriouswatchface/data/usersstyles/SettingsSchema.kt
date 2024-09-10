package com.shishkin.luxuriouswatchface.data.usersstyles

import android.content.res.Resources
import androidx.wear.watchface.style.UserStyleSchema
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.util.allProperties
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KProperty1

@Singleton
class SettingsSchema @Inject constructor() {

    val backgroundColor = UserStyleSettingDescription(
        UserSettings::backGroundColor,
        R.string.setting_background_color_name,
        R.string.setting_background_color_description
    )

    val accessoriesColor = UserStyleSettingDescription(
        UserSettings::accessoriesColor,
        R.string.setting_accessories_color_name,
        R.string.setting_accessories_color_description
    )

    val customData = UserStyleSettingDescription(
        UserSettings::customData,
        R.string.setting_custom_data_name,
        R.string.setting_custom_data_description
    )

//    val topText = UserStyleSettingDescription(
//        UserSettings::topText,
//        R.string.setting_top_text_name,
//        R.string.setting_top_text_description
//    )
//
//    val bottomText = UserStyleSettingDescription(
//        UserSettings::bottomText,
//        R.string.setting_bottom_text_name,
//        R.string.setting_bottom_text_description
//    )

    fun createUserStyleSchema(resources: Resources): UserStyleSchema {

        val descriptions = SettingsSchema::class.allProperties()
            .filterIsInstance<KProperty1<SettingsSchema, UserStyleSettingDescription<*>>>()
            .map { it.get(this) }

        descriptions.map { it.property }.also {
            if (!it.containsAll(UserSettings::class.allProperties())) throw NotEqualsSettingsPropertiesException()
        }

        return UserStyleSchema(descriptions.map { it.create(resources) })

//            .let { properties ->
//
//                UserStyleSchema(properties.m)
//            }
//
//        .let { settings ->
//            if(settings.filterIsInstance<KProperty1<SettingsSchema, UserStyleSettingDescription<*>>>()
//                .map { it.get(this).property }
//                .containsAll(UserSettings::class.allProperties()))
//                else
//        }
//
//        if(UserSettings::class.allProperties().containsAll(
//                SettingsSchema::class.allProperties()
//                    .filterIsInstance<KProperty1<SettingsSchema, UserStyleSettingDescription<*>>>()
//                    .map { it.get(this).property }))
//
//            else
//
//        val settingsMap = HashSet<KProperty<*>>().apply {
//            UserSettings::class.members
//                .filterIsInstance<KProperty<*>>()
//                .forEach { add(it) }
//        }
//
//        return UserStyleSchema(
//            listOf(
//                backgroundColor.create(resources),
//            )
//        )
    }

    private class NotEqualsSettingsPropertiesException : Exception("All properties in ${UserSettings::class.simpleName} class should have description in ${SettingsSchema::class.simpleName} class")

}