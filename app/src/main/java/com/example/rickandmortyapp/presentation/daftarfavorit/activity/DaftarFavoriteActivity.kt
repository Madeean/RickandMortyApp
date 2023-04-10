package com.example.rickandmortyapp.presentation.daftarfavorit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.databinding.ActivityDaftarFavoriteBinding
import com.google.android.material.tabs.TabLayoutMediator

class DaftarFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarFavoriteBinding
    private lateinit var tabs: List<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.daftarFavoriteActivity(this)
        binding = ActivityDaftarFavoriteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initTabs()
        initView()
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