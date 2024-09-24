package com.shishkin.luxuriouswatchface.data

import android.content.Context
import androidx.activity.ComponentActivity
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.adapters.SettingsAdapter
import com.shishkin.luxuriouswatchface.data.usersstyles.CustomData
import com.shishkin.luxuriouswatchface.data.usersstyles.SettingsEditor
import com.shishkin.luxuriouswatchface.data.usersstyles.SettingsSchema
import com.shishkin.luxuriouswatchface.data.usersstyles.UserSettings
import com.shishkin.luxuriouswatchface.models.SettingsData
import com.shishkin.luxuriouswatchface.ui.util.toId
import com.shishkin.luxuriouswatchface.ui.util.toInt
import kotlinx.coroutines.flow.StateFlow
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

    fun <V : Any> setSetting(id: String, value: V){
        settingsEditor.set(id, value)
    }

    fun <V : Any> setSettingSilently(id: String, value: V){
        settingsEditor.setSilently(id, value)
    }

    suspend fun subscribeToSettingsChange(listener : (UserSettings?) -> Unit){
        settingsEditor.settingsHolder.settings.collectLatest{
            listener(it)
        }
    }

    suspend fun subscribeToSettingsState(listener : (SettingsEditor.State) -> Unit){
        settingsEditor.state.collectLatest {
            listener(it)
        }
    }

    fun getSettingsListData(context: Context, userSettings: UserSettings?) : List<SettingsData> =
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
            )
        } ?: arrayListOf()

    fun saveToSetting(settingsId : StateFlow<String>, value: Int) : Boolean{
        settingsId.value.let {
            if(it == SettingsRepository.SETTINGS_ID_NOT_SET) return false
            setSetting(it, value)
        }

        return true
    }

    companion object{
        const val SETTINGS_ID_NOT_SET = ""
    }
}