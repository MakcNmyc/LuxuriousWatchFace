package com.shishkin.luxuriouswatchface.models

data class SettingsTitle (
    override val id: Int,
    val title: String,
) : ListElementModel<Int>