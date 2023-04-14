package com.example.rickandmortyapp.presentation.karakter.fragment

import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentKarakterBinding
import com.example.rickandmortyapp.databinding.KarakterBottomSheetDialogBinding
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.INTENT_DATA
import com.example.rickandmortyapp.presentation.PresentationUtils.loadingAlertDialog
import com.example.rickandmortyapp.presentation.PresentationUtils.setLoading
import com.example.rickandmortyapp.presentation.PresentationUtils.showError
import com.example.rickandmortyapp.presentation.activity.MainActivity
import com.example.rickandmortyapp.presentation.karakter.activity.DetailKarakterActivity
import com.example.rickandmortyapp.presentation.karakter.adapter.KarakterPagingAdapter
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class KarakterFragment : Fragment() {
    private lateinit var binding: FragmentKarakterBinding
    private lateinit var karakterViewModel: KarakterViewModel
    private lateinit var application: Application
    private lateinit var adapter: KarakterPagingAdapter
    private lateinit var dialog: Dialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentKarakterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModelAndApplication()
        setProgressBar()
        setToolbar()
        setBottomSheetDialog()
        setRecyclerView()
        getAllData()
        setSwipeRefresh()
    }

    private fun setViewModelAndApplication() {
        karakterViewModel = (requireActivity() as MainActivity).getViewModelKarakter()
        application = (requireActivity() as MainActivity).getApplicationForApi()
    }

    private fun setSwipeRefresh() {
        binding.swlKarakter.setOnRefreshListener {
            getAllData()
            binding.swlKarakter.isRefreshing = false
        }
    }

    private fun setProgressBar() {
        dialog = loadingAlertDialog(requireContext())
    }


    private fun getAllData(
        gender: String = "",
        status: String = "",
        species: String = "",
        type: String = "",
        name: String = ""
    ) {
        setLoading(true, dialog)
        if (PresentationUtils.isNetworkAvailable(requireContext())) {
            val genderValue: String = if (gender == "all") "" else gender
            val statusValue: String = if (status == "all") "" else status

            lifecycleScope.launch {
                karakterViewModel.getAllKarakter(
                    application,
                    name,
                    statusValue,
                    species,
                    type,
                    genderValue
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
            val data = karakterViewModel
                .getKarakterRoom(application)
            if (data.isNotEmpty()) {
                val dataSudahDiTransform = KarakterItemModelRoom.transforms(data)
                adapter.submitData(lifecycle, dataSudahDiTransform)
            } else {
                showError(getString(R.string.karakter_tidak_ditemukan), requireContext())
            }
        }
    }


    private fun setRecyclerView() {
        adapter = KarakterPagingAdapter().apply {
            setOnItemClickListener { _, data ->
                val intent = Intent(context, DetailKarakterActivity::class.java)
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
                    showError(getString(R.string.karakter_tidak_ditemukan), requireContext())
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
            rvKarakter.layoutManager = GridLayoutManager(context, 2)
            rvKarakter.adapter = adapter
        }

    }


    private fun setBottomSheetDialog() {

        val adapterGender: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), R.layout.dropdown_menu, resources.getStringArray(R.array.gender)
        )
        val adapterStatus: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), R.layout.dropdown_menu, resources.getStringArray(R.array.status)
        )

        binding.btnOpenBottomSheet.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val viewBottomSheet = KarakterBottomSheetDialogBinding.inflate(layoutInflater)

            viewBottomSheet.apply {
                acGender.setAdapter(adapterGender)
                acStatus.setAdapter(adapterStatus)

                btnGetKarakter.setOnClickListener {

                    getAllData(
                        name = etNamaKarakter.text.toString(),
                        gender = acGender.text.toString(),
                        species = etSearchSpecies.text.toString(),
                        type = etSearchType.text.toString(),
                        status = acStatus.text.toString()
                    )

                    dialog.dismiss()
                }
            }


            dialog.apply {
                setCancelable(true)
                setContentView(viewBottomSheet.root)
                show()
            }


        }
    }

    private fun setToolbar() {
        binding.karakterToolbar.tvToolbar.text = getString(R.string.karakter)
    }


    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }
}