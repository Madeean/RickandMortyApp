package com.example.rickandmortyapp.presentation.episode.fragment

import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentHomeBinding
import com.example.rickandmortyapp.domain.model.episode.local.EpisodeItemModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.INTENT_DATA
import com.example.rickandmortyapp.presentation.PresentationUtils.setupDialogError
import com.example.rickandmortyapp.presentation.episode.activity.DetailEpisodeActivity
import com.example.rickandmortyapp.presentation.episode.adapter.EpisodePagingAdapter
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var episodeViewModel: EpisodeViewModel
    private lateinit var application: Application
    private lateinit var adapter: EpisodePagingAdapter
    private lateinit var dialog: Dialog


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
        getAllData()

        binding.swlEpisode.setOnRefreshListener {
            getAllData()
            binding.swlEpisode.isRefreshing = false
        }
    }


    private fun setProgressBar() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setView(R.layout.progress)
        dialog = alertDialog.create()
    }


    private fun getAllData(name: String = "") {
        setLoading(true)
        if (PresentationUtils.isNetworkAvailable(requireContext())) {
            lifecycleScope.launch {
                episodeViewModel.getAllEpisode(application, name).collectLatest {
                    setLoading(false)
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
            val data = episodeViewModel.getEpisodeRoom(application)
            if (data.isNotEmpty()) {
                val dataSudahTransform = EpisodeItemModelRoom.transformFromDomainToRoom(data)
                adapter.submitData(lifecycle, dataSudahTransform)
            } else {
                showError("Episode tidak ditemukan")
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
        adapter = EpisodePagingAdapter().apply {
            setOnItemClickListener { position, data ->
                val intent = Intent(context, DetailEpisodeActivity::class.java)
                intent.putExtra(INTENT_DATA, data)
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
                if (!PresentationUtils.isNetworkAvailable(requireContext())) {
                    showError("Tidak ada koneksi internet")
                } else {
                    showError("Episode tidak ditemukan")
                }
            }

            if (loadState.append is LoadState.Error) {
                if (!PresentationUtils.isNetworkAvailable(requireContext())) showError("Tidak ada koneksi internet")
            }

        }

        binding.rvEpisode.layoutManager = LinearLayoutManager(context)
        binding.rvEpisode.adapter = adapter
    }

    private fun setSearchEpisode() {
        val searchValue = binding.etSearchHome.text
        binding.ibSearch.setOnClickListener {
            getAllData(searchValue.toString())
        }
    }

    private fun setToolbar() {
        binding.homeToolbar.tvToolbar.text = "Episode"
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