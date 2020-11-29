package com.example.itunesapiapptest.di

import com.example.data.api.ItunesService
import com.example.data.repository.ItunesRepositoryImpl
import com.example.domain.repository.ItunesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@InstallIn(ApplicationComponent::class)
@Module
abstract class DataModule {
    @Binds
    abstract fun bindItunesRepositoryImpl(repo: ItunesRepositoryImpl): ItunesRepository
}