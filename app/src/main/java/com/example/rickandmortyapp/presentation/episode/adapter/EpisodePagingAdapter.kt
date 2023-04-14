package com.example.rickandmortyapp.presentation.episode.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyapp.databinding.ItemEpisodeBinding
import com.example.rickandmortyapp.domain.episode.model.network.EpisodeModelItemModel

class EpisodePagingAdapter : PagingDataAdapter<EpisodeModelItemModel, EpisodePagingAdapter.EpisodeViewHolder>(
    DiffCallback
) {

    private var onItemClick: ((position: Int,data: EpisodeModelItemModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int,data: EpisodeModelItemModel) -> Unit) {
        onItemClick = listener
    }

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
        }
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bindData(getItem(position) ?: return)

        holder.binding.btnDetail.setOnClickListener {
            onItemClick?.invoke(position,getItem(position) ?: return@setOnClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemEpisodeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EpisodeViewHolder(binding)
    }
}