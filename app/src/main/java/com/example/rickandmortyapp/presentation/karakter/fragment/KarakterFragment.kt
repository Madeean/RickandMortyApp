package com.example.rickandmortyapp.presentation.karakter.fragment

import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentKarakterBinding
import com.example.rickandmortyapp.databinding.KarakterBottomSheetDialogBinding
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemModelRoom
import com.example.rickandmortyapp.presentation.PresentationUtils
import com.example.rickandmortyapp.presentation.PresentationUtils.INTENT_DATA
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
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar()
        setToolbar()
        setBottomSheetDialog()
        setRecyclerView()
        getAllData()

        binding.swlKarakter.setOnRefreshListener {
            adapter.refresh()
            getAllData()
            binding.swlKarakter.isRefreshing = false
        }

    }

    private fun setProgressBar() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setView(R.layout.progress)
        dialog = alertDialog.create()
    }

    private fun getAllData(
        gender: String = "",
        status: String = "",
        species: String = "",
        type: String = "",
        name: String = ""
    ) {
        setLoading(true)
        if (PresentationUtils.isNetworkAvailable(requireContext())) {
            val genderValue: String = if (gender == "all") "" else gender
            val statusValue: String = if (status == "all") "" else status
            println("MASUK 1")
            lifecycleScope.launch {
                karakterViewModel.getAllKarakter(
                    application,
                    name ?: "",
                    statusValue ?: "",
                    species ?: "",
                    type ?: "",
                    genderValue ?: ""
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
            val data = karakterViewModel.getKarakterRoom(application)
            if (data.isNotEmpty()) {
                val dataSudahDiTransform = KarakterItemModelRoom.transforms(data)
                adapter.submitData(lifecycle, dataSudahDiTransform)
            } else {
                showError("Karakter tidak ditemukan")
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }

    private fun setRecyclerView() {
        adapter = KarakterPagingAdapter().apply {
            setOnItemClickListener { position, data ->
                val intent = Intent(context, DetailKarakterActivity::class.java)
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
                    showError("Karakter tidak ditemukan")
                }
            }

            if (loadState.append is LoadState.Error) {
                if (!PresentationUtils.isNetworkAvailable(requireContext())) showError("Tidak ada koneksi internet")
            }
        }
        binding.rvKarakter.layoutManager = GridLayoutManager(context, 2)
        binding.rvKarakter.adapter = adapter
    }

    private fun showError(error: String?) {
        PresentationUtils.setupDialogError(requireContext(), error ?: "")
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }.show()
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

            viewBottomSheet.acGender.setAdapter(adapterGender)
            viewBottomSheet.acStatus.setAdapter(adapterStatus)

            viewBottomSheet.btnGetKarakter.setOnClickListener {

                getAllData(
                    name = viewBottomSheet.etNamaKarakter.text.toString(),
                    gender = viewBottomSheet.acGender.text.toString(),
                    species = viewBottomSheet.etSearchSpecies.text.toString(),
                    type = viewBottomSheet.etSearchType.text.toString(),
                    status = viewBottomSheet.acStatus.text.toString()
                )

                dialog.dismiss()
            }

            dialog.setCancelable(true)
            dialog.setContentView(viewBottomSheet.root)
            dialog.show()
        }
    }

    private fun setToolbar() {
        binding.karakterToolbar.tvToolbar.text = "Karakter"
    }

    companion object {
        fun newInstance(viewModel: KarakterViewModel, application: Application): KarakterFragment {
            return KarakterFragment().apply {
                karakterViewModel = viewModel
                this.application = application
            }
        }
    }
}