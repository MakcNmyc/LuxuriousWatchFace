package com.shishkin.luxuriouswatchface.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingsChanges(
    val changes : List<SettingsChange<*>>
) : Parcelable{

    constructor(settingsId : String, newValue : Int) : this(
        listOf(SettingsIntValue(settingsId, newValue))
    )

    interface SettingsChange<V> : Parcelable {
        val settingsId: String
        val newValue: V
    }

    @Parcelize
    data class SettingsIntValue(
        override val settingsId : String,
        override val newValue : Int
    ) : SettingsChange<Int>
}