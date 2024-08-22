package com.shishkin.luxuriouswatchface.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.adapters.SettingsAdapter
import com.shishkin.luxuriouswatchface.models.SettingsData
import com.shishkin.luxuriouswatchface.models.SettingsTitle
import com.shishkin.luxuriouswatchface.usersstyles.SettingsEditor
import com.shishkin.luxuriouswatchface.usersstyles.SettingsSchemaImp
import com.shishkin.luxuriouswatchface.util.toInt
import com.shishkin.luxuriouswatchface.util.toPagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {

//    lateinit var settingsChanges: StateFlow<SettingsChanges?>

    val title = SettingsTitle(TITLE_ID, context.resources.getString(R.string.settings_title))

    private var _settingsData: MutableStateFlow<PagingData<SettingsData>?> = MutableStateFlow(null)
    var settingsData = _settingsData.asStateFlow()

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

    fun initSettingsData(context: Context, settingsEditor: SettingsEditor){
        if(_settingsData.value == null)
            viewModelScope.launch {
                settingsEditor.settingsHolder.collectLatest{
                    Log.e("colorsPick", "collectLatest settingsHolder color is ${settingsEditor.settingsHolder.value[SettingsSchemaImp.BACKGROUND_COLOR]}")
                    _settingsData.value = createPagedData(context, settingsEditor)
                }
            }
    }

    private fun createPagedData(context: Context, settingsEditor: SettingsEditor) =
        getData(context, settingsEditor).toPagingData()

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

    private fun getData(context: Context, settingsEditor: SettingsEditor) : List<SettingsData> =
        Log.e("colorsPick", "getData color is ${settingsEditor.settingsHolder.value[SettingsSchemaImp.BACKGROUND_COLOR]}").let {
            if (settingsEditor.state.value != SettingsEditor.State.Ready)
                Log.e("colorsPick", "SettingsViewModel getData settingsEditor state not ready!").let {
                    arrayListOf()
                }
            else
                arrayListOf(
                    SettingsData(
                        id = SettingsSchemaImp.BACKGROUND_COLOR,
                        type = SettingsAdapter.TEXT_WITH_IMAGE,
                        title = "qwe",
                        colorImage = (settingsEditor.settingsHolder.value[SettingsSchemaImp.BACKGROUND_COLOR] as Long).toInt(),
                        clickListenerType = SettingsAdapter.ClickListenerTypes.ColorPickListener.toInt()
                    ),
                    SettingsData(
                        id = "ewq",
                        type = SettingsAdapter.EDIT_TEXT,
                        title = "ewq",
                        text = "ewq",
                    ),
                )
        }


//        return arrayOf(
//            SettingsTextWithImage(
//                SettingsSchemaImp.BACKGROUND_COLOR,
//                "qwe",
//                ColorImageCreator(c.toInt()),
//                createColorPickerListener(SettingsSchemaImp.BACKGROUND_COLOR)
//            ),
//            SettingsTextWithImage("ewq", "ewq"),
//            SettingsEditText("asdasd",
//                context.resources.getString(R.string.settings_top_text),
//                "asdasd"),
//        )


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



    companion object {
        const val TITLE_ID = "SETTINGS_TITLE"
        const val SETTINGS_ID_NOT_SET = ""
        val SETTINGS_CHANGES_NOT_SET = null
    }

}