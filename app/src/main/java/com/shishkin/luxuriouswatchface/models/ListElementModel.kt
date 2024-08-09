package com.shishkin.luxuriouswatchface.models

interface ListElementModel<T> {
    val id: T
    override fun equals(other: Any?): Boolean
}