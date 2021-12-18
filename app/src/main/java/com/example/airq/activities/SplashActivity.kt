package com.example.airq.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.airq.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Light)
        setContentView(R.layout.activity_splash)
        splashCloudLeft.animate().setInterpolator(AccelerateDecelerateInterpolator()).translationX(0f).duration = 1500
        splashCloudRight.animate().setInterpolator(AccelerateDecelerateInterpolator()).translationX(0f).duration = 1500
        handler.postDelayed({
            startActivity(Intent(this@SplashActivity, CitiesActivity::class.java))
            finish()
        }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}