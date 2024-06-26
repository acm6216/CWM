package me.qingshu.cwm

import android.app.Application

lateinit var app:Application
class App:Application(){

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}