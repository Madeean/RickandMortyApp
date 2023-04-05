package com.example.rickandmortyapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.databinding.ItemEpisodeBinding
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel

class EpisodePagingAdapter : PagingDataAdapter<EpisodeModelItemModel,EpisodePagingAdapter.EpisodeViewHolder>(DiffCallback) {
    companion object {
        object DiffCallback : DiffUtil.ItemCallback<EpisodeModelItemModel>() {
            override fun areItemsTheSame(
                oldItem: EpisodeModelItemModel,
                newItem: EpisodeModelItemModel
            ): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(
                oldItem: EpisodeModelItemModel,
                newItem: EpisodeModelItemModel
            ): Boolean {
                return (oldItem == newItem)
            }

        }
    }

    class EpisodeViewHolder(val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(episode: EpisodeModelItemModel) {
            with(binding) {
                tvJudul.text = episode.name
                tvEpisode.text = episode.episode
                tvDate.text = episode.airDate
            }
//            Glide.with(itemView.context)
//                .load(Utils.ImageUrl + movie.posterPath)
//                .into(binding.ivPosterMovie)
        }
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bindData(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemEpisodeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EpisodeViewHolder(binding)
    }
}