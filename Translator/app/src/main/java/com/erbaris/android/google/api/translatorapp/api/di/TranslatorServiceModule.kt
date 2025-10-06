package com.erbaris.android.google.api.translatorapp.api.di

import com.erbaris.android.google.api.translatorapp.api.google.service.ITranslatorService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TranslatorServiceModule {
    @Provides
    @Singleton
    fun provideTranslatorService(retrofit: Retrofit): ITranslatorService = retrofit.create(ITranslatorService::class.java)
}