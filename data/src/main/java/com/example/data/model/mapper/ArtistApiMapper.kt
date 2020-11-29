package com.example.data.model.mapper

import com.example.data.model.ArtistApi
import com.example.domain.model.Artist

fun ArtistApi.toDomainModel(): Artist = Artist(
    artistName ?: "",
    artistId ?: -1,
    artistLinkUrl,
    amgArtistId,
    primaryGenreName ?: "",
    primaryGenreId
)