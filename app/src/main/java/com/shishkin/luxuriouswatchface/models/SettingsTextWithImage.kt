package com.shishkin.luxuriouswatchface.models

import com.shishkin.luxuriouswatchface.adapters.SettingsAdapter
import com.shishkin.luxuriouswatchface.util.ImageCreator
import com.shishkin.luxuriouswatchface.util.toColorImageCreator
import com.shishkin.luxuriouswatchface.util.toEnum
import com.shishkin.luxuriouswatchface.util.toImageResourceCreator

data class SettingsTextWithImage(
    override val id: String,
    val title: String,
    val rightImage: ImageCreator? = null,
    val clickListenerType: SettingsAdapter.ClickListenerTypes? = null,
) : ListElementModel<String>

fun SettingsData.toSettingsTextWithImage() =
    SettingsTextWithImage(
        id,
        title,
        imageResource.toImageResourceCreator() ?: colorImage.toColorImageCreator(),
        clickListenerType.toEnum<SettingsAdapter.ClickListenerTypes>()
    )