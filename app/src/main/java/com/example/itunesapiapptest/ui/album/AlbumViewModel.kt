package com.example.itunesapiapptest.ui.album

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.domain.interactor.LookUpSongsUseCase
import com.example.domain.model.Album
import com.example.domain.model.ItunesLookupParams
import com.example.domain.model.Song
import com.example.domain.resource.Resource

class AlbumViewModel @ViewModelInject constructor(
        private val getAlbumSongsUseCase: LookUpSongsUseCase
) : ViewModel(){

    private val _lookupId = MutableLiveData<String>()
    val songsResource = _lookupId.switchMap { id ->
        getAlbumSongsUseCase.invoke(
                ItunesLookupParams(
                        id
                )
        ).asLiveData()
    }

    val songsData = songsResource.map { res ->
        res.data
    }

    val albumData = songsData.map { data ->
        with(data?.firstOrNull()){
            this?.let {
                Album(
                        artist.artistId,
                        artist.amgArtistId ?: -1,
                        collectionId ?: -1,
                        artist.artistName,
                        collectionName,
                        collectionName,
                        artWorkUrl100,
                        collectionPrice ?: -1.0,
                        trackCount,
                        copyright,
                        country,
                        currency,
                        releaseDate ?: "",
                        primaryGenre
                )
            }
        }
    }

    fun setLookupId(id: String?){
        if(!id.isNullOrBlank())
            _lookupId.postValue(id)
    }
}