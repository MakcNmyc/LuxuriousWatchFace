package com.shishkin.luxuriouswatchface.models

data class SettingsEditText(
    override val id: String,
    val title: String,
    var text: String,
): ListElementModel<String>

fun SettingsData.toSettingsEditText() =
    SettingsEditText(id, title, text)

