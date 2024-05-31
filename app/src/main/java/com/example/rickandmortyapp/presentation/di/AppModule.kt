package com.example.rickandmortyapp.presentation.di

import com.example.rickandmortyapp.domain.episode.EpisodeDomainUseCase
import com.example.rickandmortyapp.domain.episode.EpisodeDomainUseCaseImpl
import com.example.rickandmortyapp.domain.karakter.KarakterDomainUseCase
import com.example.rickandmortyapp.domain.karakter.KarakterDomainUseCaseImpl
import com.example.rickandmortyapp.domain.location.LocationDomainUseCase
import com.example.rickandmortyapp.domain.location.LocationDomainUseCaseImpl
import com.example.rickandmortyapp.domain.tmdb.TmdbDomainUseCase
import com.example.rickandmortyapp.domain.tmdb.TmdbDomainUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideEpisodeDomainUseCase(useCaseImpl: EpisodeDomainUseCaseImpl): EpisodeDomainUseCase

    @Binds
    @Singleton
    abstract fun provideKarakterDomainUseCase(useCaseImpl: KarakterDomainUseCaseImpl): KarakterDomainUseCase

    @Binds
    @Singleton
    abstract fun provideLocationDomainUseCase(useCaseImpl: LocationDomainUseCaseImpl): LocationDomainUseCase

    @Binds
    @Singleton
    abstract fun provideTmdbDomainUseCase(useCaseImpl: TmdbDomainUseCaseImpl): TmdbDomainUseCase
}