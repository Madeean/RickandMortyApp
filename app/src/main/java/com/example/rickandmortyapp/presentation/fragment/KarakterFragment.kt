package com.example.rickandmortyapp.presentation.fragment

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentKarakterBinding
import com.example.rickandmortyapp.databinding.KarakterBottomSheetDialogBinding
import com.example.rickandmortyapp.presentation.adapter.EpisodePagingAdapter
import com.example.rickandmortyapp.presentation.adapter.KarakterPagingAdapter
import com.example.rickandmortyapp.presentation.viewmodel.episode.EpisodeViewModel
import com.example.rickandmortyapp.presentation.viewmodel.karakter.KarakterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class KarakterFragment : Fragment() {
    private lateinit var binding: FragmentKarakterBinding
    private lateinit var karakterViewModel: KarakterViewModel
    private lateinit var application: Application
    private lateinit var karakterBottomSheetDialog: KarakterBottomSheetDialogBinding
    private lateinit var adapter: KarakterPagingAdapter

    private var name: String? = null
    private var gender: String? = null
    private var status: String? = null
    private var species: String? = null
    private var type: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentKarakterBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        setToolbar()
        setBottomSheetDialog()
        setRecyclerView()
        getAllData()

    }

    private fun getAllData() {
        println("MASUK 1")
        lifecycleScope.launch {
            karakterViewModel.getAllKarakter(
                application, name ?: "", status ?: "", species ?: "", type ?: "", gender ?: ""
            ).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setRecyclerView() {
        adapter = KarakterPagingAdapter()
        binding.rvKarakter.layoutManager = GridLayoutManager(context, 2)
        binding.rvKarakter.adapter = adapter
    }

    private fun setBinding() {
        karakterBottomSheetDialog = KarakterBottomSheetDialogBinding.inflate(layoutInflater)
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
                println(
                    """
                    ${viewBottomSheet.acGender.text}
                    ${viewBottomSheet.acStatus.text}
                    ${viewBottomSheet.etNamaKarakter.text}
                    ${viewBottomSheet.etSearchType.text}
                    ${viewBottomSheet.etSearchSpecies.text}
                """.trimIndent()
                )
                dialog.dismiss()
            }

            dialog.setCancelable(true)
            dialog.setContentView(viewBottomSheet.root)
            dialog.show()
        }
    }

    private fun setToolbar() {
        binding.karakterToolbar.myToolbarTitle.text = "Karakter"
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