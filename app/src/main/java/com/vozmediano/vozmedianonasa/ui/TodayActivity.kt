package com.vozmediano.vozmedianonasa.ui

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityTodayBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TodayActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTodayBinding.inflate(layoutInflater) }

    private val viewModel: TodayViewModel by viewModels { TodayViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        viewModel.fetchPhoto()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photo.collectLatest { photo ->
                    photo?.let {
                        Glide
                            .with(binding.imageView.context)
                            .load(it.url)
                            .error(R.drawable.baseline_image_not_supported_24)
                            .into(binding.imageView)

                        binding.title.text = it.title
                        binding.date.text = it.date
                        binding.explanation.text = it.explanation
                        binding.title.paintFlags = Paint.UNDERLINE_TEXT_FLAG

                        binding.title.setOnClickListener {
                            val titleText = photo.title.replace(" ", "+")
                            val searchIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.google.com/search?q=$titleText")
                            )
                            startActivity(searchIntent)
                        }

                        binding.imageView.setOnClickListener {
                            val intent = Intent(
                                this@TodayActivity,
                                FullscreenActivity::class.java
                            )
                            intent.putExtra("hdurl", photo.hdurl)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}

