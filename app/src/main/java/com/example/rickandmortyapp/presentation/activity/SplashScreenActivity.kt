package com.example.rickandmortyapp.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModelFactory
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModelFactory
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashScreenActivity : AppCompatActivity() {

//    @Inject
//    lateinit var episodeFactory: EpisodeViewModelFactory
//    private val episodeViewModel: EpisodeViewModel by viewModels {
//        episodeFactory
//    }
//
//    @Inject
//    lateinit var karakterFactory: KarakterViewModelFactory
//    private val karakterViewModel: KarakterViewModel by viewModels {
//        karakterFactory
//    }
//
//    @Inject
//    lateinit var locationFactory: LocationViewModelFactory
//    private val locationViewModel: LocationViewModel by viewModels {
//        locationFactory
//    }
//
//    private var dataEpisode = ""
//    private var dataKarakter = ""
//    private var dataLocation = ""
    override fun onCreate(savedInstanceState: Bundle?) {
//        (application as MyApplication).appComponent.splashScreenActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

//        set timeour 3 second
//        Handler().postDelayed({
//
//        }, 3000)
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
//                startActivity(Intent(this, MainActivity::class.java))
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }, 3000)
        }

//        get data
//        lifecycleScope.launch {
//            Toast.makeText(this@SplashScreenActivity, "Loading", Toast.LENGTH_SHORT).show()
//            episodeViewModel.getAllEpisode(
//                application, ""
//            ).collect {
//                Toast.makeText(this@SplashScreenActivity, "Episode", Toast.LENGTH_SHORT).show()
//                dataEpisode = "episode"
//            }
//        }
//        lifecycleScope.launch {
//
//            karakterViewModel.getAllKarakter(
//                application, "", "", "", "", ""
//            ).collectLatest {
//                Toast.makeText(this@SplashScreenActivity, "Karakter", Toast.LENGTH_SHORT).show()
//                dataKarakter = "karakter"
//            }
//        }
//        lifecycleScope.launch {
//
//            locationViewModel.getAllLocation(
//                application, "", "", ""
//            ).collectLatest {
//                Toast.makeText(this@SplashScreenActivity, "Location", Toast.LENGTH_SHORT).show()
//                dataLocation = "location"
//            }
//        }
//
//        println(dataEpisode)
//        println(dataKarakter)
//        println(dataLocation)
//
//        if (dataEpisode.isNotBlank() && dataKarakter.isNotBlank() && dataLocation.isNotBlank()) {
//
//            startActivity(Intent(this, MainActivity::class.java))
//            val intent = Intent(this, MainActivity::class.java)
//            intent.flags =
//                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//            finish()
//        }


    }
}