package com.example.domain.interactor

import com.example.domain.di.IoDispatcher
import com.example.domain.model.ItunesSearchParams
import com.example.domain.model.SearchResult
import com.example.domain.repository.ItunesRepository
import com.example.domain.resource.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchResultUseCase @Inject constructor(
    private val itunesRepository: ItunesRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<ItunesSearchParams, SearchResult>(dispatcher) {
    override fun execute(params: ItunesSearchParams): Flow<Resource<SearchResult>> {
        return itunesRepository.getAll(params)
    }
}