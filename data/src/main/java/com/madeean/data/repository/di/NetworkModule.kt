package com.madeean.data.repository.di

import com.madeean.data.DataUtils.BASE_URL
import com.madeean.data.DataUtils.BASE_URL_TMDB
import com.madeean.data.repository.network.episode.EpisodeApiService
import com.madeean.data.repository.network.karakter.KarakterApiService
import com.madeean.data.repository.network.location.LocationApiService
import com.madeean.data.repository.network.tmbd.TmdbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @TmdbRetrofit // Tambahkan anotasi kualifier di sini
    fun provideTmdbRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_TMDB)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideTvShowApiService(retrofit: Retrofit): EpisodeApiService {
        return retrofit.create(EpisodeApiService::class.java)
    }

    @Provides
    fun provideKarakterApiService(retrofit: Retrofit): KarakterApiService {
        return retrofit.create(KarakterApiService::class.java)
    }

    @Provides
    fun provideLocationApiService(retrofit: Retrofit): LocationApiService {
        return retrofit.create(LocationApiService::class.java)
    }

    @Provides
    fun provideTmdbApiService(@TmdbRetrofit retrofit: Retrofit): TmdbApiService {
        return retrofit.create(TmdbApiService::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TmdbRetrofit
