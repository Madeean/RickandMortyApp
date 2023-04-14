package com.example.rickandmortyapp.presentation.setting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentSettingBinding
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.GET_BOOLEAN_DARK_MODE
import com.example.rickandmortyapp.presentation.daftarfavorit.activity.DaftarFavoriteActivity
import com.example.rickandmortyapp.presentation.tentang_aplikasi_ini.activity.TentangAplikasiIniActivity


class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setSharedPreferences()
        setSwitchDarkMode()
        setDaftarFavorit()
        setTentangAplikasi()
    }

    private fun setTentangAplikasi() {
        binding.tvTentangAplikasi.setOnClickListener {
            startActivity(Intent(requireActivity(), TentangAplikasiIniActivity::class.java))
        }
    }

    private fun setDaftarFavorit() {
        binding.tvDaftarFavorit.setOnClickListener {
            startActivity(Intent(requireActivity(), DaftarFavoriteActivity::class.java))
        }
    }

    private fun setSwitchDarkMode() {
        binding.switchDarkMode.apply {
            isChecked = pref.getBoolean(GET_BOOLEAN_DARK_MODE, false)
            setOnCheckedChangeListener { _, isChecked ->
                val editor = pref.edit()
                if (isChecked) {
                    editor.putBoolean(GET_BOOLEAN_DARK_MODE, true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    editor.putBoolean(GET_BOOLEAN_DARK_MODE, false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                editor.apply()
            }
        }

    }

    private fun setSharedPreferences() {
        pref = requireActivity().getSharedPreferences(
            PresentationUtils.PREF_DARK_MODE, Context.MODE_PRIVATE
        )
    }


    private fun setToolbar() {
        binding.settingToolbar.tvToolbar.text = getString(R.string.setting)
    }


}