package com.robin729.assignment

import android.app.Application
import com.robin729.assignment.util.StoreSession

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        StoreSession.init(applicationContext)
    }
}