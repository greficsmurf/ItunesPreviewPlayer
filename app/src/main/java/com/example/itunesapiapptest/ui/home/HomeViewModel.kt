package com.example.itunesapiapptest.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.domain.interactor.GetAlbumsUseCase
import com.example.domain.interactor.GetSearchResultUseCase
import com.example.domain.model.ItunesSearchParams
import com.example.itunesapiapptest.liveevent.LiveEvent

class HomeViewModel @ViewModelInject constructor(
    private val getSearchResultUseCase: GetSearchResultUseCase
) : ViewModel(){

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String>
        get() = _searchText

    val searchResource = _searchText.switchMap { text ->
        getSearchResultUseCase.invoke(ItunesSearchParams(text)).asLiveData()
    }

    val albumsData = searchResource.map {res ->
        res.data?.albums?.take(3)
    }
    val songsData = searchResource.map {res ->
        res.data?.songs?.take(3)
    }
    val artistData = searchResource.map {res ->
        res.data?.artists?.take(3)
    }

    fun setSearchText(searchText: String){
        if(searchText != _searchText.value && !searchText.isBlank()){
            _searchText.postValue(processSearchText(searchText))
        }
    }

    private val _navigateToAllAlbums = LiveEvent<Boolean>()
    val navigateToAllAlbums: LiveData<Boolean>
        get() = _navigateToAllAlbums

    private val _navigateToAllSongs = LiveEvent<Boolean>()
    val navigateToAllSongs: LiveData<Boolean>
        get() = _navigateToAllSongs

    fun onAllAlbumsClicked(){
        _navigateToAllAlbums.setValue(true)
    }
    fun onAllSongsClicked(){
        _navigateToAllSongs.setValue(true)
    }
    private fun processSearchText(text: String) = text.split(" ").joinToString("+")


}