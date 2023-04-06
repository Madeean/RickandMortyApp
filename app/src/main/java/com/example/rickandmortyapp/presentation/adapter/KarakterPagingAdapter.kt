package com.example.rickandmortyapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ItemEpisodeBinding
import com.example.rickandmortyapp.databinding.ItemKarakterBinding
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel

class KarakterPagingAdapter : PagingDataAdapter<KarakterModelItemModel, KarakterPagingAdapter.KarakterViewHolder>(
    DiffCallback
) {

    companion object{
        object DiffCallback : DiffUtil.ItemCallback<KarakterModelItemModel>() {
            override fun areItemsTheSame(
                oldItem: KarakterModelItemModel,
                newItem: KarakterModelItemModel
            ): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(
                oldItem: KarakterModelItemModel,
                newItem: KarakterModelItemModel
            ): Boolean {
                return (oldItem == newItem)
            }

        }
    }

    class KarakterViewHolder(val binding: ItemKarakterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(episode: KarakterModelItemModel) {
            with(binding) {
                tvNamaKarakter.text = episode.name
                tvGenderKarakter.text = episode.gender
            }
            Glide.with(itemView.context)
                .load(episode.image)
                .placeholder(R.drawable.rickandmorty)
                .into(binding.ivImage)
        }
    }

    override fun onBindViewHolder(holder: KarakterPagingAdapter.KarakterViewHolder, position: Int) {
        holder.bindData(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KarakterPagingAdapter.KarakterViewHolder {
        val binding = ItemKarakterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return KarakterViewHolder(binding)
    }
}