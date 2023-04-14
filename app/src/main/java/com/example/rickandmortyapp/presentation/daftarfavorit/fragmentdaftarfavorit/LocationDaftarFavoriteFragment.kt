package com.example.rickandmortyapp.presentation.daftarfavorit.fragmentdaftarfavorit

import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentLocationDaftarFavoriteBinding
import com.example.rickandmortyapp.domain.model.location.local.LocationItemFavoriteModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.loadingAlertDialog
import com.example.rickandmortyapp.presentation.PresentationUtils.setLoading
import com.example.rickandmortyapp.presentation.PresentationUtils.showError
import com.example.rickandmortyapp.presentation.daftarfavorit.activity.DaftarFavoriteActivity
import com.example.rickandmortyapp.presentation.location.activity.DetailLocationActivity
import com.example.rickandmortyapp.presentation.location.adapter.LocationPagingAdapter
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class LocationDaftarFavoriteFragment : Fragment() {
    private lateinit var binding: FragmentLocationDaftarFavoriteBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var application: Application
    private lateinit var adapter: LocationPagingAdapter
    private lateinit var dialog: Dialog
    private var dataFavorite: List<LocationItemFavoriteModelRoom> = ArrayList()
    private var dataId = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationDaftarFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModelAndApplication()
        setProgressBar()
        setRecyclerView()
        getDataFvorite()
    }

    private fun setViewModelAndApplication() {
        locationViewModel = (requireActivity() as DaftarFavoriteActivity).getViewmodelLocation()
        application = (requireActivity() as DaftarFavoriteActivity).getApplicationForApi()
    }


    private fun getDataFvorite() {
        dataId = ""
        dataFavorite = listOf()
        lifecycleScope.launch {
            dataFavorite = locationViewModel.getLocationFavoriteRoom(application)
            dataFavorite.forEach {
                dataId += "${it.idLocation},"
            }
            getDataFromInternet()
        }
    }

    private fun getDataFromInternet() {
        setLoading(true, dialog)
        if (PresentationUtils.isNetworkAvailable(requireContext())) {
            lifecycleScope.launch {
                locationViewModel.getMultipleLocation(dataId).collectLatest {
                    setLoading(false, dialog)
                    adapter.submitData(it)

                }
            }
        } else {
            setLoading(false, dialog)
            showError(getString(R.string.tidak_ada_koneksi_internet), requireContext())
        }
    }

    private fun setRecyclerView() {
        adapter = LocationPagingAdapter().apply {
            setOnItemClickListener { _, data ->
                val intent = Intent(requireContext(), DetailLocationActivity::class.java)
                intent.putExtra(PresentationUtils.INTENT_DATA, data)
                resultLauncher.launch(intent)
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
                if (!PresentationUtils.isNetworkAvailable(requireContext())) {
                    showError(getString(R.string.tidak_ada_koneksi_internet), requireContext())
                }
            }

            if (loadState.append is LoadState.Error) {
                if (!PresentationUtils.isNetworkAvailable(requireContext())) showError(
                    getString(R.string.tidak_ada_koneksi_internet), requireContext()
                )
            }

        }
        binding.apply {
            rvLocation.layoutManager = LinearLayoutManager(context)
            rvLocation.adapter = adapter
        }
    }


    private fun setProgressBar() {
        dialog = loadingAlertDialog(requireContext())
    }

    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == PresentationUtils.CODE_RESULT) {
            getDataFvorite()
        }
    }
}