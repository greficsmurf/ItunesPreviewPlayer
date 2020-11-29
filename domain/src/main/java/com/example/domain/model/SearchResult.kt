package com.example.domain.model

data class SearchResult(
    val songs: List<Song>,
    val albums: List<Album>,
    val artists: List<Artist>
)