package com.shishkin.luxuriouswatchface.di

import com.shishkin.luxuriouswatchface.data.usersstyles.SettingsHolder
import com.shishkin.luxuriouswatchface.data.usersstyles.SettingsSchema
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppEntryPoint {

    fun createSettingsSchema(): SettingsSchema

    fun createSettingsHolder(): SettingsHolder

}