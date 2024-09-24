package com.shishkin.luxuriouswatchface.ui.viewmodels

import android.content.Context
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

    private val _settingsData: MutableStateFlow<PagingData<SettingsData>?> = MutableStateFlow(null)
    val settingsData = _settingsData.asStateFlow()

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
        settingsRepository.setSettingSilently(id, value)
    }

    companion object {
        const val TITLE_ID = "settingsTitle"
        const val FOOTER_ID = "footerId"
    }

}