package com.example.domain.resource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.lang.Exception

abstract class DatabaseResource<DbT, DomainT>{
    private val result = flow<Resource<DomainT>> {
        emit(Resource.loading(null))
        try{
            fetch().collect { dbRes ->
                emit(Resource.loaded(toDomainModel(dbRes)))
            }
        }catch (e: Exception){
            emit(onError(e))
        }
    }

    fun asFlow() = result

    abstract suspend fun fetch(): Flow<DbT>
    abstract fun toDomainModel(data: DbT): DomainT

    protected open fun onError(e: Exception): Resource<DomainT> = Resource.failedEx(e)
}