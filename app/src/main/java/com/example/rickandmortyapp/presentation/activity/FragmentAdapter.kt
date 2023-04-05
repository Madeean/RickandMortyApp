package com.example.rickandmortyapp.presentation.activity

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rickandmortyapp.presentation.fragment.HomeFragment
import com.example.rickandmortyapp.presentation.fragment.KarakterFragment
import com.example.rickandmortyapp.presentation.viewmodel.EpisodeViewModel

class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle,private val application: Application, private val episodeViewModel: EpisodeViewModel) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragmentList = arrayListOf<Fragment>()

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
//        return fragmentList[position]
        return when (position) {
            0 -> HomeFragment.newInstance(episodeViewModel,application)
            1 -> KarakterFragment()
            else -> HomeFragment.newInstance(episodeViewModel,application)
        }
    }
}