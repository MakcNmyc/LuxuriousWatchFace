package com.shishkin.luxuriouswatchface.usersstyles

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.UserStyleSetting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsEditor(
    activity: AppCompatActivity
) {

    private lateinit var editorSession: EditorSession

    private val _settingsHolder = MutableStateFlow(HashMap<String, Any>())
    val settingsHolder = _settingsHolder.asStateFlow()

    private val _state = MutableStateFlow(State.Loading)
    val state = _state.asStateFlow()

    init {
        activity.lifecycleScope.launch{
            editorSession = EditorSession.createOnWatchEditorSession(
                activity = activity
            )

            editorSession.userStyle.collect{
                _state.value = State.Loading

                val settings = HashMap<String, Any>()
                for(setting in it){
                    settings[setting.key.id.value] = fromOption(setting.value)
                }
                _settingsHolder.value = settings

                _state.value = State.Ready
            }
        }
    }

    operator fun get(id: String) = editorSession.userStyleSchema[UserStyleSetting.Id(id)]!!

//    fun getValue(id: String){
//        editorSession.userStyle.collect{}
//
//        when (val setting = get(id)[id]) {
//            is UserStyleSetting.LongRangeUserStyleSetting -> setting[id]
//            else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toOption type")
//        }
//    }

    fun set(id: String, userStyleOption: UserStyleSetting.Option){
        val mutableUserStyle = editorSession.userStyle.value.toMutableUserStyle()
        mutableUserStyle[this[id]] = userStyleOption
        editorSession.userStyle.value = mutableUserStyle.toUserStyle()
    }

    fun <V> set(id: String, value: V){
        set(id, toOption(value))
    }

    private fun <V> toOption(value: V) : UserStyleSetting.Option =
        when (value) {
            is Int -> UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption(value.toLong())
            is Long -> UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption(value)
            else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toOption type")
        }

    private fun <V> fromOption(option: V) =
        when (option) {
            is UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption -> (option.value)
            else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toOption type")
        }

//    private fun <V> toOption(setting: UserStyleSetting.Option) : V =
//        when (setting) {
//            is UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption -> setting.value
//            else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toOption type")
//        }

    enum class State{
        Loading, Ready
    }
}