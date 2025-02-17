package com.vozmediano.vozmedianonasa.ui.pickdate

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.vozmediano.vozmedianonasa.ui.FullscreenActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.vozmediano.vozmedianonasa.utils.Utils

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

        viewModel.fetchPhoto(date!!)

        var imageUrl:String? = ""

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photo.collectLatest { photo ->
                    photo?.let { it ->
                        binding.date.text = it.date
                        binding.explanation.text = it.explanation
                        binding.title.paintFlags = Paint.UNDERLINE_TEXT_FLAG

                        if (photo.mediaType == "video") {
                            binding.title.text = it.title
                            binding.mediaType.text = "🎥"
                            imageUrl = Utils.getVideoThumbnail(photo.url)
                            binding.imageView.setOnClickListener{
                                Toast.makeText(binding.imageView.context, "Click on the title link to open the video", Toast.LENGTH_SHORT).show()
                            }

                            binding.title.setOnClickListener {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(photo.url)
                                )
                                startActivity(intent)
                            }
                        } else {
                            binding.title.text = it.title
                            binding.mediaType.text = "📸"
                            imageUrl = photo.url
                            binding.imageView.setOnClickListener {
                                val intent = Intent(
                                    this@PickDateActivity,
                                    FullscreenActivity::class.java
                                )
                                intent.putExtra("hdurl", photo.hdurl)
                                startActivity(intent)
                            }

                            binding.title.setOnClickListener {
                                val titleText = photo.title.replace(" ", "+")
                                val searchIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://www.google.com/search?q=$titleText")
                                )
                                startActivity(searchIntent)
                            }
                        }

                        Glide
                            .with(binding.imageView.context)
                            .load(imageUrl)
                            .error(R.drawable.baseline_image_not_supported_24)
                            .into(binding.imageView)

                    }
                }
            }
        }
    }





}