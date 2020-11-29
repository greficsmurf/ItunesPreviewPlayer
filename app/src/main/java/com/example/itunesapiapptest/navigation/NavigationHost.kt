package com.example.itunesapiapptest.navigation

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController

interface NavigationHost {
    fun registerToolbarWithNav(toolbar: Toolbar)
}