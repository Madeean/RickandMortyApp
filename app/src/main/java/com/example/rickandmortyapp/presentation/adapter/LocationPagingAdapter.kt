package com.example.rickandmortyapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.databinding.ItemEpisodeBinding
import com.example.rickandmortyapp.domain.model.location.LocationModelItemModel

class LocationPagingAdapter : PagingDataAdapter<LocationModelItemModel,LocationPagingAdapter.LocationViewHolder>(DiffCallback) {
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
//            Glide.with(itemView.context)
//                .load(Utils.ImageUrl + movie.posterPath)
//                .into(binding.ivPosterMovie)
        }
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bindData(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemEpisodeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LocationViewHolder(binding)
    }
}