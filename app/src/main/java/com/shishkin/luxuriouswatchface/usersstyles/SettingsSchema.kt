package com.shishkin.luxuriouswatchface.usersstyles

import android.content.res.Resources
import androidx.wear.watchface.style.UserStyleSchema

interface SettingsSchema {
    fun createUserStyleSchema(resources: Resources) : UserStyleSchema
    operator fun get(id: String) : UserStyleSettingDescription<*>
}