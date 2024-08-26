package com.shishkin.luxuriouswatchface.usersstyles

import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting
import kotlin.reflect.KClass

data class UserSettings (
    val backGroundColor: Int,
){
    companion object{
        const val BACKGROUND_COLOR = "background_color"
    }
}

fun UserStyle.toUserSettings() : UserSettings{

    val settingsMap = HashMap<String, UserStyleSetting.Option>()
    for(setting in this){
        settingsMap[setting.key.id.value] = setting.value
    }

    return UserSettings (
        settingsMap[UserSettings.BACKGROUND_COLOR]!!.fromOption(Int::class),
    )
}

//private fun <V : Any> propertyValue(property: KProperty1<UserSettings, V>, optionsMap: HashMap<String, UserStyleSetting.Option>){
//        optionsMap[property.name]!!.fromOption(property.returnType.classifier as KClass<*>)
//}

//fun <V : Any> UserStyle.getValue(id: String, editorSession: EditorSession, type: KClass<V>) : V =
//    this[editorSession.userStyleSchema[UserStyleSetting.Id(id)]]!!.fromOption(type)

@Suppress("UNCHECKED_CAST")
private fun <V : Any> UserStyleSetting.Option.fromOption(to: KClass<V>): V =
    when (this) {
        is UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption ->
            when (to) {
                Int::class -> value.toInt() as V
                Long::class -> value as V
                else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toSettingsValue LongRangeOption type")
            }
        is UserStyleSetting.BooleanUserStyleSetting.BooleanOption -> value as V
        else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toSettingsValue type")
    }

