package com.example.data.model

import com.squareup.moshi.Json

data class ArtistApi(

    val artistName: String?,
    val artistId: Long?,
    val artistLinkUrl: String?,
    val amgArtistId: Long?,
    val primaryGenreName: String?,
    val primaryGenreId: Long?
)