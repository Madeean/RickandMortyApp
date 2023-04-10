package com.example.rickandmortyapp.presentation.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentSettingBinding
import com.example.rickandmortyapp.presentation.daftarfavorit.activity.DaftarFavoriteActivity


class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        binding.tvDaftarFavorit.setOnClickListener {
            val intent = Intent(requireActivity(), DaftarFavoriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setToolbar() {
        binding.settingToolbar.tvToolbar.text = "Setting"
    }


}