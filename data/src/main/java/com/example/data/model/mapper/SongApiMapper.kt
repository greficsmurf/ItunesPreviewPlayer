package com.example.data.model.mapper

import com.example.data.model.ArtistApi
import com.example.data.model.SongApi
import com.example.domain.model.Artist
import com.example.domain.model.Song

fun SongApi.toDomainModel(): Song = Song(
    ArtistApi(
        artistName,
        artistId,
        artistLinkUrl,
        amgArtistId,
        primaryGenreName,
        primaryGenreId
    ).toDomainModel(),

    collectionName ?: "",
    collectionId,
    trackId ?: -1,
    previewUrl ?: "",
    artWorkUrl60 ?: "",
    artWorkUrl100 ?: "",
    collectionPrice,
    trackPrice,
    releaseDate ?: "",
    trackCount ?: -1,
    trackNumber ?: -1,
    trackTimeMillis ?: -1,
    country ?: "",
    currency ?: "",
    isStreamable ?: false,
    copyright ?: "",
        trackName ?: "",
        primaryGenreName ?: ""
)