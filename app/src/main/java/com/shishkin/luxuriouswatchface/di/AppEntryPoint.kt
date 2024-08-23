package com.shishkin.luxuriouswatchface.di

import com.shishkin.luxuriouswatchface.usersstyles.SettingsSchemaImp
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppEntryPoint {
    @Singleton
    fun createSettingsSchema(): SettingsSchemaImp
}