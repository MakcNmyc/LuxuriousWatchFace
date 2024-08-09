package com.shishkin.luxuriouswatchface.models

interface SettingsElement: ListElementModel<String>{
    override val id: String
    val title: String
}