package com.vozmediano.vozmedianonasa.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
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
import com.google.android.material.snackbar.Snackbar
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import android.widget.Button
import com.vozmediano.vozmedianonasa.ui.pickdate.PickDateActivity
import com.vozmediano.vozmedianonasa.ui.thisweek.ThisWeekActivity
import com.vozmediano.vozmedianonasa.ui.today.TodayActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var date = ""
        var text = ""

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        val yearAgo = LocalDate.now().minusDays(365).toString()

        viewModel.fetchPhoto(yearAgo)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photo.collectLatest { photo ->
                    photo?.let {
                        Glide
                            .with(binding.imageView.context)
                            .load(it.url)
                            .error(R.drawable.baseline_image_not_supported_24)
                            .into(binding.imageView)
                        date = it.date
                        text = it.title
                    }
                }
            }
        }

        binding.info.setOnClickListener {
            val snackbar = Snackbar
                .make(binding.root, text, Snackbar.LENGTH_INDEFINITE)

            val snackbarView = snackbar.view
            val layoutParams = snackbarView.layoutParams as FrameLayout.LayoutParams
            layoutParams.gravity = Gravity.TOP
            layoutParams.setMargins(0, 150, 0, 0)

            snackbarView.layoutParams = layoutParams

            snackbar.show()
            Handler(Looper.getMainLooper()).postDelayed({
                snackbar.dismiss()
            }, 8000)

            binding.day.value = extractDay(date)
            binding.month.value = extractMonth(date)
            binding.year.value = extractYear(date)

            flashButton(binding.pickDate)
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
        binding.year.value = YearMonth.now().year
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

    private fun isDateValid(): Boolean {
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

    private fun extractDay(date: String): Int {
        return date.substring(8, 10).toInt()
    }

    private fun extractMonth(date: String): Int {
        return date.substring(5, 7).toInt()
    }
    private fun extractYear(date: String): Int {
        return date.substring(0, 4).toInt()
    }



    private fun flashButton(button: Button) {
        val originalColor = (button.background as? ColorDrawable)?.color ?: Color.TRANSPARENT

        val colorAnimator = ObjectAnimator.ofArgb(
            button,
            "backgroundColor",
            Color.parseColor("#7195D999")
        )
        colorAnimator.duration = 100
        colorAnimator.repeatCount = 3
        colorAnimator.repeatMode = ObjectAnimator.REVERSE
        colorAnimator.setEvaluator(ArgbEvaluator())
        colorAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                button.setBackgroundColor(originalColor)
            }
        })
        colorAnimator.start()
    }



}