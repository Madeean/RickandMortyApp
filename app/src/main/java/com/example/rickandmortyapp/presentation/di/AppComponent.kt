package com.example.rickandmortyapp.presentation.di

import com.example.rickandmortyapp.data.repository.di.CoreComponent
import com.example.rickandmortyapp.presentation.episode.activity.DetailEpisodeActivity
import com.example.rickandmortyapp.presentation.karakter.activity.DetailKarakterActivity
import com.example.rickandmortyapp.presentation.activity.MainActivity
import com.example.rickandmortyapp.presentation.daftarfavorit.activity.DaftarFavoriteActivity
import com.example.rickandmortyapp.presentation.location.activity.DetailLocationActivity
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
    fun detailKarakterActivity(activity: DetailKarakterActivity)
    fun detailLocationActivity(activity: DetailLocationActivity)
    fun daftarFavoriteActivity(activity: DaftarFavoriteActivity)
}