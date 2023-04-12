package com.example.rickandmortyapp.presentation.daftarfavorit.fragmentdaftarfavorit

import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentLocationDaftarFavoriteBinding
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.model.location.local.LocationItemFavoriteModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.karakter.activity.DetailKarakterActivity
import com.example.rickandmortyapp.presentation.karakter.adapter.KarakterPagingAdapter
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
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
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar()
        setRecyclerView()
        getAllData()
    }

    private fun getAllData() {
        Toast.makeText(requireContext(), "Location", Toast.LENGTH_SHORT).show()
        getDataFvorite()
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
        setLoading(true)
        if (PresentationUtils.isNetworkAvailable(requireContext())) {
            lifecycleScope.launch {
                locationViewModel.getMultipleLocation(dataId).collectLatest {
                    setLoading(false)
                    adapter.submitData(it)

                }
            }
        } else {
            setLoading(false)
            showError("Tidak ada koneksi internet")
        }
    }

    private fun setRecyclerView() {
        adapter = LocationPagingAdapter().apply {
            setOnItemClickListener { position, data ->
                val intent = Intent(requireContext(), DetailLocationActivity::class.java)
                intent.putExtra(PresentationUtils.INTENT_DATA, data)
                resultLauncher.launch(intent)
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
                if (!PresentationUtils.isNetworkAvailable(requireContext())) {
                    showError("Tidak ada koneksi internet")
                }
            }

            if (loadState.append is LoadState.Error) {
                if (!PresentationUtils.isNetworkAvailable(requireContext())) showError("Tidak ada koneksi internet")
            }

        }

        binding.rvLocation.layoutManager = LinearLayoutManager(context)
        binding.rvLocation.adapter = adapter
    }

    private fun showError(error: String?) {
        PresentationUtils.setupDialogError(requireContext(), error ?: "")
            .setPositiveButton("Ok") { dialog, _ ->
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


    private fun setProgressBar() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setView(R.layout.progress)
        dialog = alertDialog.create()
    }

    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == PresentationUtils.CODE_RESULT) {
            setRecyclerView()
            getAllData()
        }
    }


    companion object {
        fun newInstance(
            viewModel: LocationViewModel, application: Application
        ): LocationDaftarFavoriteFragment {
            return LocationDaftarFavoriteFragment().apply {
                locationViewModel = viewModel
                this.application = application
            }
        }
    }
}