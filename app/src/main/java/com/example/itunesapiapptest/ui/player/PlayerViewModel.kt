package com.example.itunesapiapptest.ui.player

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.domain.interactor.LookUpSongsUseCase
import com.example.domain.model.ItunesLookupParams
import com.example.domain.model.Song
import com.example.domain.resource.Resource
import com.example.itunesapiapptest.liveevent.LiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlayerViewModel @ViewModelInject constructor(
        private val lookUpSongsUseCase: LookUpSongsUseCase
) : ViewModel(){
    private var _startSongId  = ""
    private val _songId = MutableLiveData<String>()
    private val _albumId = MutableLiveData<String>()

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    private val _isPrepared = MutableLiveData<Boolean>()
    val isPrepared: LiveData<Boolean>
        get() = _isPrepared

    val playerResource = _isPrepared.map {
        if(_isPrepared.value == false){
            Resource.loading(null)
        }
        else{
            Resource.loaded(null)
        }
    }

    private val _isLeftEnd = MutableLiveData<Boolean>()
    val isLeftEnd: LiveData<Boolean>
        get() = _isLeftEnd

    private val _isRightEnd = MutableLiveData<Boolean>()
    val isRightEnd: LiveData<Boolean>
        get() = _isRightEnd

    private val _currentPlayerPos = MutableLiveData<Int>()
    val currentPlayerPos: LiveData<Int>
        get() = _currentPlayerPos

    private val _songsResource = MediatorLiveData<Resource<List<Song>>>()
    val songsResource: LiveData<Resource<List<Song>>>
        get() = _songsResource

    val songs = _songsResource.map {
        it.data
    }
    init {
        _songsResource.apply {
            addSource(_songId){
                _lookup.invoke(it)
            }
            addSource(_albumId){
                _lookup.invoke(it)
            }
        }

        _isPlaying.value = false
        _isPrepared.value = false

        songs.observeForever{}
    }
    private val _currentSong = MutableLiveData<Song>()

    val selectedSongIndex: Int
        get() = songs.value?.indexOfFirst {
            it.trackId == _currentSong.value?.trackId
        } ?: -1


    private val _lookup: (id: String) -> Unit = {id ->
        viewModelScope.launch {
            lookUpSongsUseCase.invoke(
                    ItunesLookupParams(
                            id
                    )
            ).collect {
                setCurrentSong(_startSongId, it.data ?: listOf())
                _songsResource.value = it
            }
        }
    }

    private fun setCurrentSong(id: String, songs: List<Song>){
        val intendedSong = songs.firstOrNull() {
            it.trackId.toString() == id
        }

        if(intendedSong?.trackId != _currentSong.value?.trackId)
            _currentSong.value = intendedSong
    }

    fun setLeftEnd(b: Boolean){
        if(_isLeftEnd.value != b)
            _isLeftEnd.value = b
    }
    fun setRightEnd(b: Boolean){
        if(_isRightEnd.value != b)
            _isRightEnd.value = b
    }
    fun setPlayingStatus(status: Boolean){
        _isPlaying.postValue(status)
    }
    fun setPreparedStatus(status: Boolean){
        _isPrepared.postValue(status)
    }
    fun setData(songId: String, albumId: String?){
        if(!albumId.isNullOrBlank()){
            _albumId.postValue(albumId)
            _startSongId = songId
        }
        else {
            _songId.postValue(songId)
        }
    }

    fun setPlayerPos(millis: Int){
        if(_currentPlayerPos.value != millis)
            _currentPlayerPos.postValue(millis)
    }




}