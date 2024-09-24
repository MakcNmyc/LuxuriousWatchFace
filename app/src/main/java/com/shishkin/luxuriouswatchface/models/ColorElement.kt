package com.shishkin.luxuriouswatchface.models

data class ColorElement(
    override val id: Int,
) : ListElementModel<Int> {
    val color: Int
        get() = id
}