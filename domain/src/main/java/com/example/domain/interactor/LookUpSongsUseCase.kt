package com.example.domain.interactor

import com.example.domain.di.IoDispatcher
import com.example.domain.model.ItunesLookupParams
import com.example.domain.model.Song
import com.example.domain.repository.ItunesRepository
import com.example.domain.resource.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LookUpSongsUseCase @Inject constructor(
        private val itunesRepository: ItunesRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<ItunesLookupParams, List<Song>>(
        dispatcher
){
    override fun execute(params: ItunesLookupParams): Flow<Resource<List<Song>>> = itunesRepository.lookUpSongs(params)
}