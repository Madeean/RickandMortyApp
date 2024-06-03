package com.example.rickandmortyapp.presentation.di

import com.madeean.domain.episode.EpisodeDomainUseCase
import com.madeean.domain.episode.EpisodeDomainUseCaseImpl
import com.madeean.domain.karakter.KarakterDomainUseCase
import com.madeean.domain.karakter.KarakterDomainUseCaseImpl
import com.madeean.domain.location.LocationDomainUseCase
import com.madeean.domain.location.LocationDomainUseCaseImpl
import com.madeean.domain.tmdb.TmdbDomainUseCase
import com.madeean.domain.tmdb.TmdbDomainUseCaseImpl
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