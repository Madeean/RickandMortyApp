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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentKarakterDaftarFavoriteBinding
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemFavoriteModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.INTENT_DATA
import com.example.rickandmortyapp.presentation.PresentationUtils.loadingAlertDialog
import com.example.rickandmortyapp.presentation.PresentationUtils.setLoading
import com.example.rickandmortyapp.presentation.PresentationUtils.showError
import com.example.rickandmortyapp.presentation.karakter.activity.DetailKarakterActivity
import com.example.rickandmortyapp.presentation.karakter.adapter.KarakterPagingAdapter
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class KarakterDaftarFavoriteFragment : Fragment() {
    private lateinit var binding: FragmentKarakterDaftarFavoriteBinding
    private lateinit var karakterViewModel: KarakterViewModel
    private lateinit var application: Application
    private lateinit var adapter: KarakterPagingAdapter
    private lateinit var dialog: Dialog

    private var dataFavorite: List<KarakterItemFavoriteModelRoom> = ArrayList()
    private var dataId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentKarakterDaftarFavoriteBinding.inflate(inflater, container, false)
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
            dataFavorite = karakterViewModel.getKarakterFavoriteRoom(application)
            dataFavorite.forEach {
                dataId += "${it.idKarakter},"
            }
            getDataFromInternet()
        }
    }

    private fun getDataFromInternet() {
        setLoading(true,dialog)
        if (PresentationUtils.isNetworkAvailable(requireContext())) {
            lifecycleScope.launch {
                karakterViewModel.getKarakterById( dataId).collectLatest {
                    setLoading(false,dialog)
                    adapter.submitData(it)

                }
            }
        } else {
            setLoading(false,dialog)
            showError(getString(R.string.tidak_ada_koneksi_internet),requireContext())
        }
    }

    private fun setRecyclerView() {
        adapter = KarakterPagingAdapter().apply {
            setOnItemClickListener { _, data ->
                val intent = Intent(requireContext(), DetailKarakterActivity::class.java)
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
                }
            }

            if (loadState.append is LoadState.Error) {
                setLoading(false,dialog)
                if (!PresentationUtils.isNetworkAvailable(requireContext())) showError(getString(R.string.tidak_ada_koneksi_internet),requireContext())
            }

        }
        binding.apply {
            rvKarakter.layoutManager = GridLayoutManager(context, 2)
            rvKarakter.adapter = adapter
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

    companion object {
        fun newInstance(
            viewModel: KarakterViewModel, application: Application
        ): KarakterDaftarFavoriteFragment {
            return KarakterDaftarFavoriteFragment().apply {
                karakterViewModel = viewModel
                this.application = application
            }
        }
    }


}