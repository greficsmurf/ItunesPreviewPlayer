package com.example.itunesapiapptest.ui.songs

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.domain.interactor.LookUpSongsUseCase
import com.example.domain.interactor.GetSongsUseCase
import com.example.domain.model.ItunesLookupParams
import com.example.domain.model.ItunesSearchParams
import com.example.domain.model.Song
import com.example.domain.resource.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SongsViewModel @ViewModelInject constructor(
        private val getAlbumSongsUseCase: LookUpSongsUseCase,
        private val getSongsUseCase: GetSongsUseCase
) : ViewModel(){

    private val _searchTerm = MutableLiveData<String>()

    val songsResource = _searchTerm.switchMap {term ->
        getSongsUseCase.invoke(
                ItunesSearchParams(
                        term
                )
        ).asLiveData()
    }

    val songsData = this.songsResource.map { res ->
        res.data
    }

    fun setSearchTerm(term: String?){
        if(!term.isNullOrBlank())
            _searchTerm.postValue(term)
    }
}