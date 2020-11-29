package com.example.domain.resource

import kotlinx.coroutines.flow.flow
import java.lang.Exception

abstract class NetworkResource<T> {
    private val result = flow<Resource<T>> {
        emit(Resource.loading(null))
        try{
            emit(Resource.loaded(fetch()))
        }catch (e: Exception){
            e.printStackTrace()
            emit(onError(e))
        }
    }

    fun asFlow() = result

    abstract suspend fun fetch(): T

    protected open fun onError(e: Exception): Resource<T> = Resource.failedEx(e)
}