package com.vozmediano.vozmedianonasa.ui

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.vozmediano.vozmedianonasa.R
import com.vozmediano.vozmedianonasa.databinding.ActivityFullscreenBinding

class FullscreenActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private val binding by lazy { ActivityFullscreenBinding.inflate(layoutInflater) }
    private lateinit var gestureDetector: GestureDetector
    private var fullscreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        gestureDetector = GestureDetector(this, this).apply {
            setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    fullscreenSwap(!fullscreen)
                    return true
                }
                override fun onDoubleTap(e: MotionEvent) = false
                override fun onDoubleTapEvent(e: MotionEvent) = false
            })
        }

        fullscreenSwap(fullscreen)

        Glide.with(this)
            .load(intent.getStringExtra("hdurl"))
            .error(R.drawable.baseline_image_not_supported_24)
            .into(binding.fullscreenImageView)

        binding.returnButton.setOnClickListener { finish() }
        binding.menuButton.setOnClickListener { Toast.makeText(this, "TODO: Descargar/Compartir imagen", Toast.LENGTH_LONG).show() }

        binding.root.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event); true }
    }

    private fun fullscreenSwap(fullscreen: Boolean) {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        if (fullscreen) {
            windowInsetsController?.hide(WindowInsetsCompat.Type.systemBars())
            windowInsetsController?.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            binding.returnButton.visibility = android.view.View.GONE
            binding.menuButton.visibility = android.view.View.GONE
        } else {
            windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
            binding.returnButton.visibility = android.view.View.VISIBLE
            binding.menuButton.visibility = android.view.View.VISIBLE
        }
        this.fullscreen = fullscreen
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        event?.let { onTouchEvent(it) }
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (binding.menuButton.isPressed) return false
            return gestureDetector.onTouchEvent(it) || super.onTouchEvent(it)
        }
        return false
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean = false

    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean = false

    override fun onDown(e: MotionEvent) = true
    override fun onShowPress(e: MotionEvent) {}
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float) = true
    override fun onLongPress(e: MotionEvent) {}
}
