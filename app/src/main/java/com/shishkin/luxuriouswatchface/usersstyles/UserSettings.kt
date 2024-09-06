package com.shishkin.luxuriouswatchface.usersstyles

import android.util.Log
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting
import com.shishkin.luxuriouswatchface.util.toId
import com.shishkin.luxuriouswatchface.util.typeFromClassifier
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.reflect.KProperty1

data class UserSettings (
    val backGroundColor: Int,
    val accessoriesColor: Int,
    //only one allow
    val customData: CustomData,
)

// 1024 byte max size (with serialization)
@Serializable
data class CustomData(
    val topText: String = "",
    val bottomText: String = "",
){
    constructor(propertyMap: Map<String, String>, copeObj: CustomData) : this(
        propertyMap["topText"] ?: copeObj.topText,
        propertyMap["bottomText"] ?: copeObj.bottomText,
    )

    constructor(propertyName: String, propertyValue: String, copeObj: CustomData) : this(
        mapOf(propertyName to propertyValue),
        copeObj
    )
}

fun UserStyle.toUserSettings() : UserSettings{

    val settingsMap = HashMap<String, UserStyleSetting.Option>()
    this.forEach{ setting ->
//        if(setting.value is UserStyleSetting.CustomValueUserStyleSetting.CustomValueOption) settingsMap[UserSettings::customData.toId()] = setting.value
//            else settingsMap[setting.key.id.value] = setting.value
        settingsMap[setting.key.id.value] = setting.value
    }

    return UserSettings (
        backGroundColor = UserSettings::backGroundColor.optionValue(settingsMap),
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

