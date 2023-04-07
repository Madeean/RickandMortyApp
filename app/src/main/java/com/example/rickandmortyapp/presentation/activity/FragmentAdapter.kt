package com.example.rickandmortyapp.presentation.activity

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rickandmortyapp.presentation.fragment.HomeFragment
import com.example.rickandmortyapp.presentation.fragment.KarakterFragment
import com.example.rickandmortyapp.presentation.fragment.LocationFragment
import com.example.rickandmortyapp.presentation.fragment.SettingFragment
import com.example.rickandmortyapp.presentation.viewmodel.episode.EpisodeViewModel
import com.example.rickandmortyapp.presentation.viewmodel.karakter.KarakterViewModel
import com.example.rickandmortyapp.presentation.viewmodel.location.LocationViewModel

class FragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val application: Application,
    private val episodeViewModel: EpisodeViewModel,
    private val karakterViewModel: KarakterViewModel,
    private val locationViewModel: LocationViewModel
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragmentList = arrayListOf<Fragment>()

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment.newInstance(episodeViewModel, application)
            1 -> KarakterFragment.newInstance(karakterViewModel, application)
            2 -> LocationFragment.newInstance(locationViewModel, application)
            3 -> SettingFragment()
            else -> HomeFragment.newInstance(episodeViewModel, application)
        }
    }
}