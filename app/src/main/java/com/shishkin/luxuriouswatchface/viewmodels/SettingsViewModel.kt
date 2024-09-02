package com.shishkin.luxuriouswatchface.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.adapters.SettingsAdapter
import com.shishkin.luxuriouswatchface.models.SettingsData
import com.shishkin.luxuriouswatchface.usersstyles.SettingsEditor
import com.shishkin.luxuriouswatchface.usersstyles.SettingsRepository
import com.shishkin.luxuriouswatchface.usersstyles.SettingsSchema
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

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var schema: SettingsSchema

    private val title = SettingsData(
        TITLE_ID,
        SettingsAdapter.TITLE,
        context.resources.getString(R.string.settings_title),
    )

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
//                    Log.e("colorsPick", "collectLatest settingsHolder color is ${settingsEditor.settingsHolder.value[SettingsSchemaImp.BACKGROUND_COLOR]}")
                    _settingsData.value = createSettingsData(context, settingsEditor)
                }
            }
    }

    private fun createSettingsData(context: Context, settingsEditor: SettingsEditor) =
        arrayListOf(title).apply {
            addAll(settingsRepository.getSettingsData(context, settingsEditor, schema))
        }.toPagingData()

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