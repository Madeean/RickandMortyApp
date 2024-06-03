package com.madeean.data.repository.di

import com.madeean.data.repository.domain_repository.EpisodeDomainRepositoryImpl
import com.madeean.data.repository.domain_repository.KarakterDomainRepositoryImpl
import com.madeean.data.repository.domain_repository.LocationDomainRepositoryImpl
import com.madeean.data.repository.domain_repository.TmdbDomainRepositoryImpl
import com.madeean.domain.episode.EpisodeDomainRepository
import com.madeean.domain.karakter.KarakterDomainRepository
import com.madeean.domain.location.LocationDomainRepository
import com.madeean.domain.tmdb.TmdbDomainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindEpisodeRepository(repositoryImpl: EpisodeDomainRepositoryImpl): EpisodeDomainRepository

    @Binds
    abstract fun bindKarakterRepository(repositoryImpl: KarakterDomainRepositoryImpl): KarakterDomainRepository

    @Binds
    abstract fun bindLocationRepository(repositoryImpl: LocationDomainRepositoryImpl): LocationDomainRepository

    @Binds
    abstract fun bindTmdbRepository(repositoryImpl: TmdbDomainRepositoryImpl): TmdbDomainRepository
}