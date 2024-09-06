package com.shishkin.luxuriouswatchface.usersstyles

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.UserStyleSetting
import com.shishkin.luxuriouswatchface.util.toId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SettingsEditor @Inject constructor() {

    private lateinit var editorSession: EditorSession

    private val _settingsHolder = MutableStateFlow<UserSettings?>(null)
    val settingsHolder = _settingsHolder.asStateFlow()

    private val _state = MutableStateFlow(State.Loading)
    val state = _state.asStateFlow()

    fun initSession(activity: AppCompatActivity){
        activity.lifecycleScope.launch{
            editorSession = EditorSession.createOnWatchEditorSession(
                activity = activity
            )

            editorSession.userStyle.collectLatest{ userStyle ->
                _state.value = State.Loading

                _settingsHolder.value = userStyle.toUserSettings()

                _state.value = State.Ready
            }
        }
    }

    private fun set(id: String, userStyleOption: UserStyleSetting.Option){
        val mutableUserStyle = editorSession.userStyle.value.toMutableUserStyle()
        Log.e("customData", "editor set id - $id userStyleSchema - ${editorSession.userStyleSchema} userStyleOption - $userStyleOption")
        mutableUserStyle[editorSession.userStyleSchema[UserStyleSetting.Id(id)]!!] = userStyleOption
        editorSession.userStyle.value = mutableUserStyle.toUserStyle()
    }

//    fun <V : Any> set(property: KProperty1<UserSettings, V>, value: V){
//        set(property.toId(), value.toOption())
//    }

    fun <V : Any> set(id: String, value: V) {
        if (value is String) {
            val s = settingsHolder.value ?: return
            val c: CustomData = CustomData(id, value, s.customData)
            set(UserSettings::customData.toId(), c.toOption())
        } else set(id, value.toOption())
    }

    private fun <V : Any> V.toOption() : UserStyleSetting.Option =
        when (this) {
            is Int -> UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption(this.toLong())
            is Long -> UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption(this)
            is CustomData -> UserStyleSetting.CustomValueUserStyleSetting.CustomValueOption(
                Json.encodeToString(this as CustomData).toByteArray()
            )
            else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toOption type")
        }



//    private fun UserStyleSetting.Option.fromOption(description: UserStyleSettingDescription<*>) : Any =
//        when(this){
//            is UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption ->
//                when (description.property.javaClass) {
//                    Int::class.java -> value.toInt()
//                    Long::class.java -> value
//                    else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toSettingsValue LongRangeOption type")
//                }
//            else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toSettingsValue type")
//        }

//    private fun <V> fromOption(option: V) =
//        when (option) {
//            is UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption -> (option.value)
//            else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toOption type")
//        }

//    private fun <V> toOption(setting: UserStyleSetting.Option) : V =
//        when (setting) {
//            is UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption -> setting.value
//            else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toOption type")
//        }

    enum class State{
        Loading, Ready
    }
}