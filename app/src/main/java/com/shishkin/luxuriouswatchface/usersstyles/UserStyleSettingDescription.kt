package com.shishkin.luxuriouswatchface.usersstyles

import android.content.res.Resources
import android.graphics.drawable.Icon
import androidx.annotation.StringRes
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.WatchFaceLayer
import com.shishkin.luxuriouswatchface.util.typeFromClassifier
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

class UserStyleSettingDescription<V : Any>(
//    private val editorSession: EditorSession,
    val property: KProperty1<UserSettings, V>,
    @StringRes val displayNameResourceId: Int,
    @StringRes private val descriptionResourceId: Int,
    private val icon: Icon? = null,
    private val minimumValue: Long = Int.MIN_VALUE.toLong(),
    private val maximumValue: Long = Int.MAX_VALUE.toLong(),
    private val affectsWatchFaceLayers: Collection<WatchFaceLayer> = listOf(WatchFaceLayer.BASE),
    private val defaultValue: V? = null,
) {

    fun create(resources: Resources): UserStyleSetting =
        when (property.typeFromClassifier()) {
            Int::class -> UserStyleSetting.LongRangeUserStyleSetting(
                UserStyleSetting.Id(property.name),
                resources,
                displayNameResourceId,
                descriptionResourceId,
                icon,
                minimumValue,
                maximumValue,
                affectsWatchFaceLayers,
                (defaultValue ?: 0L) as Long,
                null
            )
            CustomData::class -> UserStyleSetting.CustomValueUserStyleSetting(
                listOf(WatchFaceLayer.BASE),
                Json.encodeToString(CustomData()).toByteArray()
            )

            else -> throw IllegalArgumentException("Unsupported ${javaClass.name} create type ${property.returnType.classifier as KClass<*>}")
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