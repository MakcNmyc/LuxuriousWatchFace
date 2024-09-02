package com.shishkin.luxuriouswatchface.usersstyles

import android.content.Context
import com.shishkin.luxuriouswatchface.adapters.SettingsAdapter
import com.shishkin.luxuriouswatchface.models.SettingsData
import com.shishkin.luxuriouswatchface.util.toId
import com.shishkin.luxuriouswatchface.util.toInt
import javax.inject.Inject

class SettingsRepository @Inject constructor() {

    fun getSettingsData(context: Context, settingsEditor: SettingsEditor, schema: SettingsSchema) : List<SettingsData> =
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
                    id = UserSettings::backGroundColor.toId(),
                    type = SettingsAdapter.TEXT_WITH_IMAGE,
                    title = context.resources.getString(schema.backgroundColor.displayNameResourceId),
                    colorImage = settings.backGroundColor,
                    clickListenerType = SettingsAdapter.ClickListenerTypes.ColorPickListener.toInt()
                ),
                SettingsData(
                    id = "1",
                    type = SettingsAdapter.EDIT_TEXT,
                    title = "1",
                    text = "ewq",
                ),
                SettingsData(
                    id = "2",
                    type = SettingsAdapter.EDIT_TEXT,
                    title = "2",
                    text = "ewq",
                ),
                SettingsData(
                    id = "3",
                    type = SettingsAdapter.EDIT_TEXT,
                    title = "3",
                    text = "ewq",
                ),
                SettingsData(
                    id = "4",
                    type = SettingsAdapter.EDIT_TEXT,
                    title = "4",
                    text = "ewq",
                ),
                SettingsData(
                    id = "5",
                    type = SettingsAdapter.EDIT_TEXT,
                    title = "5",
                    text = "ewq",
                ),
                SettingsData(
                    id = "6",
                    type = SettingsAdapter.EDIT_TEXT,
                    title = "6",
                    text = "ewq",
                ),
                SettingsData(
                    id = "7",
                    type = SettingsAdapter.EDIT_TEXT,
                    title = "7",
                    text = "ewq",
                ),
            )
        } ?: arrayListOf()

}