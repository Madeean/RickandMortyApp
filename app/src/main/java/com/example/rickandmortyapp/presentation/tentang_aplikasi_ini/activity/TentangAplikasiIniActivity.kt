package com.example.rickandmortyapp.presentation.tentang_aplikasi_ini.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityTentangAplikasiIniBinding

class TentangAplikasiIniActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTentangAplikasiIniBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTentangAplikasiIniBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
    }

    private fun setToolbar() {
        binding.tentangAplikasiToolbar.apply {
            tvToolbar.text = getString(R.string.tentang_aplikasi_ini)
            ivBackToolbar.setOnClickListener { finish() }
        }
    }
}