package com.shishkin.luxuriouswatchface.models

data class SettingsData(
    override val id: String,
    val type: Int,
    val title: String = "",
    val imageResource: Int = 0,
    val colorImage: Int = 0,
    val text: String = "",
    val clickListenerType: Int = -1,
) : ListElementModel<String>