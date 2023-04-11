package com.example.rickandmortyapp.presentation.episode.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityDetailEpisodeBinding
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.episode.local.EpisodeItemFavoriteModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.INTENT_DATA
import com.example.rickandmortyapp.presentation.PresentationUtils.getCreated
import com.example.rickandmortyapp.presentation.PresentationUtils.getIdFromUrl
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModelFactory
import com.example.rickandmortyapp.presentation.karakter.activity.DetailKarakterActivity
import com.example.rickandmortyapp.presentation.karakter.adapter.KarakterPagingAdapter
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailEpisodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEpisodeBinding
    private var data: EpisodeModelItemModel? = null
    private lateinit var adapter: KarakterPagingAdapter
    private lateinit var dialog: Dialog


    @Inject
    lateinit var karakterFactory: KarakterViewModelFactory
    private val karakterViewModel: KarakterViewModel by viewModels {
        karakterFactory
    }

    @Inject
    lateinit var episodeFactory: EpisodeViewModelFactory
    private val episodeViewModel: EpisodeViewModel by viewModels {
        episodeFactory
    }

    private var idKarakter = ""
    private var dataFavorite: List<EpisodeItemFavoriteModelRoom> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.detailEpisodeActivity(this)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEpisodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setProgressBar()
        setToolbar()
        setAdapter()
        getDataFromIntent()
        setFavorite()

    }

    private fun setFavorite() {
        getFavorite()
        insertFavorite()
    }

    private fun getFavorite() {
        lifecycleScope.launch {
            dataFavorite = episodeViewModel.getEpisodeFavoriteRoom(application)

            val isFavorite = dataFavorite.any {
                it.idEpisode == data?.id
            }

            if (isFavorite) {
                binding.cbFavoritDetailEpisode.setBackgroundResource(R.drawable.favorite_full)
                binding.cbFavoritDetailEpisode.isChecked = false
            } else {
                binding.cbFavoritDetailEpisode.setBackgroundResource(R.drawable.favorite_outline)
                binding.cbFavoritDetailEpisode.isChecked = true
            }

        }
    }

    private fun insertFavorite() {
        binding.cbFavoritDetailEpisode.setOnClickListener {
            if (binding.cbFavoritDetailEpisode.isChecked) {
                lifecycleScope.launch {
                    episodeViewModel.deleteEpisodeFavoriteRoom(application, data?.id ?: -1)
                }
                Toast.makeText(this, "Berhasil menghapus favorite", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    episodeViewModel.insertEpisodeFavoriteRoom(application, data?.id ?: -1)
                }
                Toast.makeText(this, "Berhasil menambah favorite", Toast.LENGTH_SHORT).show()
            }
            getFavorite()
        }
    }

    private fun setProgressBar() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setView(R.layout.progress)
        dialog = alertDialog.create()
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }

    private fun setAdapter() {
        adapter = KarakterPagingAdapter().apply {
            setOnItemClickListener { position, data ->
                val intent = Intent(this@DetailEpisodeActivity, DetailKarakterActivity::class.java)
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
                showError("Karakter tidak ditemukan")

            }
        }
        binding.rvDetailEpisode.layoutManager = GridLayoutManager(this, 2)
        binding.rvDetailEpisode.adapter = adapter
    }

    private fun getDataFromIntent() {
        data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(INTENT_DATA, EpisodeModelItemModel::class.java)
        } else {
            intent.getParcelableExtra(INTENT_DATA)
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
            karakterViewModel.getKarakterById(application, idKarakter).collectLatest {
                adapter.submitData(it)
            }
        }
    }


    private fun setData(data: EpisodeModelItemModel?) {
        binding.apply {
            tvEpisode.text = data?.episode
            tvName.text = data?.name
            tvAirDate.text = data?.airDate
            tvCreated.text = data?.created
            tvCreated.text = getCreated(data?.created ?: "")
        }

        if (data?.characterList?.isNotEmpty() == true) {
            idKarakter = ""
            data.characterList.forEach {
                idKarakter += "${getIdFromUrl(it)},"
            }
        }


    }

    private fun showError(error: String?) {
        PresentationUtils.setupDialogError(this, error ?: "").setPositiveButton("Ok") { dialog, _ ->
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