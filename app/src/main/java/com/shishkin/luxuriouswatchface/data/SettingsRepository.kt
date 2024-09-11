package com.shishkin.luxuriouswatchface.data

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.adapters.SettingsAdapter
import com.shishkin.luxuriouswatchface.data.usersstyles.CustomData
import com.shishkin.luxuriouswatchface.data.usersstyles.SettingsEditor
import com.shishkin.luxuriouswatchface.data.usersstyles.SettingsSchema
import com.shishkin.luxuriouswatchface.data.usersstyles.UserSettings
import com.shishkin.luxuriouswatchface.models.SettingsData
import com.shishkin.luxuriouswatchface.util.toId
import com.shishkin.luxuriouswatchface.util.toInt
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class SettingsRepository @Inject constructor() {

    @Inject
    lateinit var schema: SettingsSchema
    @Inject
    lateinit var settingsEditor: SettingsEditor

    fun initSession(activity: ComponentActivity){
        settingsEditor.initSession(activity)
    }

//    fun <V : Any> setSetting(property: KProperty1<UserSettings, V>, value: V){
//        settingsEditor.set(property, value)
//    }

    fun <V : Any> setSetting(id: String, value: V){
        settingsEditor.set(id, value)
    }

    fun <V : Any> setSettingSilently(id: String, value: V){
        Log.e("customData", "SettingsRepository setSettingSilently id = ${id} value = $value")
        settingsEditor.setSilently(id, value)
    }

    suspend fun subscribeToSettingsChange(listener : (UserSettings?) -> Unit){
        settingsEditor.settingsHolder.settings.collectLatest{
            Log.e("customData", "collectLatest settingsHolder is ${it}")
            listener(it)
        }
    }

    suspend fun subscribeToSettingsState(listener : (SettingsEditor.State) -> Unit){
        settingsEditor.state.collectLatest {
            listener(it)
        }
    }

    fun getSettingsListData(context: Context, userSettings: UserSettings?) : List<SettingsData> =
//        Log.e("colorsPick", "getData color is ${settingsEditor.get[].settingsHolder.value[SettingsSchemaImp.BACKGROUND_COLOR]}").let {
//            if (settingsEditor.state.value != SettingsEditor.State.Ready)
//                Log.e("colorsPick", "SettingsViewModel getData settingsEditor state not ready!").let {
//                    arrayListOf()
//                }
//            else
//                arrayListOf(
//                    SettingsData(
//                        id = SettingsSchema.BACKGROUND_COLOR,
//                        type = SettingsAdapter.TEXT_WITH_IMAGE,
//                        title = "qwe",
//                        colorImage = settingsEditor.settingsHolder.b,
//                        clickListenerType = SettingsAdapter.ClickListenerTypes.ColorPickListener.toInt()
//                    ),
//                    SettingsData(
//                        id = "ewq",
//                        type = SettingsAdapter.EDIT_TEXT,
//                        title = "ewq",
//                    ),
//                )
//        }

        userSettings?.let { settings ->
            arrayListOf(
                SettingsData(
                    id = UserSettings::backgroundColor.toId(),
                    type = SettingsAdapter.TEXT_WITH_IMAGE,
                    title = context.resources.getString(schema.backgroundColor.displayNameResourceId),
                    colorImage = settings.backgroundColor,
                    clickListenerType = SettingsAdapter.ClickListenerTypes.COLOR_PICK_LISTENER.toInt()
                ),
                SettingsData(
                    id = UserSettings::accessoriesColor.toId(),
                    type = SettingsAdapter.TEXT_WITH_IMAGE,
                    title = context.resources.getString(schema.accessoriesColor.displayNameResourceId),
                    colorImage = settings.accessoriesColor,
                    clickListenerType = SettingsAdapter.ClickListenerTypes.COLOR_PICK_LISTENER.toInt()
                ),
                SettingsData(
                    id = CustomData::topText.toId(),
                    type = SettingsAdapter.EDIT_TEXT,
                    title = context.resources.getString(R.string.setting_top_text_name),
                    text = settings.customData.topText,
                ),
                SettingsData(
                    id =  CustomData::bottomText.toId(),
                    type = SettingsAdapter.EDIT_TEXT,
                    title = context.resources.getString(R.string.setting_bottom_text_name),
                    text = settings.customData.bottomText,
                ),
//                SettingsData(
//                    id = "4",
//                    type = SettingsAdapter.EDIT_TEXT,
//                    title = "4",
//                    text = "ewq",
//                ),
//                SettingsData(
//                    id = "5",
//                    type = SettingsAdapter.EDIT_TEXT,
//                    title = "5",
//                    text = "ewq",
//                ),
//                SettingsData(
//                    id = "6",
//                    type = SettingsAdapter.EDIT_TEXT,
//                    title = "6",
//                    text = "ewq",
//                ),
//                SettingsData(
//                    id = "7",
//                    type = SettingsAdapter.EDIT_TEXT,
//                    title = "7",
//                    text = "ewq",
//                ),
            )
        } ?: arrayListOf()

}