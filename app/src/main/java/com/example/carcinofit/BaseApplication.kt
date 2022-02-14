package com.example.carcinofit

import android.app.Application
import android.util.Log
import androidx.databinding.library.BuildConfig
import com.example.carcinofit.other.AppSignatureHelper
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

//todo
// Retrofit with Hilt (Done partially)
// Floating action button in middle of bottom navigation (the functionality is provided by android only)
// Api for Carcino Feature

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
        super.onCreate()

        if (BuildConfig.DEBUG) {//Might create problem cause I might have imported wrong BuildCofig version
            AppSignatureHelper(this).appSignatures.forEach {
                Timber.d(it)
            }
        }
    }
}