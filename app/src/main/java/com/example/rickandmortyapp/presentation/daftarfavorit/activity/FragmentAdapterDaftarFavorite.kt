package com.example.rickandmortyapp.presentation.daftarfavorit.activity

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rickandmortyapp.presentation.daftarfavorit.fragmentdaftarfavorit.EpisodeDaftarFavoriteFragment
import com.example.rickandmortyapp.presentation.daftarfavorit.fragmentdaftarfavorit.KarakterDaftarFavoriteFragment
import com.example.rickandmortyapp.presentation.daftarfavorit.fragmentdaftarfavorit.LocationDaftarFavoriteFragment
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel

class FragmentAdapterDaftarFavorite(
    activity: AppCompatActivity,
    private val application: Application,
    private val episodeViewModel: EpisodeViewModel,
    private val karakterViewModel: KarakterViewModel,
    private val locationViewModel: LocationViewModel
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EpisodeDaftarFavoriteFragment.newInstance(episodeViewModel, application)
            1 -> KarakterDaftarFavoriteFragment.newInstance(karakterViewModel, application)
            2 -> LocationDaftarFavoriteFragment.newInstance(locationViewModel,application)
            else -> EpisodeDaftarFavoriteFragment.newInstance(episodeViewModel, application)
        }
    }


}