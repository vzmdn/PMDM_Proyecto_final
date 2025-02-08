package com.vozmediano.vozmedianonasa.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityTodayBinding

class PickDateActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTodayBinding.inflate(layoutInflater) }

    private val viewModel: PickDateViewModel by viewModels { PickDateViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent

        val date = intent.getStringExtra("date")

        try{
            viewModel.fetchPhoto(date!!)
        } catch (e: Exception) {
            Log.e("", "", e)
        }

        viewModel.photo.observe(this) { photo ->
            Glide
                .with(this)
                .load(photo.url)
                .error(R.drawable.baseline_image_not_supported_24)
                .into(binding.imageView)
            binding.title.text = photo.title
            binding.date.text = photo.date
            binding.explanation.text = photo.explanation

            binding.imageView.setOnClickListener {
                val intent = Intent(this, FullscreenActivity::class.java)
                intent.putExtra("hdurl", photo.hdurl)
                startActivity(intent)
            }
        }
    }
}