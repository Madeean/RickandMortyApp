package com.example.rickandmortyapp.presentation.location.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyapp.databinding.ItemEpisodeBinding
import com.example.rickandmortyapp.domain.model.location.LocationModelItemModel

class LocationPagingAdapter : PagingDataAdapter<LocationModelItemModel, LocationPagingAdapter.LocationViewHolder>(
    DiffCallback
) {
    private var onItemClick: ((position: Int, data: LocationModelItemModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int, data: LocationModelItemModel) -> Unit) {
        onItemClick = listener
    }
    companion object {
        object DiffCallback : DiffUtil.ItemCallback<LocationModelItemModel>() {
            override fun areItemsTheSame(
                oldItem: LocationModelItemModel,
                newItem: LocationModelItemModel
            ): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(
                oldItem: LocationModelItemModel,
                newItem: LocationModelItemModel
            ): Boolean {
                return (oldItem == newItem)
            }

        }
    }

    class LocationViewHolder(val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(location: LocationModelItemModel) {
            with(binding) {
                tvJudul.text = location.name
                tvEpisode.text = location.type
                tvDate.text = location.dimension
            }

        }
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bindData(getItem(position) ?: return)

        holder.binding.btnDetail.setOnClickListener {
            onItemClick?.invoke(position, getItem(position) ?: return@setOnClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemEpisodeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LocationViewHolder(binding)
    }
}