package com.example.rickandmortyapp.presentation.di

import com.example.rickandmortyapp.data.repository.di.CoreComponent
import com.example.rickandmortyapp.presentation.activity.DetailEpisodeActivity
import com.example.rickandmortyapp.presentation.activity.MainActivity
import dagger.Component

@AppScope
@Component(
    dependencies = [CoreComponent::class], modules = [AppModule::class]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }

    fun mainActivityInject(activity: MainActivity)
    fun detailEpisodeActivity(activity: DetailEpisodeActivity)
}