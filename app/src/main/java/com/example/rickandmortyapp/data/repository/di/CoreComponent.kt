package com.example.rickandmortyapp.data.repository.di

import android.content.Context
import com.example.rickandmortyapp.domain.episode.EpisodeDomainRepository
import com.example.rickandmortyapp.domain.karakter.KarakterDomainRepository
import com.example.rickandmortyapp.domain.location.LocationDomainRepository
import com.example.rickandmortyapp.domain.tmdb.TmdbDomainRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RepositoryModule::class]
)

interface CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CoreComponent
    }
    fun provideEpisodeRepository(): EpisodeDomainRepository
    fun provideKarakterRepository(): KarakterDomainRepository
    fun provideLocationRepository(): LocationDomainRepository
    fun provideTmdbRepository(): TmdbDomainRepository
}