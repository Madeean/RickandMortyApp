package com.example.rickandmortyapp.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityMainBinding
import com.example.rickandmortyapp.presentation.fragment.HomeFragment
import com.example.rickandmortyapp.presentation.fragment.KarakterFragment
import com.example.rickandmortyapp.presentation.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.viewmodel.EpisodeViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var episodeFactory: EpisodeViewModelFactory
    private val episodeViewModel: EpisodeViewModel by viewModels {
        episodeFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.mainActivityInject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setViewPager()
        setBottomNavigationView()


    }

    private fun setBottomNavigationView() {
        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> binding.viewPager2.currentItem = 0
                R.id.person_search -> binding.viewPager2.currentItem = 1
            }
            true
        }
    }

    private fun setViewPager() {
        val adapter = FragmentAdapter(supportFragmentManager, lifecycle,application,episodeViewModel)
        adapter.addFragment(HomeFragment())
        adapter.addFragment(KarakterFragment())

        binding.viewPager2.adapter = adapter

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.navView.menu.getItem(position).isChecked = true
            }
        })
    }

}