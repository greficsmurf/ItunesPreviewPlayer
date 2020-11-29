package com.example.domain.repository

import com.example.domain.model.*
import com.example.domain.resource.Resource
import kotlinx.coroutines.flow.Flow

interface ItunesRepository {

    fun getAlbums(params: ItunesSearchParams): Flow<Resource<List<Album>>>

    fun getSongs(params: ItunesSearchParams): Flow<Resource<List<Song>>>
    fun lookUpSongs(params: ItunesLookupParams): Flow<Resource<List<Song>>>

    fun getAll(params: ItunesSearchParams): Flow<Resource<SearchResult>>
}