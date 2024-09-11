package com.shishkin.luxuriouswatchface.data.usersstyles

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
    val property: KProperty1<UserSettings, V>,
    @StringRes val displayNameResourceId: Int,
    @StringRes private val descriptionResourceId: Int,
    val defaultValue: V,
    private val icon: Icon? = null,
    private val minimumValue: Long = Int.MIN_VALUE.toLong(),
    private val maximumValue: Long = Int.MAX_VALUE.toLong(),
    private val affectsWatchFaceLayers: Collection<WatchFaceLayer> = listOf(WatchFaceLayer.BASE),
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
                (defaultValue as Int).toLong(),
                null
            )
            CustomData::class -> UserStyleSetting.CustomValueUserStyleSetting(
                listOf(WatchFaceLayer.BASE),
                Json.encodeToString(CustomData()).toByteArray()
            )

            else -> throw IllegalArgumentException("Unsupported ${javaClass.name} create type ${property.returnType.classifier as KClass<*>}")
        }
}