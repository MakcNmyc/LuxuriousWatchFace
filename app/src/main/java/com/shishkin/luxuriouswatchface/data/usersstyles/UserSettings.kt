package com.shishkin.luxuriouswatchface.data.usersstyles

import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting
import com.shishkin.luxuriouswatchface.ui.util.toId
import com.shishkin.luxuriouswatchface.ui.util.typeFromClassifier
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.reflect.KProperty1

data class UserSettings (
    var backgroundColor: Int,
    var accessoriesColor: Int,
    var backgroundImage: Int,
    //only one custom type allow
    var customData: CustomData,
)

// 1024 byte max size (with serialization)
@Serializable
data class CustomData(
    //max length = R.string.watchface_max_string_size
    var topText: String = "",
    var bottomText: String = "",
)

fun UserStyle.toUserSettings() : UserSettings{

    val settingsMap = HashMap<String, UserStyleSetting.Option>()
    this.forEach{ setting ->
        settingsMap[setting.key.id.value] = setting.value
    }

    return UserSettings (
        backgroundColor = UserSettings::backgroundColor.optionValue(settingsMap),
        accessoriesColor = UserSettings::accessoriesColor.optionValue(settingsMap),
        customData = UserSettings::customData.optionValue(settingsMap),
        backgroundImage = UserSettings::backgroundImage.optionValue(settingsMap),
    )
}

private fun <V : Any> KProperty1<UserSettings, V>.optionValue(valuesMap: Map<String, UserStyleSetting.Option>) : V =
    valuesMap[this.toId()]!!.fromOption(this)

@Suppress("UNCHECKED_CAST")
private fun <V : Any> UserStyleSetting.Option.fromOption(toProperty: KProperty1<UserSettings, V>): V =
    when (this) {
        is UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption ->
            when (toProperty.typeFromClassifier()) {
                Int::class -> value.toInt() as V
                Long::class -> value as V
                else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toSettingsValue LongRangeOption type")
            }
        is UserStyleSetting.BooleanUserStyleSetting.BooleanOption -> value as V
        is UserStyleSetting.CustomValueUserStyleSetting.CustomValueOption -> Json.decodeFromString<CustomData>(customValue.decodeToString()) as V
        else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toSettingsValue type")
    }

