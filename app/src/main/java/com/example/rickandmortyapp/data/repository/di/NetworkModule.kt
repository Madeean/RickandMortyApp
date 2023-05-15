package com.example.rickandmortyapp.data.repository.di

import com.example.rickandmortyapp.data.DataUtils.BASE_URL
import com.example.rickandmortyapp.data.DataUtils.BASE_URL_TMDB
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeApiService
import com.example.rickandmortyapp.data.repository.network.karakter.KarakterApiService
import com.example.rickandmortyapp.data.repository.network.location.LocationApiService
import com.example.rickandmortyapp.data.repository.network.tmbd.TmdbApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    private fun retrofitClient(): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    private fun tmdbRetrofitClient(): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_TMDB)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun getTvShowApiService(): EpisodeApiService {
        return retrofitClient().create(EpisodeApiService::class.java)
    }

    @Provides
    fun getKarakterApiService(): KarakterApiService {
        return retrofitClient().create(KarakterApiService::class.java)
    }

    @Provides
    fun getLocationApiService(): LocationApiService {
        return retrofitClient().create(LocationApiService::class.java)
    }

    @Provides
    fun getTmdbApiService(): TmdbApiService {
        return tmdbRetrofitClient().create(TmdbApiService::class.java)
    }
}
