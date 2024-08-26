package com.shishkin.luxuriouswatchface.usersstyles

import android.content.res.Resources
import android.graphics.drawable.Icon
import androidx.annotation.StringRes
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.WatchFaceLayer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

class UserStyleSettingDescription<V>(
//    private val editorSession: EditorSession,
    val id: UserStyleSetting.Id,
    val property: KProperty1<UserSettings, V>,
    val descriptionProducer: () -> AdditionalDescription<in V>,
) {



    data class AdditionalDescription<V>(
        @StringRes val displayNameResourceId: Int,
        @StringRes val descriptionResourceId: Int,
        val icon: Icon? = null,
        val minimumValue: Long = Int.MIN_VALUE.toLong(),
        val maximumValue: Long = Int.MAX_VALUE.toLong(),
        val affectsWatchFaceLayers: Collection<WatchFaceLayer> = listOf(WatchFaceLayer.BASE),
        val defaultValue: V? = null,
    )

    fun create(resources: Resources): UserStyleSetting =
        descriptionProducer().run {
            when (property.returnType.classifier as KClass<*>) {
                Int::class -> UserStyleSetting.LongRangeUserStyleSetting(
                    id,
                    resources,
                    displayNameResourceId,
                    descriptionResourceId,
                    null,
                    minimumValue,
                    maximumValue,
                    affectsWatchFaceLayers,
                    (defaultValue ?: 0L) as Long,
                    null
                )

                else -> throw IllegalArgumentException("Unsupported ${javaClass.name} create type ${property.returnType.classifier as KClass<*>}")
            }
        }


//    fun get() = editorSession.userStyleSchema[id]!!
//
//    fun set(userStyleOption: UserStyleSetting.Option){
//        val mutableUserStyle = editorSession.userStyle.value.toMutableUserStyle()
//        mutableUserStyle[get()] = userStyleOption
//        editorSession.userStyle.value = mutableUserStyle.toUserStyle()
//    }
//
//    fun set(value: V){
//        set(toOption(value))
//    }
//
//    private fun toOption(value: V) : UserStyleSetting.Option =
//        when (value) {
//            is Int -> UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption(value.toLong())
//            else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toOption type")
//        }
}