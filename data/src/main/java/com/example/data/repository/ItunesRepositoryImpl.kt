package com.example.data.repository

import com.example.data.api.ItunesService
import com.example.data.model.mapper.filterAlbums
import com.example.data.model.mapper.filterArtists
import com.example.data.model.mapper.filterSongs
import com.example.data.model.mapper.toDomainModel
import com.example.domain.model.*
import com.example.domain.repository.ItunesRepository
import com.example.domain.resource.NetworkResource
import com.example.domain.resource.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItunesRepositoryImpl @Inject constructor(
    private val itunesService: ItunesService
) : ItunesRepository{

    override fun getAlbums(params: ItunesSearchParams): Flow<Resource<List<Album>>> = object : NetworkResource<List<Album>>(){
        override suspend fun fetch(): List<Album> {
                val res = itunesService.getAlbums(params.term)
                return res.results.map {
                    it.toDomainModel()
                }.sortedBy { it.collectionName }
            }
        }.asFlow()

    override fun getSongs(params: ItunesSearchParams): Flow<Resource<List<Song>>> = object : NetworkResource<List<Song>>(){
        override suspend fun fetch(): List<Song> {
            return itunesService.getSongs(params.term).results.map {
                it.toDomainModel()
            }
        }
    }.asFlow()

    override fun lookUpSongs(params: ItunesLookupParams): Flow<Resource<List<Song>>> = object : NetworkResource<List<Song>>(){
        override suspend fun fetch(): List<Song> {
            val res = itunesService.lookUpSongs(params.id).results
            return filterSongs(res).map { it.toDomainModel() }
        }
    }.asFlow()

    override fun getAll(params: ItunesSearchParams): Flow<Resource<SearchResult>> = object : NetworkResource<SearchResult>(){
        override suspend fun fetch(): SearchResult {
            val res = itunesService.getAll(params.term).results

            val albums = filterAlbums(res)
            val songs = filterSongs(res)
            val artists = filterArtists(res)

            return SearchResult(
                songs.map { it.toDomainModel() },
                albums.map { it.toDomainModel() },
                artists.map { it.toDomainModel() }
            )
        }
    }.asFlow()
}