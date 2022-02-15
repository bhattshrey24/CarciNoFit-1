package com.example.carcinofit

import android.app.Application
import android.util.Log
import androidx.databinding.library.BuildConfig
import com.example.carcinofit.other.AppSignatureHelper
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber



@HiltAndroidApp
class BaseApplication : Application() {// This is the 'Application' Class

    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
        super.onCreate()

        if (BuildConfig.DEBUG) {
            AppSignatureHelper(this).appSignatures.forEach {
                Timber.d(it)
            }
        }
    }
}