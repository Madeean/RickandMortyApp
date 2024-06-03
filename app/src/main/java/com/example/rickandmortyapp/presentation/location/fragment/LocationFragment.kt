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
import com.example.rickandmortyapp.databinding.LocationBottomSheetDialogBinding
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.INTENT_DATA
import com.example.rickandmortyapp.presentation.PresentationUtils.loadingAlertDialog
import com.example.rickandmortyapp.presentation.PresentationUtils.setLoading
import com.example.rickandmortyapp.presentation.PresentationUtils.showError
import com.example.rickandmortyapp.presentation.activity.MainActivity
import com.example.rickandmortyapp.presentation.location.activity.DetailLocationActivity
import com.example.rickandmortyapp.presentation.location.adapter.LocationPagingAdapter
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.madeean.domain.location.model.local.LocationItemModelRoom
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class LocationFragment : Fragment() {
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var application: Application
    private lateinit var binding: FragmentLocationBinding
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
        setViewModelAndApplication()
        setProgressBar()
        setToolbar()
        setRecyclerView()
        getAllData()
        setAccordion()
        setSwipeRefresh()


    }

    private fun setViewModelAndApplication() {
        locationViewModel = (requireActivity() as MainActivity).getViewmodelLocation()
        application = (requireActivity() as MainActivity).getApplicationForApi()
    }

    private fun setSwipeRefresh() {
        binding.swlLocation.setOnRefreshListener {
            getAllData()
            binding.swlLocation.isRefreshing = false
        }
    }

    private fun getAllData(
        name: String = "", type: String = "", dimension: String = ""
    ) {
        setLoading(true, dialog)
        if (PresentationUtils.isNetworkAvailable(requireContext())) {
            lifecycleScope.launch {
                locationViewModel.getAllLocation(
                    application = application, name = name, type = type, dimension = dimension
                ).collectLatest {
                    setLoading(false, dialog)
                    adapter.submitData(it)
                }
            }
        } else {
            setLoading(false, dialog)
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
                binding.tvLocationKosong.visibility = View.VISIBLE
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
                setLoading(true, dialog)
            } else {
                setLoading(false, dialog)
            }
            if (loadState.refresh is LoadState.Error) {
                setLoading(false, dialog)
                if (!PresentationUtils.isNetworkAvailable(requireContext())) {
                    showError(getString(R.string.tidak_ada_koneksi_internet), requireContext())
                } else {
                    showError(getString(R.string.lokasi_tidak_ditemukan), requireContext())
                }
            }

            if (loadState.append is LoadState.Error) {
                setLoading(false, dialog)
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


    private fun setToolbar() {
        binding.locationToolbar.tvToolbar.text = getString(R.string.location)
    }

    private fun setProgressBar() {
        dialog = loadingAlertDialog(requireContext())
    }

    private fun setAccordion() {
        binding.apply {
            btnOpenAccordion.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val viewBottomSheet = LocationBottomSheetDialogBinding.inflate(layoutInflater)
                viewBottomSheet.apply {
                    btnGetLocationFilter.setOnClickListener {
                        val name = etSearchName.text.toString()
                        val type = etSearchType.text.toString()
                        val dimension = etSearchDimension.text.toString()
                        getAllData(name, type, dimension)
                        bottomSheetDialog.dismiss()
                    }
                }
                bottomSheetDialog.apply {
                    setCancelable(true)
                    setContentView(viewBottomSheet.root)
                    show()
                }
            }

        }

    }


    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog.dismiss()
    }


}