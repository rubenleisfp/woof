package com.example.woof


import android.app.Application
import com.example.woof.data.AppContainer
import com.example.woof.data.DefaultAppContainer


class WoofApplication : Application() {
    /** Usado por el resto de clases para obtener dependencias */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
