package com.example.rickandmortyapp.presentation.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rickandmortyapp.presentation.episode.fragment.HomeFragment
import com.example.rickandmortyapp.presentation.karakter.fragment.KarakterFragment
import com.example.rickandmortyapp.presentation.location.fragment.LocationFragment
import com.example.rickandmortyapp.presentation.setting.SettingFragment

class FragmentAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
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
            0 -> HomeFragment()
            1 -> KarakterFragment()
            2 -> LocationFragment()
            3 -> SettingFragment()
            else -> HomeFragment()
        }
    }
}