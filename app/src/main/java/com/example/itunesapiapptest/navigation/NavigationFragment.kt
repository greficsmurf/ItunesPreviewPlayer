package com.example.itunesapiapptest.navigation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.itunesapiapptest.R
import com.example.itunesapiapptest.base.BaseFragment

abstract class NavigationFragment : BaseFragment(){

    private var navHost: NavigationHost? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is NavigationHost)
            navHost = context
    }

    override fun onDetach() {
        super.onDetach()
        navHost = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar(view)
    }

    protected fun setUpToolbar(view: View){
        val host = navHost ?: return

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar) ?: return
        host.registerToolbarWithNav(toolbar)
    }

}