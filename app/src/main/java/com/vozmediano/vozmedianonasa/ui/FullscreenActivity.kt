package com.vozmediano.vozmedianonasa.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityFullscreenBinding

class FullscreenActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFullscreenBinding.inflate(layoutInflater) }
    var fullscreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        hideSystemBars()

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

    private fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController?.let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun fullscreenSwap(fullscreen: Boolean) {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        if (fullscreen) {
            windowInsetsController?.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}