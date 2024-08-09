package com.shishkin.luxuriouswatchface.models

data class ColorsElement(
    override val id: Int,
) : ListElementModel<Int> {
    val color: Int
        get() = id
}