package com.example.domain.interactor

import com.example.domain.resource.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

abstract class FlowUseCase<in P, R>(
    private val dispatcher: CoroutineDispatcher
) {
    fun invoke(params: P) = execute(params)
        .catch {
            e -> emit(Resource.failedEx(e as Exception))
        }.flowOn(dispatcher)

    protected abstract fun execute(params: P): Flow<Resource<R>>
}