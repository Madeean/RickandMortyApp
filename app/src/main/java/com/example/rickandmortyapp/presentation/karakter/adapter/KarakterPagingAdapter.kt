package com.example.rickandmortyapp.presentation.karakter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ItemKarakterBinding
import com.example.rickandmortyapp.domain.karakter.model.network.KarakterModelItemModel

class KarakterPagingAdapter :
    PagingDataAdapter<KarakterModelItemModel, KarakterPagingAdapter.KarakterViewHolder>(
        DiffCallback
    ) {

    private var onItemClick: ((position: Int, data: KarakterModelItemModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int, data: KarakterModelItemModel) -> Unit) {
        onItemClick = listener
    }

    companion object {
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

    override fun onBindViewHolder(holder: KarakterViewHolder, position: Int) {
        holder.bindData(getItem(position) ?: return)
        
        holder.binding.root.setOnClickListener {
            onItemClick?.invoke(position, getItem(position) ?: return@setOnClickListener)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KarakterViewHolder {
        val binding = ItemKarakterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return KarakterViewHolder(binding)
    }


}