package com.example.rickandmortyapp.presentation.daftarfavorit.activity

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityDaftarFavoriteBinding
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.factory.PresentationFactory
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class DaftarFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarFavoriteBinding
    private lateinit var tabs: List<String>

    @Inject
    lateinit var presentationFactory: PresentationFactory


    private val episodeViewModel: EpisodeViewModel by viewModels {
        presentationFactory
    }

    private val karakterViewModel: KarakterViewModel by viewModels {
        presentationFactory
    }

    private val locationViewModel: LocationViewModel by viewModels {
        presentationFactory
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


    fun getViewModelEpisode(): EpisodeViewModel {
        return episodeViewModel
    }

    fun getViewModelKarakter(): KarakterViewModel {
        return karakterViewModel
    }

    fun getViewmodelLocation(): LocationViewModel {
        return locationViewModel
    }

    fun getApplicationForApi(): Application {
        return application
    }

    private fun setToolbar() {
        binding.daftarFavoritToolbar.apply {
            tvToolbar.text = getString(R.string.daftar_favorite)
            ivBackToolbar.setOnClickListener{
                finish()
            }
        }

    }

    private fun initView() {
        binding.daftarFavoritViewPager2.adapter = FragmentAdapterDaftarFavorite(this)
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