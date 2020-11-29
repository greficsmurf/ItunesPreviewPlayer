package com.example.itunesapiapptest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.itunesapiapptest.base.BaseActivity
import com.example.itunesapiapptest.navigation.NavigationHost
import com.example.itunesapiapptest.navigation.navigateSafe
import com.example.itunesapiapptest.ui.album.AlbumFragmentDirections
import com.example.itunesapiapptest.ui.home.HomeFragmentDirections
import dagger.hilt.EntryPoint

class MainActivity : BaseActivity(), NavigationHost{

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    private val appBarConf: AppBarConfiguration by lazy {
        AppBarConfiguration(navController.graph)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent?.apply {
            val trackId = extras?.getLong("trackId")
            val albumId = extras?.getLong("albumId")

            if(trackId != null || albumId != null){
                navController.navigateSafe(
                        HomeFragmentDirections.actionHomeFragmentToPlayerFragment(
                                trackId.toString(),
                                albumId.toString()
                        )
                )
            }
        }
    }

    override fun registerToolbarWithNav(toolbar: Toolbar) {
        toolbar.setupWithNavController(navController, appBarConf)
    }
}