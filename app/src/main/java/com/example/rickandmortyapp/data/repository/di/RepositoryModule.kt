package com.example.rickandmortyapp.data.repository.di

import com.example.rickandmortyapp.data.repository.DomainRepositoryImpl
import com.example.rickandmortyapp.domain.DomainRepository
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class,LocalModule::class])
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepository(repositoryImpl: DomainRepositoryImpl): DomainRepository
}