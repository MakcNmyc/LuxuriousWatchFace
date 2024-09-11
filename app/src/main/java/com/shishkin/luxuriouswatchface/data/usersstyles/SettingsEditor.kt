package com.shishkin.luxuriouswatchface.data.usersstyles

import android.util.ArraySet
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.UserStyleSetting
import com.shishkin.luxuriouswatchface.util.CUSTOM_DATA_ID
import com.shishkin.luxuriouswatchface.util.findMember
import com.shishkin.luxuriouswatchface.util.setProperty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SettingsEditor @Inject constructor() {

    private var editorSession: EditorSession? = null

//    private val _settingsHolder = MutableStateFlow<UserSettings?>(null)
//    val settingsHolder = _settingsHolder.asStateFlow()

    private val _state = MutableStateFlow(State.WAITING_INIT)
    val state = _state.asStateFlow()

    @Inject
    lateinit var settingsHolder: SettingsHolder

    private val delayedActions = ArraySet<()->Unit>()

    fun initSession(activity: ComponentActivity){
        if (editorSession != null) return

        activity.lifecycleScope.launch{
            EditorSession.createOnWatchEditorSession(
                activity = activity
            ).also { editor ->

                editorSession = editor

                settingsHolder.subscribeToStyle(editor.userStyle, activity.lifecycleScope, _state)

                state.collectLatest { v ->
                    if(isReady(v)) {
                        for(action in delayedActions) action()
                        delayedActions.clear()
                    }
                }
            }
        }.invokeOnCompletion {
            Log.e("SettingsEditor", "SettingsEditor invokeOnCompletion")
            _state.value = State.WAITING_INIT
            editorSession = null
        }
    }


//    fun <V : Any> set(property: KProperty1<UserSettings, V>, value: V){
//        set(property.toId(), value.toOption())
//    }

    fun <V : Any> set(id: String, value: V) {
        doWhenReady{
            value.toCustomData(id){settingsHolder.settings.value!!.customData.copy()}?.let {
                set(CUSTOM_DATA_ID, it.toOption())
            } ?: set(id, value.toOption())
        }
//            .also {
//                Log.e("customData", "toCustomData id = $id value - $it")
//                set(id, it.toOption()) }
    }

    private fun set(id: String, userStyleOption: UserStyleSetting.Option){
        doWhenReady{
            val editor = editorSession!!
            val mutableUserStyle = editor.userStyle.value.toMutableUserStyle()
            Log.e("customData", "editor set id - $id userStyleSchema - ${editor.userStyleSchema} userStyleOption - $userStyleOption")
            mutableUserStyle[editor.userStyleSchema[UserStyleSetting.Id(id)]!!] = userStyleOption
            editor.userStyle.value = mutableUserStyle.toUserStyle()
        }
    }

    private fun <V : Any> V.toCustomData(propertyName: String, producer: () -> CustomData?) =
        CustomData::class.findMember(propertyName)?.let { customMember ->
            Log.e("customData", "toCustomData customMember = $customMember")
            producer()?.let { newCustomData ->
                Log.e("customData", "toCustomData this = $this newCustomData = $newCustomData")
                customMember.setProperty(newCustomData, this)
                newCustomData
            }
        }

    fun <V : Any> setSilently(id: String, value: V) {
        doWhenReady{
            (value.toCustomData(id){settingsHolder.settings.value!!.customData})?.setProperty(id, value)
            set(id, value)
        }
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

    companion object{
        fun isReady(state: State) = state == State.READY
    }

    private inline fun doWhenReady(crossinline action: ()->Unit){
        if(isReady(state.value)) {
            action()
        }else{
            delayAction{action()}
        }
    }

    private fun delayAction(action: ()->Unit){
        delayedActions.add(action)
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
        LOADING, READY, WAITING_INIT
    }
}