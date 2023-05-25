package com.example.rickandmortyapp.presentation.location.activity

import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyapp.MyApplication
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityDetailLocationBinding
import com.example.rickandmortyapp.domain.location.model.network.LocationModelItemModel
import com.example.rickandmortyapp.domain.location.model.local.LocationItemFavoriteModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.CODE_RESULT
import com.example.rickandmortyapp.presentation.PresentationUtils.loadingAlertDialog
import com.example.rickandmortyapp.presentation.PresentationUtils.setLoading
import com.example.rickandmortyapp.presentation.PresentationUtils.showError
import com.example.rickandmortyapp.presentation.factory.PresentationFactory
import com.example.rickandmortyapp.presentation.karakter.activity.DetailKarakterActivity
import com.example.rickandmortyapp.presentation.karakter.adapter.KarakterPagingAdapter
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLocationBinding
    private var data: LocationModelItemModel? = null
    private lateinit var adapter: KarakterPagingAdapter
    private lateinit var dialog: Dialog

    @Inject
    lateinit var presentationFactory: PresentationFactory

    private val karakterViewModel: KarakterViewModel by viewModels {
        presentationFactory
    }
    private val locationViewModel: LocationViewModel by viewModels {
        presentationFactory
    }

    private var idKarakter = ""
    private var idLocation = 0
    private var dataFavorite: List<LocationItemFavoriteModelRoom> = ArrayList()

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
        setFavorite()

    }

    private fun setFavorite() {
        getFavorite()
        insertFavorite()
    }

    private fun insertFavorite() {
        binding.cbFavoritDetailLocation.apply {
            setOnClickListener {
                if (isChecked) {
                    lifecycleScope.launch {
                        locationViewModel.deleteLocationFavoriteRoom(application, data?.id ?: -1)
                    }
                    Toast.makeText(
                        this@DetailLocationActivity,
                        getString(R.string.berhasil_menghapus_favorite, data?.name),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    lifecycleScope.launch {
                        locationViewModel.insertLocationFavoriteRoom(application, data?.id ?: -1)
                    }
                    Toast.makeText(
                        this@DetailLocationActivity,
                        getString(R.string.berhasil_menambah_favorite, data?.name),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                getFavorite()
            }
        }

    }

    private fun getFavorite() {
        lifecycleScope.launch {
            dataFavorite = locationViewModel.getLocationFavoriteRoom(application)

            val isFavorite = dataFavorite.any {
                it.idLocation == data?.id
            }

            binding.cbFavoritDetailLocation.apply {
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

    private fun setupObserverLocation() {
        locationViewModel.locationById.observe(this) {
            if (it.id == null) return@observe
            setLoading(false, dialog)
            setData(it)
            getDataFromInternet()
        }
    }

    private fun getDataFromInternet() {
        lifecycleScope.launch {
            karakterViewModel.getKarakterById(idKarakter).collectLatest {
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
            setLoading(true, dialog)
            locationViewModel.getLocationById(idLocation)
        } else {
            showError(getString(R.string.location_tidak_ditemukan), this)
        }
    }

    private fun setData(data: LocationModelItemModel?) {
        binding.apply {
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
            setOnItemClickListener { _, data ->
                val intent = Intent(this@DetailLocationActivity, DetailKarakterActivity::class.java)
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
                showError(getString(R.string.karakter_tidak_ditemukan), this)

            }
        }
        binding.apply {
            rvDetailLocation.layoutManager = GridLayoutManager(this@DetailLocationActivity, 2)
            rvDetailLocation.adapter = adapter
        }

    }


    private fun setToolbar() {
        binding.detailLocationToolbar.apply {
            tvToolbar.text = getString(R.string.detail_location)
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