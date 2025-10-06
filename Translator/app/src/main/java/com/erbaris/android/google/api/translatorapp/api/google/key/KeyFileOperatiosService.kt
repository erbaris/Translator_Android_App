package com.erbaris.android.google.api.translatorapp.api.google.key

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyFileOperatiosService @Inject constructor(@ApplicationContext private val context: Context) {
    private val file = File(context.filesDir, "google_api_key.txt")

    fun getGoogleApiKey(): String {
        return if (!file.exists())
            ""
        else
            file.readText()
    }

    fun saveGoogleApiKey(keyString: String) {
        try {
            file.writeText(keyString)
        }
        catch (ignore: IOException) {
        }
    }
}
