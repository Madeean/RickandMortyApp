package com.example.rickandmortyapp.data.repository.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeDao
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeFavoriteModelRoom
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeModelRoom
import com.example.rickandmortyapp.data.repository.local.karakter.KarakterDao
import com.example.rickandmortyapp.data.repository.local.karakter.KarakterFavoriteModelRoom
import com.example.rickandmortyapp.data.repository.local.karakter.KarakterModelRoom
import com.example.rickandmortyapp.data.repository.local.location.LocationDao
import com.example.rickandmortyapp.data.repository.local.location.LocationFavoriteModelRoom
import com.example.rickandmortyapp.data.repository.local.location.LocationModelRoom
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context: Context): LocalModuleDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocalModuleDatabase::class.java,
            "rick_and_morty_db4"
        ).build()
    }

}

@Database(
    entities = [
        EpisodeModelRoom::class,
        KarakterModelRoom::class,
        LocationModelRoom::class,
        EpisodeFavoriteModelRoom::class,
        KarakterFavoriteModelRoom::class,
        LocationFavoriteModelRoom::class
    ],
    version = 1
)
abstract class LocalModuleDatabase : RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao
    abstract fun karakterDao(): KarakterDao
    abstract fun locationDao(): LocationDao
}