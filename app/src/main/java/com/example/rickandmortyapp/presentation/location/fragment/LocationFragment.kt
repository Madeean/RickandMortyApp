package com.example.rickandmortyapp.presentation.location.fragment

import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentLocationBinding
import com.example.rickandmortyapp.domain.model.location.local.LocationItemModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.INTENT_DATA
import com.example.rickandmortyapp.presentation.location.activity.DetailLocationActivity
import com.example.rickandmortyapp.presentation.location.adapter.LocationPagingAdapter
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class LocationFragment : Fragment() {
    private lateinit var binding: FragmentLocationBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var application: Application
    private lateinit var dialog: Dialog
    private lateinit var adapter: LocationPagingAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar()
        setToolbar()
        setAccordion()
        setRecyclerView()
        setDataFilter()
        getAllData()

        binding.swlLocation.setOnRefreshListener {
            getAllData()
            binding.swlLocation.isRefreshing = false
        }

    }

    private fun setDataFilter() {
        binding.btnGetLocationFilter.setOnClickListener {
            val name = binding.etSearchName.text.toString()
            val type = binding.etSearchType.text.toString()
            val dimension = binding.etSearchDimension.text.toString()
            binding.rlAccordionLocation.visibility = View.GONE
            getAllData(name, type, dimension)
        }
    }

    private fun getAllData(
        name: String = "", type: String = "", dimension: String = ""
    ) {
        setLoading(true)
        if (PresentationUtils.isNetworkAvailable(requireContext())) {
            lifecycleScope.launch {
                locationViewModel.getAllLocation(
                    application = application, name = name, type = type, dimension = dimension
                ).collectLatest {
                    adapter.submitData(it)
                }
            }
        } else {
            setLoading(false)
            showError("Tidak ada koneksi internet")
            checkDbRoom()
        }

    }

    private fun checkDbRoom() {
        lifecycleScope.launch {
            val data = locationViewModel.getLocationRoom(application)
            if (data.isNotEmpty()) {
                val dataSudahDiTransform = LocationItemModelRoom.transforms(data)
                adapter.submitData(lifecycle, dataSudahDiTransform)
            } else {
                showError("Location tidak ditemukan")
            }
        }
    }

    private fun setRecyclerView() {
        adapter = LocationPagingAdapter().apply {
            setOnItemClickListener { position, data ->
                val intent = Intent(requireContext(), DetailLocationActivity::class.java)
                intent.putExtra(INTENT_DATA, data)
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
                } else {
                    showError("Location tidak ditemukan")
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

    private fun setToolbar() {
        binding.locationToolbar.tvToolbar.text = "Location"
    }

    private fun setProgressBar() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setView(R.layout.progress)
        dialog = alertDialog.create()
    }

    private fun setAccordion() {
        binding.btnOpenAccordion.setOnClickListener {
            if (binding.rlAccordionLocation.visibility == View.VISIBLE) {
                binding.btnOpenAccordion.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.baseline_keyboard_arrow_down_24, 0
                )
                binding.rlAccordionLocation.visibility = View.GONE
            } else {
//                edit drawable
                binding.btnOpenAccordion.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.baseline_keyboard_arrow_up_24, 0
                )
                binding.rlAccordionLocation.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        fun newInstance(viewModel: LocationViewModel, application: Application): LocationFragment {
            return LocationFragment().apply {
                locationViewModel = viewModel
                this.application = application
            }
        }
    }

    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->}


}