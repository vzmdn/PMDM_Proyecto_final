package com.vozmediano.vozmedianonasa.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityFullscreenBinding

class FullscreenActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFullscreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val hdurl = intent.getStringExtra("hdurl")

        Glide.with(this)
            .load(hdurl)
            .error(R.drawable.baseline_image_not_supported_24)
            .into(binding.fullscreenImageView)
    }
}