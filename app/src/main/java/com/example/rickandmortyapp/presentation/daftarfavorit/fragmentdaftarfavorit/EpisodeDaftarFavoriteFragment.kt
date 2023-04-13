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
import com.example.rickandmortyapp.databinding.FragmentEpisodeDaftarFavoriteBinding
import com.example.rickandmortyapp.domain.model.episode.local.EpisodeItemFavoriteModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.INTENT_DATA
import com.example.rickandmortyapp.presentation.PresentationUtils.loadingAlertDialog
import com.example.rickandmortyapp.presentation.PresentationUtils.setLoading
import com.example.rickandmortyapp.presentation.PresentationUtils.showError
import com.example.rickandmortyapp.presentation.episode.activity.DetailEpisodeActivity
import com.example.rickandmortyapp.presentation.episode.adapter.EpisodePagingAdapter
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EpisodeDaftarFavoriteFragment : Fragment() {
    private lateinit var binding: FragmentEpisodeDaftarFavoriteBinding
    private lateinit var episodeViewModel: EpisodeViewModel
    private lateinit var application: Application
    private lateinit var adapter: EpisodePagingAdapter
    private lateinit var dialog: Dialog

    private var dataFavorite: List<EpisodeItemFavoriteModelRoom> = ArrayList()
    private var dataId = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpisodeDaftarFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar()
        setRecyclerView()
        getDataFvorite()
    }

    private fun getDataFvorite() {
        dataId = ""
        dataFavorite = listOf()
        lifecycleScope.launch {
            dataFavorite = episodeViewModel.getEpisodeFavoriteRoom(application)
            dataFavorite.forEach {
                dataId += "${it.idEpisode},"
            }
            getDataFromInternet()
        }
    }

    private fun getDataFromInternet() {
        setLoading(true, dialog)
        if (PresentationUtils.isNetworkAvailable(requireContext())) {
            lifecycleScope.launch {
                episodeViewModel.getEpisodeById( dataId).collectLatest {
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
                }
            }

            if (loadState.append is LoadState.Error) {
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

    private fun setProgressBar() {
        dialog = loadingAlertDialog(requireContext())
    }

    companion object {
        fun newInstance(
            viewModel: EpisodeViewModel, application: Application
        ): EpisodeDaftarFavoriteFragment {
            return EpisodeDaftarFavoriteFragment().apply {
                episodeViewModel = viewModel
                this.application = application
            }
        }
    }

    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == PresentationUtils.CODE_RESULT) {
            getDataFvorite()
        }
    }


}