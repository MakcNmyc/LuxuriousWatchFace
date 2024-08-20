package com.shishkin.luxuriouswatchface.viewmodels

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.paging.PagedList
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.models.SettingsEditText
import com.shishkin.luxuriouswatchface.models.SettingsElement
import com.shishkin.luxuriouswatchface.models.SettingsTextWithImage
import com.shishkin.luxuriouswatchface.models.SettingsTitle
import com.shishkin.luxuriouswatchface.settings.SettingsDirections
import com.shishkin.luxuriouswatchface.usersstyles.SettingsEditor
import com.shishkin.luxuriouswatchface.usersstyles.SettingsSchemaImp
import com.shishkin.luxuriouswatchface.util.ColorImageCreator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {

//    lateinit var settingsChanges: StateFlow<SettingsChanges?>

    val title = SettingsTitle(TITLE_ID, context.resources.getString(R.string.settings_title))
    var pagedList : LiveData<PagedList<SettingsElement>> = MutableLiveData()

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

//    fun initPageList(context: Context, settingsEditor: SettingsEditor){
//        pagedList = createSettingsList(context, settingsEditor).asPagedList()
//    }

    fun createPagedList(context: Context, settingsEditor: SettingsEditor) {
//        pagedList = createSettingsList(context, settingsEditor).asPagedList{ dataSource ->
//            viewModelScope.launch {
//                settingsEditor.settingsHolder.collect{
//                    dataSource.invalidate()
//                }
//            }
//        }
//            .also {
//            viewModelScope.launch {
//                settingsEditor.settingsHolder.collect{
//                    pagedList = createSettingsList(context, settingsEditor).asPagedList()
//                }
//            }
//        }
    }

//    fun refreshPagedList(context: Context, settingsEditor: SettingsEditor) {
//        if (this::pagedList.isInitialized)
//        if (pagedList.value == null)
//            pagedList = createSettingsList(context, settingsEditor).asPagedList()
//        else
//            pagedList.value?.let {
//                it.clear()
//                it.addAll(createSettingsList(context, settingsEditor))
//            }
//    }

    private fun createSettingsList(context: Context, settingsEditor: SettingsEditor) : Array<SettingsElement>{

        if(settingsEditor.settingsHolder.value.size == 0) return arrayOf()

        Log.e("settingsList", "size ${settingsEditor.settingsHolder.value.size}")
        Log.e("settingsList", "${settingsEditor.settingsHolder.value[SettingsSchemaImp.BACKGROUND_COLOR]!!::class}")

        val c = settingsEditor.settingsHolder.value[SettingsSchemaImp.BACKGROUND_COLOR] as Long

        return arrayOf(
            SettingsTextWithImage(
                SettingsSchemaImp.BACKGROUND_COLOR,
                "qwe",
                ColorImageCreator(c.toInt()),
                createColorPickerListener(SettingsSchemaImp.BACKGROUND_COLOR)
            ),
            SettingsTextWithImage("ewq", "ewq"),
            SettingsEditText("asdasd",
                context.resources.getString(R.string.settings_top_text),
                "asdasd"),
        )
    }

//    data class SettingsListElement(
//        val type: SettingsListElementTypes,
//        val id: String,
//        val title: String,
//        val rightImageType: SettingsImageTypes,
//        val rightImage: String,
//
//    )
//
//    enum class SettingsListElementTypes{
//        TextWithImage, EditText
//    }
//
//    enum class SettingsImageTypes{
//        Color, Resource
//    }

//    fun changeSettings(settingsChanges: Bundle?, settingsEditor: SettingsEditor){
//        if(settingsChanges == null) return
//
////        for (change in settingsChanges.changes){
////            Log.e("qwe", "changeSettings settingsId:${change.settingsId} newValue:${change.newValue}")
////
////            settingsEditor.set(change.settingsId, change.newValue)
////        }
//
//        for (change in settingsChanges.){
//            Log.e("qwe", "changeSettings settingsId:${change.settingsId} newValue:${change.newValue}")
//
//            settingsEditor.set(change.settingsId, change.newValue)
//        }
//    }

//    binding.root.setOnClickListener{ v ->
//        Navigation.findNavController(v).navigate(
//            ContentMainFragmentDirections.actionContentMainToRateDetails(OuterDetailsModel(model))
//        )
//    }

    private fun createColorPickerListener(settingsId: String) = View.OnClickListener { v ->
        Navigation.findNavController(v).navigate(
//                Log.e("qwe", "fo to colorpicker settingsId:$settingsId")
//                SettingsDiraction.actionSettingsToColorPicker(settingsId)
            SettingsDirections.actionSettingsToColorsPicker(settingsId)
        )
    }

    companion object {
        const val TITLE_ID = -1
        const val SETTINGS_ID_NOT_SET = ""
        val SETTINGS_CHANGES_NOT_SET = null
    }

}