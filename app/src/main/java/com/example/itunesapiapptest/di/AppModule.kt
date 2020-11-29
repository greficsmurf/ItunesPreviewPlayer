package com.example.itunesapiapptest.di

import android.app.Application
import androidx.room.Room
import com.example.data.api.ItunesService
import com.example.data.repository.ItunesRepositoryImpl
import com.example.domain.repository.ItunesRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class AppModule{

    @Singleton
    @Provides
    fun provideStackOverflowService(): ItunesService{
        @Suppress("SpellCheckingInspection")
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

        return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl("https://itunes.apple.com/")
                .build()
                .create(ItunesService::class.java)
    }


}