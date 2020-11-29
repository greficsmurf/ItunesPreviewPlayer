package com.example.domain.resource

import java.lang.Exception
import java.net.UnknownHostException

data class Resource<T>(
    val data: T?,
    val status: ResourceStatus,
    val msg: String
){
    companion object{
        fun <T> loading(data: T?, msg: String = "") = Resource(
                data,
                ResourceStatus.LOADING,
                msg
        )
        fun <T> loaded(data: T?, msg: String = "") = Resource(
                data,
                ResourceStatus.LOADED,
                msg
        )
        fun <T> failed(data: T?, msg: String = "") = Resource(
                data,
                ResourceStatus.FAILED,
                msg
        )
        fun <T> authFailed(data: T?, msg: String = "") = Resource(
                data,
                ResourceStatus.AUTH_REJECTED,
                msg
        )

        fun <T> failedEx(e: Exception): Resource<T> {

            return if(e is UnknownHostException){
                failed(null,  "Check you internet connection")
            }else
            {
                failed(null,  e.localizedMessage ?: "")
            }
        }
    }
}