package com.example.data.model

import com.squareup.moshi.Json

data class SongApi(

    val artistName: String?,
    val artistId: Long?,
    val artistLinkUrl: String?,
    val amgArtistId: Long?,
    val primaryGenreName: String?,
    val primaryGenreId: Long?,

    val collectionName: String?,
    val collectionId: Long?,
    val trackId: Long?,
    val previewUrl: String?,
    @Json(name = "artworkUrl60")
    val artWorkUrl60: String?,
    @Json(name = "artworkUrl100")
    val artWorkUrl100: String?,
    val collectionPrice: Double?,
    val trackPrice: Double?,
    val releaseDate: String?,
    val trackCount: Long?,
    val trackNumber: Long?,
    val trackTimeMillis: Long?,
    val country: String?,
    val currency: String?,
    val isStreamable: Boolean?,
    val copyright: String?,
    val trackName: String?
)