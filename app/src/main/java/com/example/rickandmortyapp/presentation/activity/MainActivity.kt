package com.example.rickandmortyapp.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityMainBinding
import com.example.rickandmortyapp.presentation.episode.fragment.HomeFragment
import com.example.rickandmortyapp.presentation.karakter.fragment.KarakterFragment
import com.example.rickandmortyapp.presentation.location.fragment.LocationFragment
import com.example.rickandmortyapp.presentation.setting.SettingFragment
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModelFactory
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModelFactory
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private var isNightModeOn = false

    @Inject
    lateinit var episodeFactory: EpisodeViewModelFactory
    private val episodeViewModel: EpisodeViewModel by viewModels {
        episodeFactory
    }

    @Inject
    lateinit var karakterFactory: KarakterViewModelFactory
    private val karakterViewModel: KarakterViewModel by viewModels {
        karakterFactory
    }

    @Inject
    lateinit var locationFactory: LocationViewModelFactory
    private val locationViewModel: LocationViewModel by viewModels {
        locationFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.mainActivityInject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
//        isNightModeOn = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        setViewPager()
        setBottomNavigationView()


    }

    private fun setBottomNavigationView() {
        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> binding.viewPager2.currentItem = 0
                R.id.person_search -> binding.viewPager2.currentItem = 1
                R.id.location -> binding.viewPager2.currentItem = 2
                R.id.setting -> binding.viewPager2.currentItem = 3
            }
            true
        }
    }

    private fun setViewPager() {
        val adapter = FragmentAdapter(
            supportFragmentManager, lifecycle, application, episodeViewModel, karakterViewModel,locationViewModel
        )
        adapter.addFragment(HomeFragment())
        adapter.addFragment(KarakterFragment())
        adapter.addFragment(LocationFragment())
        adapter.addFragment(SettingFragment())

        binding.viewPager2.adapter = adapter

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.navView.menu.getItem(position).isChecked = true
            }
        })
    }

}