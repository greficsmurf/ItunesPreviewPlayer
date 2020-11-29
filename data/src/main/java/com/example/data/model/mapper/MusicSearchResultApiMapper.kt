package com.example.data.model.mapper

import com.example.data.model.AlbumApi
import com.example.data.model.ArtistApi
import com.example.data.model.MusicSearchResultApi
import com.example.data.model.SongApi
import com.example.domain.model.Album

const val ARTIST_TYPE = "artist"
const val SONG_TYPE = "track"
const val ALBUM_TYPE = "collection"

fun filterAlbums(list: List<MusicSearchResultApi>): List<AlbumApi> = list.filter {
    it.type == ALBUM_TYPE
}.map {
    with(it){
        AlbumApi(
            artistId,
            amgArtistId,
            collectionId ?: -1,
            artistName,
            collectionName ?: "",
            null,
            artWorkUrl100,
            collectionPrice,
            trackCount,
            copyright,
            country,
            currency,
            releaseDate,
            primaryGenreName
        )
    }
}

fun filterSongs(list: List<MusicSearchResultApi>): List<SongApi> = list.filter {
    it.type == SONG_TYPE
}.map {
    with(it){
        SongApi(
            artistName,
            artistId,
            artistLinkUrl,
            amgArtistId,
            primaryGenreName,
            primaryGenreId,
            collectionName,
            collectionId,
            trackId,
            previewUrl,
            artWorkUrl60,
            artWorkUrl100,
            collectionPrice,
            trackPrice,
            releaseDate,
            trackCount,
            trackNumber,
            trackTimeMillis,
            country,
            currency,
            isStreamable,
            copyright,
            trackName
        )
    }
}



fun filterArtists(list: List<MusicSearchResultApi>): List<ArtistApi> = list.filter {
    it.type == ARTIST_TYPE
}.map {
    with(it){
        ArtistApi(
            artistName,
            artistId,
            artistLinkUrl,
            amgArtistId,
            primaryGenreName,
            primaryGenreId
        )
    }
}