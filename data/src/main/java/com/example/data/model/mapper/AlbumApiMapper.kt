package com.example.data.model.mapper

import com.example.data.model.AlbumApi
import com.example.domain.model.Album

fun AlbumApi.toDomainModel(): Album = Album(
    artistId ?: -1,
    amgArtistId ?: 0,
    collectionId ?: -1,
    artistName ?: "",
    collectionName,
    collectionCensoredName ?: "",
    artworkUrl60 ?: "",
    collectionPrice ?: 0.0,
    trackCount ?: 0,
    copyright ?: "",
    country ?: "",
    currency ?: "",
    releaseDate ?: "",
    primaryGenreName ?: ""
)