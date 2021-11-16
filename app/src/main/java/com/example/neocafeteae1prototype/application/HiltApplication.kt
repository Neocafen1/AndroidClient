package com.example.neocafeteae1prototype.application

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: HiltApplication
        fun getContext(): Context? {
            return instance.applicationContext
        }
    }

}