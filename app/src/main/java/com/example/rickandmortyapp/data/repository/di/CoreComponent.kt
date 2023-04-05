package com.example.rickandmortyapp.data.repository.di

import android.content.Context
import com.example.rickandmortyapp.domain.DomainRepository
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

    fun provideRepository(): DomainRepository
}