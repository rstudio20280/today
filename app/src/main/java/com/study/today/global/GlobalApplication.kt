package com.study.today.global

import android.app.Application
import timber.log.Timber

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}