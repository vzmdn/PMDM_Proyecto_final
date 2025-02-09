package com.vozmediano.vozmedianonasa.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.YearMonth

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        val yearAgo = LocalDate.now().minusDays(365).toString()

        viewModel.fetchPhoto(yearAgo)

        viewModel.photo.observe(this) { photo ->
            Glide
                .with(this)
                .load(photo.url)
                .error(R.drawable.baseline_image_not_supported_24)
                .into(binding.imageView)
            binding.cardDate.text = photo.date
            binding.cardTitle.text = photo.title
        }

        binding.infoBtn.setOnClickListener {
            binding.infoBtn.visibility = android.view.View.GONE
            binding.infoCardView.visibility = android.view.View.VISIBLE
        }

        binding.infoCardView.setOnClickListener {
            binding.infoBtn.visibility = android.view.View.VISIBLE
            binding.infoCardView.visibility = android.view.View.GONE
        }

        binding.today.setOnClickListener {
            val intent = Intent(this, TodayActivity::class.java)
            startActivity(intent)
        }

        binding.thisWeek.setOnClickListener {
            val intent = Intent(this, ThisWeekActivity::class.java)
            startActivity(intent)
        }

        binding.year.setMaxValue(YearMonth.now().year)
        binding.year.setMinValue(1995)
        binding.year.value = YearMonth.now().year;
        binding.month.maxValue = 12
        binding.month.minValue = 1
        binding.month.value = LocalDate.now().monthValue
        binding.day.maxValue = 31
        binding.day.minValue = 1
        binding.day.value = LocalDate.now().dayOfMonth

        binding.pickDate.setOnClickListener {
            val year = binding.year.value
            val month = binding.month.value
            val day = binding.day.value
            val date = "$year-$month-$day"
            if (isDateValid()) {
                val intent = Intent(this, PickDateActivity::class.java)
                intent.putExtra("date", date)
                startActivity(intent)
            } else {
                Log.i("", "date is not valid")
            }
        }
    }

    fun isDateValid(): Boolean {
        val localDate: LocalDate
        val year = binding.year.value
        val month = binding.month.value
        val day = binding.day.value
        try {
            localDate = LocalDate.of(year, month, day)
        } catch (e: Exception) {
            Toast.makeText(this, R.string.invalid_date, Toast.LENGTH_SHORT).show()
            return false
        }
        if (localDate.isAfter(LocalDate.now())) {
            Toast.makeText(this, R.string.future_date, Toast.LENGTH_SHORT).show()
            return false
        }
        if (localDate.isBefore(LocalDate.of(1995, 6, 16))) {
            Toast.makeText(this, R.string.minimum_date, Toast.LENGTH_SHORT).show()
            return false
        }
        return true

    }

}