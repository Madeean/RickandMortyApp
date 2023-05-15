package com.example.rickandmortyapp.data.repository.di

import com.example.rickandmortyapp.data.repository.domain_repository.EpisodeDomainRepositoryImpl
import com.example.rickandmortyapp.data.repository.domain_repository.KarakterDomainRepositoryImpl
import com.example.rickandmortyapp.data.repository.domain_repository.LocationDomainRepositoryImpl
import com.example.rickandmortyapp.data.repository.domain_repository.TmdbDomainRepositoryImpl
import com.example.rickandmortyapp.domain.episode.EpisodeDomainRepository
import com.example.rickandmortyapp.domain.karakter.KarakterDomainRepository
import com.example.rickandmortyapp.domain.location.LocationDomainRepository
import com.example.rickandmortyapp.domain.tmdb.TmdbDomainRepository
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class,LocalModule::class])
abstract class RepositoryModule {
    @Binds
    abstract fun provideEpisodeRepository(repositoryImpl: EpisodeDomainRepositoryImpl): EpisodeDomainRepository
    @Binds
    abstract fun provideKarakterRepository(repositoryImpl: KarakterDomainRepositoryImpl): KarakterDomainRepository
    @Binds
    abstract fun provideLocationRepository(repositoryImpl: LocationDomainRepositoryImpl): LocationDomainRepository
    @Binds
    abstract fun provideTmdbRepository(repositoryImpl: TmdbDomainRepositoryImpl): TmdbDomainRepository
}