package com.example.domain.resource

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.lang.Exception

abstract class NetworkDatabaseResource<T> {
    private val result = flow<Resource<T>> {
        emit(Resource.loading(null))
        coroutineScope {
            try{
                fetchDb().collect { dbData ->
                    if(shouldFetchApi(dbData)){
                        onDbSave(fetchApi())
                    }
                    else
                    {
                        emit(Resource.loaded(dbData))
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
                emit(onError(e))
            }
        }
    }

    fun asFlow() = result

    abstract suspend fun fetchDb(): Flow<T>

    abstract suspend fun fetchApi(): T

    abstract fun shouldFetchApi(data: T?): Boolean
    abstract suspend fun onDbSave(data: T)

    protected open fun onError(e: Exception): Resource<T> = Resource.failedEx(e)
}