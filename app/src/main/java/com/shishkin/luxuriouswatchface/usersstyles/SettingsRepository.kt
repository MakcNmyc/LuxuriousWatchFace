package com.shishkin.luxuriouswatchface.usersstyles

import android.content.Context
import com.shishkin.luxuriouswatchface.adapters.SettingsAdapter
import com.shishkin.luxuriouswatchface.models.SettingsData
import com.shishkin.luxuriouswatchface.util.toInt
import javax.inject.Inject

class SettingsRepository @Inject constructor() {

    fun getSettingsData(context: Context, settingsEditor: SettingsEditor) : List<SettingsData> =
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
//                        text = "ewq",
//                    ),
//                )
//        }

        settingsEditor.settingsHolder.value?.let { settings ->
            arrayListOf(
                SettingsData(
                    id = UserSettings.BACKGROUND_COLOR,
                    type = SettingsAdapter.TEXT_WITH_IMAGE,
                    title = "qwe",
                    colorImage = settings.backGroundColor,
                    clickListenerType = SettingsAdapter.ClickListenerTypes.ColorPickListener.toInt()
                ),
                SettingsData(
                    id = "ewq",
                    type = SettingsAdapter.EDIT_TEXT,
                    title = "ewq",
                    text = "ewq",
                ),
            )
        } ?: arrayListOf()

}