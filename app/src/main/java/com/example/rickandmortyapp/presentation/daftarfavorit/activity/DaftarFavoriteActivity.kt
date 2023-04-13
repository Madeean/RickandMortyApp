package com.example.rickandmortyapp.presentation.daftarfavorit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityDaftarFavoriteBinding
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModelFactory
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModelFactory
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class DaftarFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarFavoriteBinding
    private lateinit var tabs: List<String>

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
        (application as MyApplication).appComponent.daftarFavoriteActivity(this)
        binding = ActivityDaftarFavoriteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        initTabs()
        initView()
    }
    private fun setToolbar() {
        binding.daftarFavoritToolbar.tvToolbar.text = getString(R.string.daftar_favorite)
        binding.daftarFavoritToolbar.ivBackToolbar.setOnClickListener{
            finish()
        }
    }

    private fun initView() {
        binding.daftarFavoritViewPager2.adapter = FragmentAdapterDaftarFavorite(this, application, episodeViewModel, karakterViewModel,locationViewModel)
        TabLayoutMediator(
            binding.daftarFavoritTabLayout, binding.daftarFavoritViewPager2
        ) { tab, pos ->
            tab.text = tabs[pos]
        }.attach()
    }

    private fun initTabs() {
        tabs = listOf(
            "Episode", "Character", "Location"
        )
    }
}