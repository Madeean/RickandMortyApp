package com.example.rickandmortyapp.presentation.location.fragment

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentLocationBinding
import com.example.rickandmortyapp.domain.model.location.local.LocationItemModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.INTENT_DATA
import com.example.rickandmortyapp.presentation.PresentationUtils.loadingAlertDialog
import com.example.rickandmortyapp.presentation.PresentationUtils.setLoading
import com.example.rickandmortyapp.presentation.PresentationUtils.showError
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
        setSwipeRefresh()



    }

    private fun setSwipeRefresh() {
        binding.swlLocation.setOnRefreshListener {
            getAllData()
            binding.swlLocation.isRefreshing = false
        }
    }

    private fun setDataFilter() {
        binding.apply {
            btnGetLocationFilter.setOnClickListener {
                val name = etSearchName.text.toString()
                val type = etSearchType.text.toString()
                val dimension = etSearchDimension.text.toString()
                rlAccordionLocation.visibility = View.GONE
                getAllData(name, type, dimension)
            }
        }

    }

    private fun getAllData(
        name: String = "", type: String = "", dimension: String = ""
    ) {
        setLoading(true,dialog)
        if (PresentationUtils.isNetworkAvailable(requireContext())) {
            lifecycleScope.launch {
                locationViewModel.getAllLocation(
                    application = application, name = name, type = type, dimension = dimension
                ).collectLatest {
                    setLoading(false,dialog)
                    adapter.submitData(it)
                }
            }
        } else {
            setLoading(false,dialog)
            showError(getString(R.string.tidak_ada_koneksi_internet), requireContext())
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
                showError(getString(R.string.lokasi_tidak_ditemukan), requireContext())
            }
        }
    }

    private fun setRecyclerView() {
        adapter = LocationPagingAdapter().apply {
            setOnItemClickListener { _, data ->
                val intent = Intent(requireContext(), DetailLocationActivity::class.java)
                intent.putExtra(INTENT_DATA, data)
                resultLauncher.launch(intent)
            }
        }
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                setLoading(true,dialog)
            } else {
                setLoading(false,dialog)
            }
            if (loadState.refresh is LoadState.Error) {
                setLoading(false,dialog)
                if (!PresentationUtils.isNetworkAvailable(requireContext())) {
                    showError(getString(R.string.tidak_ada_koneksi_internet),requireContext())
                } else {
                    showError(getString(R.string.lokasi_tidak_ditemukan),requireContext())
                }
            }

            if (loadState.append is LoadState.Error) {
                setLoading(false,dialog)
                if (!PresentationUtils.isNetworkAvailable(requireContext())) showError(
                    getString(R.string.tidak_ada_koneksi_internet),
                    requireContext()
                )
            }
        }

        binding.apply {
            rvLocation.layoutManager = LinearLayoutManager(context)
            rvLocation.adapter = adapter
        }

    }


    private fun setToolbar() {
        binding.locationToolbar.tvToolbar.text = getString(R.string.location)
    }

    private fun setProgressBar() {
        dialog = loadingAlertDialog(requireContext())
    }

    private fun setAccordion() {
        binding.apply {
            btnOpenAccordion.setOnClickListener {
                if (rlAccordionLocation.visibility == View.VISIBLE) {
                    btnOpenAccordion.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.baseline_keyboard_arrow_down_24, 0
                    )
                    rlAccordionLocation.visibility = View.GONE
                } else {
                    btnOpenAccordion.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.baseline_keyboard_arrow_up_24, 0
                    )
                    rlAccordionLocation.visibility = View.VISIBLE
                }
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
    ) { }


}