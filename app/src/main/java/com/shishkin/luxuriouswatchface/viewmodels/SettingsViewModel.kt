package com.shishkin.luxuriouswatchface.viewmodels

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.models.SettingsChanges
import com.shishkin.luxuriouswatchface.models.SettingsEditText
import com.shishkin.luxuriouswatchface.models.SettingsElement
import com.shishkin.luxuriouswatchface.models.SettingsTextWithImage
import com.shishkin.luxuriouswatchface.models.SettingsTitle
import com.shishkin.luxuriouswatchface.settings.SettingsDirections
import com.shishkin.luxuriouswatchface.util.ColorImageCreator
import com.shishkin.luxuriouswatchface.util.asPagedList
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {

//    lateinit var settingsChanges: StateFlow<SettingsChanges?>

    val title = SettingsTitle(TITLE_ID, context.resources.getString(R.string.settings_title))
    val pagedList = createSettingsList(context).asPagedList()

    init {
        Log.e("settingsIdFlow", "SettingsViewModel init")
    }

//    fun initArg(context: Context, savedStateHandle: SavedStateHandle){
//        settingsChanges = savedStateHandle
//            .getStateFlow(context.getString(R.string.settings_changes_argument), SETTINGS_CHANGES_NOT_SET)
//            .apply {
//                viewModelScope.launch {
//                    collect{
//                        Log.e("settingsIdFlow", "StateFlow collect changeSettings:${it}")
//                        changeSettings(it) }
//                }
//            }
//    }

    private fun createSettingsList(context: Context) : Array<SettingsElement>{

        return arrayOf(
            SettingsTextWithImage(
                BACKGROUND_COLOR,
                "qwe",
                ColorImageCreator(Color.RED),
                createColorPickerListener(BACKGROUND_COLOR)
            ) ,
            SettingsTextWithImage("ewq", "ewq"),
            SettingsEditText("asdasd",
                context.resources.getString(R.string.settings_top_text),
                "asdasd"),
        )
    }

    fun changeSettings(settingsChanges: SettingsChanges?){
        if(settingsChanges == null) return
        for (change in settingsChanges.changes){
            Log.e("qwe", "changeSettings settingsId:${change.settingsId} newValue:${change.newValue}")
        }
    }

//    binding.root.setOnClickListener{ v ->
//        Navigation.findNavController(v).navigate(
//            ContentMainFragmentDirections.actionContentMainToRateDetails(OuterDetailsModel(model))
//        )
//    }

    companion object {
        const val TITLE_ID = -1
        const val SETTINGS_ID_NOT_SET = ""
        val SETTINGS_CHANGES_NOT_SET = null

        const val BACKGROUND_COLOR = "BACKGROUND_COLOR"

        fun createColorPickerListener(settingsId: String) = View.OnClickListener { v ->
            Navigation.findNavController(v).navigate(
//                Log.e("qwe", "fo to colorpicker settingsId:$settingsId")
//                SettingsDiraction.actionSettingsToColorPicker(settingsId)
                SettingsDirections.actionSettingsToColorsPicker(settingsId)
            )
        }
    }

}