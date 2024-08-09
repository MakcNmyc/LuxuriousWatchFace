package com.shishkin.luxuriouswatchface.models

import android.view.View
import com.shishkin.luxuriouswatchface.util.ImageCreator

data class SettingsTextWithImage(
    override val id: String,
    override val title: String,
    val rightImage: ImageCreator? = null,
    val onClickListener: View.OnClickListener? = null,
    ) : SettingsElement