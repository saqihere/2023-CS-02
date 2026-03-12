package com.smarteventmanager

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.smarteventmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        animateEntry()
    }

    private fun setupUI() {
        // Updated image for Discover page
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1533174072545-7a4b6ad7a6c3?w=800")
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.image_corner_radius)))
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(binding.ivEventImage)

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    private fun animateEntry() {
        binding.headerView.translationY = -200f
        binding.tvHeaderTitle.alpha = 0f
        binding.btnRegister.scaleX = 0f
        binding.btnRegister.scaleY = 0f
        
        val headerSlide = ObjectAnimator.ofFloat(binding.headerView, View.TRANSLATION_Y, -200f, 0f)
        val titleFade = ObjectAnimator.ofFloat(binding.tvHeaderTitle, View.ALPHA, 0f, 1f)
        val btnScaleX = ObjectAnimator.ofFloat(binding.btnRegister, View.SCALE_X, 0f, 1f)
        val btnScaleY = ObjectAnimator.ofFloat(binding.btnRegister, View.SCALE_Y, 0f, 1f)

        AnimatorSet().apply {
            playTogether(headerSlide, titleFade, btnScaleX, btnScaleY)
            duration = 800
            interpolator = DecelerateInterpolator()
            start()
        }
    }
}