package com.example.rickandmortyapp.presentation.fragment

import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentHomeBinding
import com.example.rickandmortyapp.presentation.PresentationUtils.setupDialogError
import com.example.rickandmortyapp.presentation.adapter.EpisodePagingAdapter
import com.example.rickandmortyapp.presentation.viewmodel.EpisodeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var episodeViewModel: EpisodeViewModel
    private lateinit var application: Application
    private lateinit var adapter: EpisodePagingAdapter
    private lateinit var dialog: Dialog

    private var perulanganError = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar()
        setToolbar()
        setSearchEpisode()
        setRecyclerView()
        getAllData(state = 0)

        binding.swlEpisode.setOnRefreshListener {
            getAllData(state = 0)
            binding.swlEpisode.isRefreshing = false
        }
    }

    private fun setProgressBar() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setView(R.layout.progress)
        dialog = alertDialog.create()
    }


    private fun getAllData(name: String? = "", state: Int) {
        var searchName = ""
        if (state == 1) {
            searchName = name ?: ""
        } else {
            searchName = ""
        }
        perulanganError = 0
        setLoading(true)
        adapter.refresh()
        adapter.retry()
        lifecycleScope.launch {
            episodeViewModel.getAllEpisode(application, searchName).collectLatest {
                setLoading(false)
                adapter.submitData(it)
                adapter.addLoadStateListener { loadState ->
                    if (loadState.refresh is LoadState.Loading) {
                        setLoading(true)
                    } else {
                        setLoading(false)
                    }
                    if (loadState.refresh is LoadState.Error) {
                        setLoading(false)
                        perulanganError++
                        if (perulanganError == 1) {
                            println("ERROR312")
                            showError("Episode tidak ditemukan")
                        }
                    }
                }
            }
        }
    }

    private fun showError(error: String?) {
        setupDialogError(requireContext(), error ?: "").setPositiveButton("Ok") { dialog, _ ->
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

    private fun setRecyclerView() {
        adapter = EpisodePagingAdapter()
        binding.rvEpisode.layoutManager = LinearLayoutManager(context)
        binding.rvEpisode.adapter = adapter
    }

    private fun setSearchEpisode() {
        val searchValue = binding.etSearchHome.text
        binding.ibSearch.setOnClickListener {
            if (searchValue.toString().isNotBlank()) {
                getAllData(searchValue.toString(), 1)
            } else {
                Toast.makeText(context, "Masukkan nama episode", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setToolbar() {
        binding.homeToolbar.myToolbarTitle.text = "Episode"
    }

    companion object {
        fun newInstance(viewModel: EpisodeViewModel, application: Application): HomeFragment {
            return HomeFragment().apply {
                episodeViewModel = viewModel
                this.application = application
            }
        }
    }

}