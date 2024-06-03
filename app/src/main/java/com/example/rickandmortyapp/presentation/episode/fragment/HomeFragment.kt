package com.example.rickandmortyapp.presentation.episode.fragment

import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentHomeBinding
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.INTENT_DATA
import com.example.rickandmortyapp.presentation.PresentationUtils.loadingAlertDialog
import com.example.rickandmortyapp.presentation.PresentationUtils.setLoading
import com.example.rickandmortyapp.presentation.PresentationUtils.showError
import com.example.rickandmortyapp.presentation.activity.MainActivity
import com.example.rickandmortyapp.presentation.episode.activity.DetailEpisodeActivity
import com.example.rickandmortyapp.presentation.episode.adapter.EpisodePagingAdapter
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.madeean.domain.episode.model.local.EpisodeItemModelRoom
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
        setViewModelAndApplication()
        setProgressBar()
        setToolbar()
        setRecyclerView()
        getAllData()
        setSearchEpisode()
        setSwipeRefresh()
    }

    private fun setViewModelAndApplication() {
        episodeViewModel = (requireActivity() as MainActivity).getViewModelEpisode()
        application = (requireActivity() as MainActivity).getApplicationForApi()
    }

    private fun setSwipeRefresh() {
        binding.swlEpisode.setOnRefreshListener {
            getAllData()
            binding.swlEpisode.isRefreshing = false
        }
    }


    private fun setProgressBar() {
        dialog = loadingAlertDialog(requireActivity())
    }


    private fun getAllData(name: String = "") {
        setLoading(true, dialog)
        if (PresentationUtils.isNetworkAvailable(requireContext())) {
            lifecycleScope.launch {
                episodeViewModel.getAllEpisode(application, name).collectLatest {
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
            val data = episodeViewModel.getEpisodeRoom(application)
            if (data.isNotEmpty()) {
                val dataSudahTransform = EpisodeItemModelRoom.transformFromDomainToRoom(data)
                adapter.submitData(lifecycle, dataSudahTransform)
            } else {
                binding.tvEpisodeKosong.visibility = View.VISIBLE
            }
        }
    }


    private fun setRecyclerView() {
        adapter = EpisodePagingAdapter().apply {
            setOnItemClickListener { _, data ->
                val intent = Intent(context, DetailEpisodeActivity::class.java)
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
                    showError(getString(R.string.episode_tidak_ditemukan), requireContext())
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
            rvEpisode.layoutManager = LinearLayoutManager(context)
            rvEpisode.adapter = adapter
        }

    }

    private fun setSearchEpisode() {
        binding.apply {
            etSearchHome.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getAllData(binding.etSearchHome.text.toString())
                }
                true
            }

        }
    }

    private fun setToolbar() {
        binding.homeToolbar.tvToolbar.text = getString(R.string.episode)
    }


    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog.dismiss()
    }

}
