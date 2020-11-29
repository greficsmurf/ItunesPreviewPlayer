package com.example.domain.interactor

import com.example.domain.di.IoDispatcher
import com.example.domain.model.Album
import com.example.domain.model.ItunesSearchParams
import com.example.domain.repository.ItunesRepository
import com.example.domain.resource.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val itunesRepository: ItunesRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<ItunesSearchParams, List<Album>>(
    dispatcher
) {
    override fun execute(params: ItunesSearchParams): Flow<Resource<List<Album>>> {
        return itunesRepository.getAlbums(params)
    }
}