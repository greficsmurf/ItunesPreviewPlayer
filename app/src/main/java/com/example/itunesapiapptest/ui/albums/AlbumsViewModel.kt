package com.example.itunesapiapptest.ui.albums

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.domain.interactor.GetAlbumsUseCase
import com.example.domain.model.ItunesSearchParams

class AlbumsViewModel @ViewModelInject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel(){

    private val _albumSearchTerm = MutableLiveData<String>()

    val albumsResource = _albumSearchTerm.switchMap { term ->
        getAlbumsUseCase.invoke(
                ItunesSearchParams(
                        term
                )
        ).asLiveData()
    }

    val data = albumsResource.map { res ->
        res.data
    }

    fun setSearchText(text: String){
        if(text.isNotBlank())
            _albumSearchTerm.postValue(text)
    }
}