package com.example.domain.model

data class  Song(
    val artist: Artist,

    val collectionName: String,
    val collectionId: Long?,
    val trackId: Long,
    val previewUrl: String,
    val artWorkUrl60: String,
    val artWorkUrl100: String,
    val collectionPrice: Double?,
    val trackPrice: Double?,
    val releaseDate: String?,
    val trackCount: Long,
    val trackNumber: Long,
    val trackTimeMillis: Long,
    val country: String,
    val currency: String,
    val isStreamable: Boolean,
    val copyright: String,
    val trackName: String,
    val primaryGenre: String
)