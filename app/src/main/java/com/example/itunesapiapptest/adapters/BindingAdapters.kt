package com.example.itunesapiapptest.adapters

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.domain.resource.ResourceStatus
import com.example.itunesapiapptest.R
import dagger.BindsInstance


fun emptyGone(view: View, data: String){
    if(data.isBlank())
        setVisibility(view, false)
    else
        setVisibility(view, true)
}

@BindingAdapter("android:isVisible")
fun setVisibility(view: View, visibility: Boolean){
    view.visibility = if(visibility){
        View.VISIBLE
    }
    else{
        View.GONE
    }
}

@BindingAdapter("android:image")
fun setImage(view: ImageView, url: String?){
    Glide
            .with(view)
            .load(url)
            .error(
                    ContextCompat.getDrawable(view.context, R.drawable.ic_launcher_background)
            )
            .into(view)
}

@BindingAdapter("android:playStopDrawable")
fun playStopDrawable(view: View, isPlaying: Boolean){
    if(isPlaying)
        view.background = ContextCompat.getDrawable(view.context, R.drawable.drawable_stop_btn)
    else
        view.background = ContextCompat.getDrawable(view.context, R.drawable.drawable_play_btn)
}

@BindingAdapter("android:isVisible")
fun setVisibility(view: View, resourceStatus: ResourceStatus?){
    when(resourceStatus){
        ResourceStatus.LOADED -> setVisibility(view, false)
        ResourceStatus.FAILED -> setVisibility(view, false)
        ResourceStatus.LOADING -> setVisibility(view, true)
        ResourceStatus.AUTH_REJECTED -> setVisibility(view, false)
        null -> setVisibility(view, false)
    }
}

@BindingAdapter("android:isErrorVisible")
fun setErrorVisibility(view: View, resourceStatus: ResourceStatus?){
    when(resourceStatus){
        ResourceStatus.FAILED -> setVisibility(view, true)
        else -> setVisibility(view, false)
    }
}
