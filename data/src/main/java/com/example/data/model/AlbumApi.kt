package com.example.data.model

import com.squareup.moshi.Json

data class AlbumApi(
    val artistId: Long?,
    val amgArtistId: Long?,
    val collectionId: Long?,
    val artistName: String?,
    val collectionName: String,
    val collectionCensoredName: String?,
    val artworkUrl60: String?,
    val collectionPrice: Double?,
    val trackCount: Long?,
    val copyright: String?,
    val country: String?,
    val currency: String?,
    val releaseDate: String?,
    @Json(name = "primaryGenreName")
    val primaryGenreName: String?
)