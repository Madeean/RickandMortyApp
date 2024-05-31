package com.example.rickandmortyapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//open class MyApplication : Application() {
//
//    private val coreComponent: CoreComponent by lazy {
//        DaggerCoreComponent.factory().create(applicationContext)
//    }
//
//    val appComponent: AppComponent by lazy {
//        DaggerAppComponent.factory().create(coreComponent)
//    }
//
//}

@HiltAndroidApp
open class MyApplication : Application() {
    // Tidak perlu mendeklarasikan komponen secara manual
}