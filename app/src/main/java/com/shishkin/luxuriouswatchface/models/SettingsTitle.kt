package com.shishkin.luxuriouswatchface.models

data class SettingsTitle (
    override val id: String,
    val title: String,
) : ListElementModel<String>