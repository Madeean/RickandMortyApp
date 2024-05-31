package com.example.rickandmortyapp.presentation.activity

import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityMainBinding
import com.example.rickandmortyapp.databinding.TentangAplikasiIniDialogBinding
import com.example.rickandmortyapp.presentation.episode.fragment.HomeFragment
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.factory.PresentationFactory
import com.example.rickandmortyapp.presentation.karakter.fragment.KarakterFragment
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.location.fragment.LocationFragment
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel
import com.example.rickandmortyapp.presentation.setting.SettingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tentangAplikasiDialog: Dialog

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setDialogTentangAplikasi()
        setContentView(binding.root)
        setViewPager()
        setBottomNavigationView()
    }

    private fun setDialogTentangAplikasi() {
        val builder = AlertDialog.Builder(this)
        val tentangAplikasiIniBinding = TentangAplikasiIniDialogBinding.inflate(layoutInflater)
        val view = tentangAplikasiIniBinding.root
        tentangAplikasiIniBinding.ivClose.setOnClickListener {
            tentangAplikasiDialog.dismiss()
        }
        builder.setView(view)
        tentangAplikasiDialog = builder.create()
        tentangAplikasiDialog.run {
            setCancelable(false)
            show()
        }
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
            supportFragmentManager, lifecycle
        )
        adapter.apply {
            addFragment(HomeFragment())
            addFragment(KarakterFragment())
            addFragment(LocationFragment())
            addFragment(SettingFragment())
        }

        binding.apply {
            viewPager2.adapter = adapter

            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    navView.menu.getItem(position).isChecked = true
                }
            })
        }

    }

}