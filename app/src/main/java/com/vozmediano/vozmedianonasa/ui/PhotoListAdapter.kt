package com.vozmediano.vozmedianonasa.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityTodayBinding
import com.vozmediano.vozmedianonasa.domain.model.Photo

class PhotoListAdapter(val onItemClick: (Photo) -> Unit) : ListAdapter<Photo, PhotoListAdapter.PhotoViewHolder>(DIFF_CALLBACK) {


    class PhotoViewHolder(
        private val binding: ActivityTodayBinding,
        val onItemClick : (Photo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(photo: Photo) {
            Glide
                .with(binding.imageView)
                .load(photo.url)
                .error(R.drawable.baseline_image_not_supported_24)
                .into(binding.imageView)
            binding.title.text = photo.title
            binding.date.text = photo.date
            binding.explanation.text = photo.explanation

            itemView.setOnClickListener { onItemClick(photo) }


        }
    }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ActivityTodayBinding.inflate(layoutInflater)
            return PhotoViewHolder(binding, onItemClick)
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            holder.bind(currentList[position])
        }


        companion object {
            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {
                override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
                override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
                    oldItem.date == newItem.date
            }
        }
    }
