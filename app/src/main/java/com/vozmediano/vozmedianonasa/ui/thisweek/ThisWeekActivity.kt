package com.vozmediano.vozmedianonasa.ui.thisweek

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityThisWeekBinding
import com.vozmediano.vozmedianonasa.ui.FullscreenActivity
import com.vozmediano.vozmedianonasa.ui.PhotoListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
        val startDate = LocalDate.now().minusDays(7).format(formatter)
        val endDate = LocalDate.now().minusDays(1).format(formatter)

        viewModel.fetchPhotos(startDate, endDate)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photos.collectLatest { photos ->
                    photoAdapter.submitList(photos)
                }
            }
        }

        photoAdapter.onItemClick = { photo ->
            val intent = Intent(this, FullscreenActivity::class.java)
            intent.putExtra("hdurl", photo.hdurl)
            startActivity(intent)
        }

    }
}