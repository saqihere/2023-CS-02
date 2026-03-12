package com.smarteventmanager

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.smarteventmanager.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()

        binding.btnBegin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun startAnimations() {
        // App Name Animation: Slide Up and Fade In
        binding.tvAppName.alpha = 0f
        val moveTitle = ObjectAnimator.ofFloat(binding.tvAppName, View.TRANSLATION_Y, 100f, 0f)
        val fadeInTitle = ObjectAnimator.ofFloat(binding.tvAppName, View.ALPHA, 0f, 1f)

        // Subtitle Animation
        binding.tvSubtitle.alpha = 0f
        val moveSubtitle = ObjectAnimator.ofFloat(binding.tvSubtitle, View.TRANSLATION_Y, 50f, 0f)
        val fadeInSubtitle = ObjectAnimator.ofFloat(binding.tvSubtitle, View.ALPHA, 0f, 1f)
        
        // Indicator
        binding.yellowIndicator.scaleX = 0f
        val scaleIndicator = ObjectAnimator.ofFloat(binding.yellowIndicator, View.SCALE_X, 0f, 1f)

        // Button
        binding.btnBegin.alpha = 0f
        val fadeInBtn = ObjectAnimator.ofFloat(binding.btnBegin, View.ALPHA, 0f, 1f)

        val set = AnimatorSet().apply {
            playTogether(moveTitle, fadeInTitle, scaleIndicator)
            duration = 1000
            interpolator = AnticipateOvershootInterpolator()
        }

        val subtitleSet = AnimatorSet().apply {
            playTogether(moveSubtitle, fadeInSubtitle, fadeInBtn)
            duration = 800
            startDelay = 500
            interpolator = DecelerateInterpolator()
        }

        set.start()
        subtitleSet.start()
        
        // Background Pulse
        ObjectAnimator.ofFloat(binding.yellowIndicator, View.ALPHA, 0.4f, 1f).apply {
            duration = 1500
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            start()
        }
    }
}