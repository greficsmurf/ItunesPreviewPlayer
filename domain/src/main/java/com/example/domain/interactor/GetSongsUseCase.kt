package com.example.domain.interactor

import com.example.domain.di.IoDispatcher
import com.example.domain.model.ItunesSearchParams
import com.example.domain.model.Song
import com.example.domain.repository.ItunesRepository
import com.example.domain.resource.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSongsUseCase @Inject constructor(
        private val itunesRepository: ItunesRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<ItunesSearchParams, List<Song>>(
        dispatcher
){
    override fun execute(params: ItunesSearchParams): Flow<Resource<List<Song>>> = itunesRepository.getSongs(params)
}