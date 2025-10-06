package com.erbaris.android.google.api.translatorapp.api.google.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TranslatedText(@SerializedName("translatedText") val translatedText: String): Serializable {
    override fun toString(): String  = translatedText
}


