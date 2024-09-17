package com.shishkin.luxuriouswatchface.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.shishkin.luxuriouswatchface.R
import com.shishkin.luxuriouswatchface.adapters.SettingsAdapter
import com.shishkin.luxuriouswatchface.data.SettingsRepository
import com.shishkin.luxuriouswatchface.data.usersstyles.SettingsEditor
import com.shishkin.luxuriouswatchface.data.usersstyles.UserSettings
import com.shishkin.luxuriouswatchface.models.SettingsData
import com.shishkin.luxuriouswatchface.ui.util.toPagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel() {

//    lateinit var settingsChanges: StateFlow<SettingsChanges?>

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val title = SettingsData(
        TITLE_ID,
        SettingsAdapter.TITLE,
        context.resources.getString(R.string.settings_title),
    )

    private val footer = SettingsData(
        FOOTER_ID,
        SettingsAdapter.FOOTER,
    )

    private var _settingsData: MutableStateFlow<PagingData<SettingsData>?> = MutableStateFlow(null)
    var settingsData = _settingsData.asStateFlow()

//    init {
//        Log.e("settingsIdFlow", "SettingsViewModel init")
//    }

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

    fun initSettingsSession(activity: ComponentActivity){
        settingsRepository.initSession(activity)
        subscribeToSettingsChange(context)
    }

    suspend fun subscribeToSettingsState(listener : (SettingsEditor.State) -> Unit){
        settingsRepository.subscribeToSettingsState(listener)
    }

    private fun subscribeToSettingsChange(context: Context){
        if(_settingsData.value == null)
            viewModelScope.launch {
                settingsRepository.subscribeToSettingsChange {
                    Log.e("customData", "collectLatest settingsHolder is ${it}")
                    _settingsData.value = createSettingsData(context, it)
                }
            }
    }

    private fun createSettingsData(context: Context, userSettings: UserSettings?) =
        arrayListOf(title).apply {
            addAll(settingsRepository.getSettingsListData(context, userSettings))
            add(footer)
        }.toPagingData()

    fun saveTextSetting(id: String, value: String){
        Log.e("customData", "SettingsViewModel saveTextSetting SettingsRepository - ${settingsRepository} id - $id value - $value")
        settingsRepository.setSettingSilently(id, value)
    }

//    override fun onCleared() {
//        Log.e("settingsIdFlow", "SettingsViewModel onCleared")
//        super.onCleared()
//    }


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
        const val TITLE_ID = "settingsTitle"
        const val FOOTER_ID = "footerId"
        const val SETTINGS_ID_NOT_SET = ""
        val SETTINGS_CHANGES_NOT_SET = null
    }

}