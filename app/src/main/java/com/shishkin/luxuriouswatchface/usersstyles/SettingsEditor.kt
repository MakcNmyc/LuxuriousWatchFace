package com.shishkin.luxuriouswatchface.usersstyles

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.UserStyleSetting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsEditor @Inject constructor(
    private val schema: SettingsSchema
) {

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

//    operator fun get(id: String) = _settingsHolder.value[id]!!

//    fun settingsDescription() : UserStyleSettingDescription<*>{
//
//    }

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
        mutableUserStyle[editorSession.userStyleSchema[UserStyleSetting.Id(id)]!!] = userStyleOption
        editorSession.userStyle.value = mutableUserStyle.toUserStyle()
    }

    fun <V : Any> set(id: String, value: V){
        set(id, value.toOption())
    }

    private fun <V : Any> V.toOption() : UserStyleSetting.Option =
        when (this) {
            is Int -> UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption(this.toLong())
            is Long -> UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption(this)
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