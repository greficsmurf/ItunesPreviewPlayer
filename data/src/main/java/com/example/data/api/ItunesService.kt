package com.example.data.api

import com.example.data.model.AlbumWrapper
import com.example.data.model.MusicSearchResultWrapper
import com.example.data.model.SongWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService{
    @GET("search?entity=album")
    suspend fun getAlbums(@Query("term") term: String): AlbumWrapper

    @GET("search?entity=allArtist,song,album")
    suspend fun getAll(@Query("term") term: String): MusicSearchResultWrapper

    @GET("search?entity=song")
    suspend fun getSongs(@Query("term") term: String): SongWrapper

    @GET("lookup?entity=song")
    suspend fun lookUpSongs(@Query("id") id: String): MusicSearchResultWrapper
}