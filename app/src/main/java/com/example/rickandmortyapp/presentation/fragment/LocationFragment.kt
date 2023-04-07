package com.example.rickandmortyapp.presentation.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentLocationBinding
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.adapter.EpisodePagingAdapter
import com.example.rickandmortyapp.presentation.adapter.KarakterPagingAdapter
import com.example.rickandmortyapp.presentation.adapter.LocationPagingAdapter
import com.example.rickandmortyapp.presentation.viewmodel.karakter.KarakterViewModel
import com.example.rickandmortyapp.presentation.viewmodel.location.LocationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.platform.android.AndroidLogHandler.setFilter


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
        lifecycleScope.launch {
            locationViewModel.getAllLocation(
                application = application, name = name, type = type, dimension = dimension
            ).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setRecyclerView() {
        adapter = LocationPagingAdapter()
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                setLoading(true)
            } else {
                setLoading(false)
            }
            if (loadState.refresh is LoadState.Error) {
                setLoading(false)
                showError("Location tidak ditemukan")

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
//            binding.rlAccordionLocation.apply {
//                visibility = if (visibility == View.VISIBLE) {
//                    View.GONE
//                } else {
//                    View.VISIBLE
//                }
//            }
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


}