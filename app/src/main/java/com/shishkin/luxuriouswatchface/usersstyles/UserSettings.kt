package com.shishkin.luxuriouswatchface.usersstyles

import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting
import com.shishkin.luxuriouswatchface.util.toId
import com.shishkin.luxuriouswatchface.util.typeFromClassifier
import kotlin.reflect.KProperty1

data class UserSettings (
    val backGroundColor: Int,
)

fun UserStyle.toUserSettings() : UserSettings{

    val settingsMap = HashMap<String, UserStyleSetting.Option>()
    for(setting in this){
        settingsMap[setting.key.id.value] = setting.value
    }

    return UserSettings (
        backGroundColor = UserSettings::backGroundColor.optionValue(settingsMap),
    )
}

private fun <V : Any> KProperty1<UserSettings, V>.optionValue(valuesMap: Map<String, UserStyleSetting.Option>) =
    valuesMap[this.toId()]!!.fromOption(this)

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
        else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toSettingsValue type")
    }

