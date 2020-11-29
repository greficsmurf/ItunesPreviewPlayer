package com.example.itunesapiapptest.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.itunesapiapptest.R
import java.lang.Exception
import java.util.*

fun <T> MutableLiveData<T>.forceRefresh(){
    this.value = this.value
}

//fun getResourceByLoadStates(loadStates: CombinedLoadStates): Resource<Nothing>{
//    when {
//        loadStates.prepend is LoadState.Loading -> return Resource.loading(null)
//        loadStates.append is LoadState.Loading -> return Resource.loading(null)
//        loadStates.refresh is LoadState.Loading -> return Resource.loading(null)
//    }
//    when {
//        loadStates.prepend is LoadState.Error -> return Resource.failed(null)
//        loadStates.append is LoadState.Error -> return Resource.failed(null)
//        loadStates.refresh is LoadState.Error -> return Resource.failed(null, msg = (loadStates.refresh as LoadState.Error).error.localizedMessage ?: "")
//    }
//
//    return Resource.loaded(null)
//}

fun Long.toDate() = Date().apply { time = this@toDate }


/**
 *  Gets drawable by its attribute reference
 */
fun getDrawableByRef(context: Context, attr: Int): Drawable{
    val typedValue = TypedValue()
    context.theme.resolveAttribute(attr, typedValue, true)
    val imageResId = typedValue.resourceId
    return ContextCompat.getDrawable(context, imageResId) ?: throw IllegalArgumentException("Cannot load drawable $imageResId")
}

fun Date?.timeElapsed(): Long = Date().time - (this?.time ?: 0)

fun NavController.navigateSafe(direction: NavDirections){
    try {
        navigate(direction)
    }catch (e: Exception){
    }
}

