package com.shishkin.luxuriouswatchface.data.usersstyles

import androidx.wear.watchface.style.UserStyle
import com.shishkin.luxuriouswatchface.data.usersstyles.SettingsEditor.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsHolder @Inject constructor(
){

    private val _settings = MutableStateFlow<UserSettings?>(null)
    val settings = _settings.asStateFlow()

    fun subscribeToStyle(userStyle: StateFlow<UserStyle>,
                         scope: CoroutineScope,
                         state: MutableStateFlow<State>? = null){

        if(state == null){
            scope.launch {
                userStyle.collectLatest{ userStyle ->
                    _settings.value = userStyle.toUserSettings()
                }
            }
        }else{
            scope.launch {
                userStyle.collectLatest{ userStyle ->
                    state.value = State.LOADING

                    _settings.value = userStyle.toUserSettings()

                    state.value = State.READY
                }
            }
        }


    }

}