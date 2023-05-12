package com.example.rickandmortyapp.presentation.karakter.activity

import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityDetailKarakterBinding
import com.example.rickandmortyapp.domain.karakter.model.network.KarakterModelItemModel
import com.example.rickandmortyapp.domain.karakter.model.local.KarakterItemFavoriteModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.CODE_RESULT
import com.example.rickandmortyapp.presentation.PresentationUtils.getIdFromUrl
import com.example.rickandmortyapp.presentation.PresentationUtils.loadingAlertDialog
import com.example.rickandmortyapp.presentation.PresentationUtils.setLoading
import com.example.rickandmortyapp.presentation.PresentationUtils.showError
import com.example.rickandmortyapp.presentation.episode.adapter.EpisodePagingAdapter
import com.example.rickandmortyapp.presentation.episode.activity.DetailEpisodeActivity
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.factory.PresentationFactory
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
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
    lateinit var presentationFactory: PresentationFactory
    private val episodeViewModel: EpisodeViewModel by viewModels {
        presentationFactory
    }

    private val karakterViewModel: KarakterViewModel by viewModels {
        presentationFactory
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
        setBtnToDetail()
    }

    private fun setBtnToDetail() {
        binding.apply {
            btnDetailLocation.setOnClickListener {
                val intent = Intent(this@DetailKarakterActivity, DetailLocationActivity::class.java)
                intent.putExtra(PresentationUtils.INTENT_ID, idLocation)
                startActivity(intent)
            }
            btnDetailOrigin.setOnClickListener {
                val intent = Intent(this@DetailKarakterActivity, DetailLocationActivity::class.java)
                intent.putExtra(PresentationUtils.INTENT_ID, idOrigin)
                startActivity(intent)
            }
        }

    }

    private fun setFavorite() {
        getFavorite()
        insertFavorite()
    }

    private fun insertFavorite() {
        binding.cbFavoritDetailKarakter.apply {
            setOnClickListener {
                if (isChecked) {
                    lifecycleScope.launch {
                        karakterViewModel.deleteKarakterFavoriteRoom(application, data?.id ?: -1)
                    }
                    Toast.makeText(
                        this@DetailKarakterActivity,
                        getString(R.string.berhasil_menghapus_favorite,data?.name),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    lifecycleScope.launch {
                        karakterViewModel.insertKarakterFavoriteRoom(application, data?.id ?: -1)
                    }
                    Toast.makeText(
                        this@DetailKarakterActivity,
                        getString(R.string.berhasil_menambah_favorite,data?.name),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                getFavorite()
            }
        }

    }

    private fun getFavorite() {
        lifecycleScope.launch {
            dataFavorite = karakterViewModel.getKarakterFavoriteRoom(application)

            val isFavorite = dataFavorite.any {
                it.idKarakter == data?.id
            }

            binding.cbFavoritDetailKarakter.apply {
                isChecked = if (isFavorite) {
                    setBackgroundResource(R.drawable.favorite_full)
                    false
                } else {
                    setBackgroundResource(R.drawable.favorite_outline)
                    true
                }
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
            showError(getString(R.string.karakter_tidak_ditemukan), this@DetailKarakterActivity)
        }
    }

    private fun getDataFromApi() {
        lifecycleScope.launch {
            episodeViewModel.getEpisodeById(idEpisode).collectLatest {
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
            tvType.text =
                if (data?.type?.isBlank() == true) getString(R.string.unknown) else data?.type

            Glide.with(this@DetailKarakterActivity).load(data?.image)
                .placeholder(R.drawable.ic_launcher_background).into(ivDetailImage)

            if (data?.location == null && data?.origin == null) {
                btnDetailLocation.isEnabled = false
                btnDetailOrigin.isEnabled = false
                return
            }
        }

        if (data?.origin?.url?.isBlank() == true) {
            binding.btnDetailOrigin.isEnabled = false
            binding.btnDetailOrigin.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.abuabutua
                )
            )
            idLocation = getIdFromUrl(data.location?.url ?: "")
        } else if (data?.location?.url?.isBlank() == true) {
            binding.btnDetailLocation.isEnabled = false
            binding.btnDetailLocation.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.abuabutua
                )
            )
            idOrigin = getIdFromUrl(data.origin?.url ?: "")
        } else {
            idLocation = getIdFromUrl(data?.location?.url ?: "")
            idOrigin = getIdFromUrl(data?.origin?.url ?: "")
        }

        if (data?.episode?.isNotEmpty() == true) {
            idEpisode = ""
            data.episode.forEach {
                idEpisode += "${getIdFromUrl(it)},"
            }
            getDataFromApi()
        }


    }

    private fun setAdapter() {
        adapter = EpisodePagingAdapter().apply {
            setOnItemClickListener { _, data ->
                val intent = Intent(this@DetailKarakterActivity, DetailEpisodeActivity::class.java)
                intent.putExtra(PresentationUtils.INTENT_DATA, data)
                startActivity(intent)
            }
        }
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                setLoading(true, dialog)
            } else {
                setLoading(false, dialog)
            }
            if (loadState.refresh is LoadState.Error) {
                setLoading(false, dialog)
                showError(getString(R.string.episode_tidak_ditemukan), this)

            }
        }
        binding.apply {
            rvDetailKarakter.layoutManager = LinearLayoutManager(this@DetailKarakterActivity)
            rvDetailKarakter.adapter = adapter
        }
    }

    private fun setToolbar() {
        binding.detailKarakterToolbar.apply {
            tvToolbar.text = getString(R.string.detail_karakter)
            ivBackToolbar.setOnClickListener {
                val intent = Intent()
                setResult(CODE_RESULT, intent)
                finish()
            }
        }

    }

    private fun setProgressBar() {
        dialog = loadingAlertDialog(this)
    }
}