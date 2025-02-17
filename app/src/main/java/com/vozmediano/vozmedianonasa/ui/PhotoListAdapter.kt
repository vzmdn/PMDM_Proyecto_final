package com.vozmediano.vozmedianonasa.ui

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityTodayBinding
import com.vozmediano.vozmedianonasa.domain.model.Photo
import com.vozmediano.vozmedianonasa.utils.Utils

class PhotoListAdapter(var onItemClick: (Photo) -> Unit) : ListAdapter<Photo, PhotoListAdapter.PhotoViewHolder>(DIFF_CALLBACK) {


    class PhotoViewHolder(
        val binding: ActivityTodayBinding,
        //val onItemClick : (Photo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(photo: Photo) {
            var imageUrl: String? = ""
            binding.date.text = photo.date
            binding.explanation.text = photo.explanation
            binding.title.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.title.text = photo.title

            if(photo.mediaType == "video"){
                binding.mediaType.text = "🎥"
                imageUrl = Utils.getVideoThumbnail(photo.url)
                binding.imageView.setOnClickListener{
                    Toast.makeText(binding.imageView.context, "Click on the title link to open the video", Toast.LENGTH_SHORT).show()
                }
                binding.title.setOnClickListener{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(photo.url))
                    binding.imageView.context.startActivity(intent)
                }
            }
            else {
                binding.mediaType.text = "📸"
                imageUrl = photo.url
                binding.imageView.setOnClickListener{
                    val context = binding.imageView.context
                    val intent = Intent(context, FullscreenActivity::class.java)
                    intent.putExtra("hdurl", photo.hdurl)
                    context.startActivity(intent)
                }
                binding.title.setOnClickListener{
                    val titleText = photo.title.replace(" ", "+")
                    val searchIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$titleText"))
                    binding.imageView.context.startActivity(searchIntent)
                }

            }

            Glide
                .with(binding.imageView)
                .load(imageUrl)
                .error(R.drawable.baseline_image_not_supported_24)
                .into(binding.imageView)
        }
    }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ActivityTodayBinding.inflate(layoutInflater)
            return PhotoViewHolder(binding)
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            holder.binding.imageView.layout(0,0,0,0)
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
