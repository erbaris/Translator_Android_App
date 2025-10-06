package com.erbaris.android.google.api.translatorapp

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.erbaris.android.google.api.translatorapp.api.google.dto.Data
import com.erbaris.android.google.api.translatorapp.api.google.key.KeyFileOperatiosService
import com.erbaris.android.google.api.translatorapp.api.google.service.ITranslatorService
import com.erbaris.android.google.api.translatorapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    @Inject
    lateinit var translatorService: ITranslatorService
    @Inject
    lateinit var keyFileOperationsService: KeyFileOperatiosService
    @Inject
    lateinit var threadPool: ExecutorService

    private var fromItemSelectedPosition = 0
    private var toItemSelectedPosition = 0
    private val languageList = arrayOf("English", "Turkish", "German", "French", "Spanish", "Italian", "Russian")
    private val languageCodeList = arrayOf("en", "tr", "de", "fr", "es", "it", "ru")

    private fun translateCallback(): Callback<Data> {
        return object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                Log.i("Response:", response.raw().toString())

                if (!response.isSuccessful || response.body() == null) {
                    Log.e("Status", response.code().toString())
                    Toast.makeText(this@MainActivity, "Unsuccessful operation", Toast.LENGTH_LONG).show()
                    return
                }

                //Log headers
                response.headers().names().forEach { Log.i("Header", it) }

                val translations = response.body()?.data?.translations

                if (translations == null || translations.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Limit exhausted or invalid response", Toast.LENGTH_LONG).show()
                    return
                }
                mBinding.toText = translations.first().translatedText
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Log.e("onFailure", "Retrofit call failed", t)
                Toast.makeText(this@MainActivity, "Error occurred while connection", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initBinding() {
        enableEdgeToEdge()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getGoogleApiKey(): String {
        return keyFileOperationsService.getGoogleApiKey()
    }

    private fun initFromAdapter() {
        mBinding.fromSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languageList)

    }

    private fun initToAdapter() {
        mBinding.toSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languageList)
    }

    private fun initModels() {
        mBinding.activity = this
        mBinding.googleApiKey = getGoogleApiKey()
        mBinding.fromText = ""
        mBinding.toText = ""
        initFromAdapter()
        initToAdapter()
    }

    private fun initialize() {
        initBinding()
        initModels()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()

    }

    fun translateButtonClicked() {
        val call = translatorService.translate(mBinding.googleApiKey!!, mBinding.fromText!!, languageCodeList[fromItemSelectedPosition], languageCodeList[toItemSelectedPosition])

        call.enqueue(translateCallback())
    }

    private fun saveButtonCallback() {
        keyFileOperationsService.saveGoogleApiKey(mBinding.googleApiKey!!)
        runOnUiThread { Toast.makeText(this, " Key is Saved", Toast.LENGTH_LONG).show() }
    }
    fun saveButtonClicked() {
        threadPool.execute { saveButtonCallback() }
    }

    fun onFromItemSelected(pos: Int) {
        fromItemSelectedPosition = pos
    }

    fun onToItemSelected(pos: Int) {
        toItemSelectedPosition = pos
    }
}
