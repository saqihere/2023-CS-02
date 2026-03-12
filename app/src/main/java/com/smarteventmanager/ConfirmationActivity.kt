package com.smarteventmanager

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.smarteventmanager.databinding.ActivityConfirmationBinding

class ConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("NAME") ?: ""
        val email = intent.getStringExtra("EMAIL") ?: ""
        val phone = intent.getStringExtra("PHONE") ?: ""
        val eventType = intent.getStringExtra("EVENT") ?: ""
        val date = intent.getStringExtra("DATE") ?: ""
        val gender = intent.getStringExtra("GENDER") ?: ""
        val imageUri = intent.getStringExtra("IMAGE")

        if (!imageUri.isNullOrEmpty() && imageUri != "null") {
            Glide.with(this).load(imageUri).circleCrop().into(binding.ivProfile)
        }

        binding.llDetails.removeAllViews()

        addDetailRow("Full Name", name)
        addDetailRow("Phone Number", phone)
        addDetailRow("Email Address", email)
        addDetailRow("Pass Type", eventType)
        addDetailRow("Event Date", date)
        addDetailRow("Gender", gender)
        addDetailRow("Gate ID", "#X-" + (100..999).random() + "-UVN")

        startAnimations()

        binding.btnBackHome.setOnClickListener {
            // Acknowledge and return to the Welcome/Splash page
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun addDetailRow(label: String, value: String) {
        val rowView = LayoutInflater.from(this).inflate(R.layout.item_detail_row, binding.llDetails, false)
        
        val labelTv = rowView.findViewById<TextView>(R.id.tvRowLabel)
        labelTv.text = label
        labelTv.setTextColor(getColor(R.color.uvento_text_slate))
        
        val valueTv = rowView.findViewById<TextView>(R.id.tvRowValue)
        valueTv.text = if (value.isEmpty()) "Not provided" else value
        valueTv.setTextColor(getColor(R.color.uvento_text_white))
        
        binding.llDetails.addView(rowView)
    }

    private fun startAnimations() {
        // Snappier animations to avoid the "blank screen" perception
        binding.ivCheckBg.scaleX = 0f
        binding.ivCheckBg.scaleY = 0f
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f)
        ObjectAnimator.ofPropertyValuesHolder(binding.ivCheckBg, scaleX, scaleY).apply {
            duration = 500
            interpolator = AnticipateOvershootInterpolator()
            start()
        }

        ValueAnimator.ofFloat(0.4f, 1f).apply {
            duration = 1500
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animator ->
                binding.glowView.alpha = animator.animatedValue as Float
            }
            start()
        }

        // Show success title and message quickly
        binding.tvSuccessTitle.alpha = 0f
        binding.tvSuccessTitle.translationY = 20f
        binding.tvSuccessTitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(400)
            .setStartDelay(200)
            .start()

        binding.tvSuccessSubtitle.alpha = 0f
        binding.tvSuccessSubtitle.animate()
            .alpha(1f)
            .setDuration(400)
            .setStartDelay(300)
            .start()

        binding.passScrollView.alpha = 0f
        binding.passScrollView.translationY = 50f
        binding.passScrollView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(400)
            .start()
    }
}