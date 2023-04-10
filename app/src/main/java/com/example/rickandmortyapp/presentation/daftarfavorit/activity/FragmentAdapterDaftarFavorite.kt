package com.example.rickandmortyapp.presentation.daftarfavorit.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rickandmortyapp.presentation.daftarfavorit.fragmentdaftarfavorit.EpisodeDaftarFavoriteFragment
import com.example.rickandmortyapp.presentation.daftarfavorit.fragmentdaftarfavorit.KarakterDaftarFavoriteFragment
import com.example.rickandmortyapp.presentation.daftarfavorit.fragmentdaftarfavorit.LocationDaftarFavoriteFragment

class FragmentAdapterDaftarFavorite(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EpisodeDaftarFavoriteFragment()
            1 -> KarakterDaftarFavoriteFragment()
            2 -> LocationDaftarFavoriteFragment()
            else -> EpisodeDaftarFavoriteFragment()
        }
    }


}