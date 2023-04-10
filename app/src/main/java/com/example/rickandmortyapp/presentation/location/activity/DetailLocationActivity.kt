package com.example.rickandmortyapp.presentation.location.activity

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
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityDetailLocationBinding
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.domain.model.location.LocationModelItemModel
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.episode.activity.DetailEpisodeActivity
import com.example.rickandmortyapp.presentation.episode.adapter.EpisodePagingAdapter
import com.example.rickandmortyapp.presentation.karakter.activity.DetailKarakterActivity
import com.example.rickandmortyapp.presentation.karakter.adapter.KarakterPagingAdapter
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModelFactory
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLocationBinding
    private var data: LocationModelItemModel? = null
    private lateinit var adapter: KarakterPagingAdapter
    private lateinit var dialog: Dialog

    @Inject
    lateinit var karakterFactory: KarakterViewModelFactory
    private val karakterViewModel: KarakterViewModel by viewModels {
        karakterFactory
    }

    @Inject
    lateinit var locationFactory: LocationViewModelFactory
    private val locationViewModel: LocationViewModel by viewModels {
        locationFactory
    }

    private var idKarakter = ""
    private var idLocation = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.detailLocationActivity(this)
        binding = ActivityDetailLocationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupObserverLocation()
        setProgressBar()
        setToolbar()
        setAdapter()
        getDataFromIntent()
    }

    private fun setupObserverLocation() {
        locationViewModel.locationById.observe(this) {
            if (it.id == null) return@observe
            setLoading(false)
            setData(it)
            getDataFromInternet()
        }
    }

    private fun getDataFromInternet() {
        lifecycleScope.launch {
            karakterViewModel.getKarakterById(application, idKarakter).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun getDataFromIntent() {
        data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                PresentationUtils.INTENT_DATA, LocationModelItemModel::class.java
            )
        } else {
            intent.getParcelableExtra(PresentationUtils.INTENT_DATA)
        }
        idLocation = intent.getIntExtra(PresentationUtils.INTENT_ID, 0)


        if (data != null) {
            setData(data)
        } else if (idLocation != 0) {
            setLoading(true)
            locationViewModel.getLocationById(idLocation)
        } else {
            showError("Data kosong")
        }
    }

    private fun setData(data: LocationModelItemModel?) {
        binding.apply {
            tvCreated.text = PresentationUtils.getCreated(data?.created ?: "")
            tvName.text = data?.name
            tvDimension.text = data?.dimension
            tvType.text = data?.type
        }

        if (data?.residents?.isNotEmpty() == true) {
            idKarakter = ""
            data.residents.forEach {
                idKarakter += "${PresentationUtils.getIdFromUrl(it)},"
            }
            getDataFromInternet()
        }

    }

    private fun setAdapter() {
        adapter = KarakterPagingAdapter().apply {
            setOnItemClickListener { position, data ->
                val intent = Intent(this@DetailLocationActivity, DetailKarakterActivity::class.java)
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
        binding.rvDetailLocation.layoutManager = GridLayoutManager(this, 2)
        binding.rvDetailLocation.adapter = adapter
    }

    private fun showError(error: String) {
        PresentationUtils.setupDialogError(this, error ?: "").setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }.show()
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }

    private fun setToolbar() {
        binding.detailLocationToolbar.tvToolbar.text = "Detail Location"
        binding.detailLocationToolbar.ivBackToolbar.setOnClickListener {
            finish()
        }
    }

    private fun setProgressBar() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setView(R.layout.progress)
        dialog = alertDialog.create()
    }


}