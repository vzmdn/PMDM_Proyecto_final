package com.vozmediano.vozmedianonasa.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityThisWeekBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ThisWeekActivity : AppCompatActivity() {

    private val binding by lazy { ActivityThisWeekBinding.inflate(layoutInflater) }
    private val viewModel: ThisWeekViewModel by viewModels { ThisWeekViewModel.Factory }
    private val photoAdapter by lazy { PhotoListAdapter {   } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.recyclerView.adapter = photoAdapter

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate = LocalDate.now().minusDays(8).format(formatter)
        val endDate = LocalDate.now().minusDays(1).format(formatter)

        viewModel.fetchPhotos(startDate, endDate)

        viewModel.photos.observe(this) { photos ->
            photoAdapter.submitList(photos)
        }
    }
}