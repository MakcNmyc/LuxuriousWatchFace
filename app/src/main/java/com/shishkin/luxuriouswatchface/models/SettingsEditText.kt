package com.shishkin.luxuriouswatchface.models

data class SettingsEditText(
    override val id: String,
    override val title: String,
    var text: String
) : SettingsElement