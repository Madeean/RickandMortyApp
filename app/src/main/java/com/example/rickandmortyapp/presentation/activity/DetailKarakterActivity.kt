package com.example.rickandmortyapp.presentation.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityDetailKarakterBinding
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.adapter.EpisodePagingAdapter
import com.example.rickandmortyapp.presentation.adapter.KarakterPagingAdapter
import com.example.rickandmortyapp.presentation.viewmodel.episode.EpisodeViewModel
import com.example.rickandmortyapp.presentation.viewmodel.episode.EpisodeViewModelFactory
import com.example.rickandmortyapp.presentation.viewmodel.karakter.KarakterViewModel
import com.example.rickandmortyapp.presentation.viewmodel.karakter.KarakterViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailKarakterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKarakterBinding
    private var data: KarakterModelItemModel? = null
    private lateinit var adapter: EpisodePagingAdapter
    private lateinit var dialog: Dialog


    @Inject
    lateinit var episodeFactory: EpisodeViewModelFactory
    private val episodeViewModel: EpisodeViewModel by viewModels {
        episodeFactory
    }
    private var idEpisode = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.detailKarakterActivity(this)
        binding = ActivityDetailKarakterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setProgressBar()
        setToolbar()
        setAdapter()
        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                PresentationUtils.INTENT_DATA, KarakterModelItemModel::class.java
            )
        } else {
            intent.getParcelableExtra(PresentationUtils.INTENT_DATA)
        }
        if (data != null) {
            setData(data)
            getDataFromApi()
        } else {
            showError("Data kosong")
        }
    }

    private fun getDataFromApi() {
        lifecycleScope.launch {
            episodeViewModel.getEpisodeById(application, idEpisode).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setData(data: KarakterModelItemModel?) {
        binding.apply {
            tvCreated.text = PresentationUtils.getCreated(data?.created ?: "")
            tvGender.text = data?.gender
            tvStatus.text = data?.status
            tvLocation.text = data?.location?.name
            tvName.text = data?.name
            tvOrigin.text = data?.origin?.name
            tvSpesies.text = data?.species
            tvType.text = if (data?.type?.isBlank() == true) "Unknown" else data?.type
        }
        Glide.with(this).load(data?.image).placeholder(R.drawable.ic_launcher_background)
            .into(binding.ivDetailImage)

        idEpisode = ""
        data?.episode?.forEach {
            idEpisode += "${PresentationUtils.getIdFromUrl(it)},"
        }


    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }

    private fun showError(error: String?) {
        PresentationUtils.setupDialogError(this, error ?: "").setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }.show()
    }

    private fun setAdapter() {
        adapter = EpisodePagingAdapter().apply {
            setOnItemClickListener { position, data ->
                val intent = Intent(this@DetailKarakterActivity, DetailEpisodeActivity::class.java)
                intent.putExtra(PresentationUtils.INTENT_DATA, data)
                startActivity(intent)
            }
        }
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                setLoading(true)
            } else {
                setLoading(false)
            }
            if (loadState.refresh is LoadState.Error) {
                setLoading(false)
                showError("Episode tidak ditemukan")

            }
        }
        binding.rvDetailKarakter.layoutManager = LinearLayoutManager(this)
        binding.rvDetailKarakter.adapter = adapter
    }

    private fun setToolbar() {
        binding.detailKarakterToolbar.tvToolbar.text = "Detail Karakter"
        binding.detailKarakterToolbar.ivBackToolbar.setOnClickListener {
            finish()
        }
    }

    private fun setProgressBar() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setView(R.layout.progress)
        dialog = alertDialog.create()
    }
}