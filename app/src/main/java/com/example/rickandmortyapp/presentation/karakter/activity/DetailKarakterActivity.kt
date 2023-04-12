package com.example.rickandmortyapp.presentation.karakter.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityDetailKarakterBinding
import com.example.rickandmortyapp.domain.model.episode.local.EpisodeItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemFavoriteModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.CODE_RESULT
import com.example.rickandmortyapp.presentation.episode.adapter.EpisodePagingAdapter
import com.example.rickandmortyapp.presentation.episode.activity.DetailEpisodeActivity
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModelFactory
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModelFactory
import com.example.rickandmortyapp.presentation.location.activity.DetailLocationActivity
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

    @Inject
    lateinit var karakterFactory: KarakterViewModelFactory
    private val karakterViewModel: KarakterViewModel by viewModels {
        karakterFactory
    }
    private var idEpisode = ""
    private var idLocation = 0
    private var idOrigin = 0
    private var dataFavorite: List<KarakterItemFavoriteModelRoom> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.detailKarakterActivity(this)
        binding = ActivityDetailKarakterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setProgressBar()
        setToolbar()
        setAdapter()
        getDataFromIntent()
        setFavorite()

        binding.btnDetailLocation.setOnClickListener {
            val intent = Intent(this, DetailLocationActivity::class.java)
            intent.putExtra(PresentationUtils.INTENT_ID, idLocation)
            startActivity(intent)
        }
        binding.btnDetailOrigin.setOnClickListener {
            val intent = Intent(this, DetailLocationActivity::class.java)
            intent.putExtra(PresentationUtils.INTENT_ID, idOrigin)
            startActivity(intent)
        }
    }

    private fun setFavorite() {
        getFavorite()
        insertFavorite()
    }

    private fun insertFavorite() {
        binding.cbFavoritDetailKarakter.setOnClickListener {
            if (binding.cbFavoritDetailKarakter.isChecked) {
                lifecycleScope.launch {
                    karakterViewModel.deleteKarakterFavoriteRoom(application, data?.id ?: -1)
                }
                Toast.makeText(this, "Berhasil menghapus favorite", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    karakterViewModel.insertKarakterFavoriteRoom(application, data?.id ?: -1)
                }
                Toast.makeText(this, "Berhasil menambah favorite", Toast.LENGTH_SHORT).show()
            }
            getFavorite()
        }
    }

    private fun getFavorite() {
        lifecycleScope.launch {
            dataFavorite = karakterViewModel.getKarakterFavoriteRoom(application)

            val isFavorite = dataFavorite.any {
                it.idKarakter == data?.id
            }

            if (isFavorite) {
                binding.cbFavoritDetailKarakter.setBackgroundResource(R.drawable.favorite_full)
                binding.cbFavoritDetailKarakter.isChecked = false
            } else {
                binding.cbFavoritDetailKarakter.setBackgroundResource(R.drawable.favorite_outline)
                binding.cbFavoritDetailKarakter.isChecked = true
            }

        }
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

        if (data?.location == null && data?.origin == null) {
            binding.btnDetailLocation.isEnabled = false
            binding.btnDetailOrigin.isEnabled = false
            return
        }

        if (data.origin?.url?.isBlank() == true) {
            binding.btnDetailOrigin.isEnabled = false
            idLocation = PresentationUtils.getIdFromUrl(data.location?.url ?: "")
        } else if (data.location?.url?.isBlank() == true) {
            binding.btnDetailLocation.isEnabled = false
            idOrigin = PresentationUtils.getIdFromUrl(data.origin?.url ?: "")
        } else {
            idLocation = PresentationUtils.getIdFromUrl(data.location?.url ?: "")
            idOrigin = PresentationUtils.getIdFromUrl(data.origin?.url ?: "")
        }

        if (data.episode?.isNotEmpty() == true) {
            idEpisode = ""
            data.episode.forEach {
                idEpisode += "${PresentationUtils.getIdFromUrl(it)},"
            }
            getDataFromApi()
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
            val intent = Intent()
            setResult(CODE_RESULT, intent)
            finish()
        }
    }

    private fun setProgressBar() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setView(R.layout.progress)
        dialog = alertDialog.create()
    }
}