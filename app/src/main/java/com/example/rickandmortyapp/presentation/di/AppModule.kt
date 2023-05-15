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

@Module
abstract class AppModule {
    @Binds
    abstract fun provideEpisodeDomainUseCase(useCaseImpl: EpisodeDomainUseCaseImpl): EpisodeDomainUseCase
    @Binds
    abstract fun provideKarakterDomainUseCase(useCaseImpl: KarakterDomainUseCaseImpl): KarakterDomainUseCase
    @Binds
    abstract fun provideLocationDomainUseCase(useCaseImpl: LocationDomainUseCaseImpl): LocationDomainUseCase
    @Binds
    abstract fun provideTmdbDomainUseCase(useCaseImpl: TmdbDomainUseCaseImpl): TmdbDomainUseCase

}