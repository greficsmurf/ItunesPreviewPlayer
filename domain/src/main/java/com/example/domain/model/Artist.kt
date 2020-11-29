package com.example.domain.model

data class Artist(
    val artistName: String,
    val artistId: Long,
    val artistLinkUrl: String?,
    val amgArtistId: Long?,
    val primaryGenreName: String,
    val primaryGenreId: Long?
)