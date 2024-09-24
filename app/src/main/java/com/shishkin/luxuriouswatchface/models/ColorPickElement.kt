package com.shishkin.luxuriouswatchface.models

data class ColorPickElement(
    override val id: Int,
) : ListElementModel<Int> {
    val color: Int
        get() = id
}