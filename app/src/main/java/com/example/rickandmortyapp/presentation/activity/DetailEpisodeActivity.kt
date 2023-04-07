package com.example.rickandmortyapp.presentation.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityDetailEpisodeBinding
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.INTENT_DATA
import com.example.rickandmortyapp.presentation.PresentationUtils.getCreated
import com.example.rickandmortyapp.presentation.adapter.KarakterPagingAdapter
import com.example.rickandmortyapp.presentation.viewmodel.karakter.KarakterViewModel
import com.example.rickandmortyapp.presentation.viewmodel.karakter.KarakterViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailEpisodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEpisodeBinding
    private var data: EpisodeModelItemModel? =null
    private lateinit var adapter: KarakterPagingAdapter

    @Inject
    lateinit var karakterFactory: KarakterViewModelFactory
    private val karakterViewModel: KarakterViewModel by viewModels {
        karakterFactory
    }

    private var characterArray = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.detailEpisodeActivity(this)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEpisodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        setAdapter()
        getDataFromIntent()
    }

    private fun setAdapter() {
        adapter = KarakterPagingAdapter()
        binding.rvDetailEpisode.layoutManager  = GridLayoutManager(this, 2)
        binding.rvDetailEpisode.adapter = adapter
    }

    private fun getDataFromIntent() {
        data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(INTENT_DATA, EpisodeModelItemModel::class.java)
        } else {
            intent.getParcelableExtra(INTENT_DATA)
        }
        if(data != null){
            setData(data)
            getDataFromApi()
        }else{
            showError("Data kosong")
        }
    }

    private fun getDataFromApi() {

    }

    private fun setData(data: EpisodeModelItemModel?) {
        binding.apply {
            tvEpisode.text = data?.episode
            tvName.text = data?.name
            tvAirDate.text = data?.airDate
            tvCreated.text = data?.created
            tvCreated.text = getCreated(data?.created ?: "")
        }
        characterArray.clear()
        data?.characterList?.forEach {
            characterArray.add(it)
        }
    }

    private fun showError(error: String?) {
        PresentationUtils.setupDialogError(this, error ?: "")
            .setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }.show()
    }

    private fun setToolbar() {
        binding.detailEpisodeToolbar.tvToolbar.text = "Detail Episode"
        binding.detailEpisodeToolbar.ivBackToolbar.setOnClickListener {
            finish()
        }
    }
}