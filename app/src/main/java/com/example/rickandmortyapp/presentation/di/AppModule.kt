package com.example.rickandmortyapp.presentation.di

import com.example.rickandmortyapp.domain.DomainUseCase
import com.example.rickandmortyapp.domain.DomainUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {
    @Binds
    abstract fun provideDomainUseCase(useCaseImpl: DomainUseCaseImpl): DomainUseCase
}