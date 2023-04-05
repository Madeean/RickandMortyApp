package com.example.rickandmortyapp

import android.app.Application
import com.example.rickandmortyapp.data.repository.di.CoreComponent
import com.example.rickandmortyapp.data.repository.di.DaggerCoreComponent
import com.example.rickandmortyapp.presentation.di.AppComponent
import com.example.rickandmortyapp.presentation.di.DaggerAppComponent

open class MyApplication : Application() {

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create(applicationContext)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(coreComponent)
    }

}