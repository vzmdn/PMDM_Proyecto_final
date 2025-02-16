package com.vozmediano.vozmedianonasa.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityFullscreenBinding

class FullscreenActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFullscreenBinding.inflate(layoutInflater) }
    var fullscreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fullscreenSwap(fullscreen)

        val hdurl = intent.getStringExtra("hdurl")

        Glide.with(this)
            .load(hdurl)
            .error(R.drawable.baseline_image_not_supported_24)
            .into(binding.fullscreenImageView)

        binding.fullscreenImageView.setOnClickListener {
            fullscreenSwap(fullscreen)
            fullscreen = !fullscreen
        }

        binding.returnButton.setOnClickListener {
            finish()
        }

        binding.menuButton.setOnClickListener {
            Toast.makeText(this, "Menu", Toast.LENGTH_LONG).show()
        }
    }



    fun fullscreenSwap(fullscreen: Boolean) {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        if (fullscreen) {
            windowInsetsController?.hide(WindowInsetsCompat.Type.systemBars())
            binding.returnButton.visibility = android.view.View.GONE
            binding.menuButton.visibility = android.view.View.GONE
        } else {
            windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
            binding.returnButton.visibility = android.view.View.VISIBLE
            binding.menuButton.visibility = android.view.View.VISIBLE
        }
    }
}