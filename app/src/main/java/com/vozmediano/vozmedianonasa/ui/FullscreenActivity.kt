package com.vozmediano.vozmedianonasa.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityFullscreenBinding

class FullscreenActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFullscreenBinding.inflate(layoutInflater) }
    var fullscreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
        )

        val hdurl = intent.getStringExtra("hdurl")

        Glide.with(this)
            .load(hdurl)
            .error(R.drawable.baseline_image_not_supported_24)
            .into(binding.fullscreenImageView)

        binding.fullscreenImageView.setOnClickListener {
            fullscreenSwap(fullscreen)
            fullscreen = !fullscreen
        }
    }

    fun fullscreenSwap(fullscreen: Boolean) {
        if (fullscreen) {
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
            )
        } else {
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
        }

    }
}