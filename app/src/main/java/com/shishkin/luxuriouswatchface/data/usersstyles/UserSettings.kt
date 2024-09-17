package com.shishkin.luxuriouswatchface.data.usersstyles

import android.util.Log
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
    //only one allow
    var customData: CustomData,
)

// 1024 byte max size (with serialization)
@Serializable
data class CustomData(
    var topText: String = DEFAULT_TOP_TEXT,
    var bottomText: String = DEFAULT_BOTTOM_TEXT,
){
    companion object{
        const val DEFAULT_TOP_TEXT = ""
        const val DEFAULT_BOTTOM_TEXT = ""
    }
}

fun UserStyle.toUserSettings() : UserSettings{

    val settingsMap = HashMap<String, UserStyleSetting.Option>()
    this.forEach{ setting ->
//        if(setting.value is UserStyleSetting.CustomValueUserStyleSetting.CustomValueOption) settingsMap[UserSettings::customData.toId()] = setting.value
//            else settingsMap[setting.key.id.value] = setting.value
        settingsMap[setting.key.id.value] = setting.value
    }

    return UserSettings (
        backgroundColor = UserSettings::backgroundColor.optionValue(settingsMap),
        accessoriesColor = UserSettings::accessoriesColor.optionValue(settingsMap),
        customData = UserSettings::customData.optionValue(settingsMap),
    )
}

private fun <V : Any> KProperty1<UserSettings, V>.optionValue(valuesMap: Map<String, UserStyleSetting.Option>) : V {
    Log.e("customData", "optionValue map - $valuesMap id - ${this.toId()}")
    return valuesMap[this.toId()]!!.fromOption(this)
}


//private fun <V : Any> propertyValue(property: KProperty1<UserSettings, V>, optionsMap: HashMap<String, UserStyleSetting.Option>){
//        optionsMap[property.name]!!.fromOption(property.returnType.classifier as KClass<*>)
//}

//fun <V : Any> UserStyle.getValue(id: String, editorSession: EditorSession, type: KClass<V>) : V =
//    this[editorSession.userStyleSchema[UserStyleSetting.Id(id)]]!!.fromOption(type)

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
        is UserStyleSetting.CustomValueUserStyleSetting.CustomValueOption ->
            Log.e("customData", "fromOption customValue - ${customValue.decodeToString()}").let {
                Json.decodeFromString<CustomData>(customValue.decodeToString()) as V
            }
        else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toSettingsValue type")
    }

