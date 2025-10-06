package com.erbaris.android.google.api.translatorapp.api.google.service

import com.erbaris.android.google.api.translatorapp.api.google.dto.Data
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.GET

interface ITranslatorService {
    @GET("/language/translate/v2")
    fun translate(@Query("key") googleApiKey: String,
                  @Query("q") fromText: String,
                  @Query("source") sourceLanguageCode: String,
                  @Query("target") targetLanguageCode: String): Call<Data>
}