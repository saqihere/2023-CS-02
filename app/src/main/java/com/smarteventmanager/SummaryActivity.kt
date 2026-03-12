package com.smarteventmanager

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.smarteventmanager.databinding.ActivitySummaryBinding

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras
        if (data != null) {
            val imageUri = data.getString("IMAGE")
            if (!imageUri.isNullOrEmpty() && imageUri != "null") {
                Glide.with(this).load(imageUri).circleCrop().into(binding.ivReviewProfile)
            }

            addSummaryRow("Full Name", data.getString("NAME", ""))
            addSummaryRow("Email", data.getString("EMAIL", ""))
            addSummaryRow("Phone", data.getString("PHONE", ""))
            addSummaryRow("Pass Type", data.getString("EVENT", ""))
            addSummaryRow("Event Date", data.getString("DATE", ""))
            addSummaryRow("Gender", data.getString("GENDER", ""))
        }

        animateEntry()

        binding.btnBackSummary.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnConfirm.setOnClickListener {
            val intent = Intent(this, ConfirmationActivity::class.java)
            if (data != null) intent.putExtras(data)
            startActivity(intent)
        }
    }

    private fun addSummaryRow(label: String, value: String) {
        val rowView = LayoutInflater.from(this).inflate(R.layout.item_detail_row, binding.llSummaryDetails, false)
        rowView.findViewById<TextView>(R.id.tvRowLabel).apply {
            text = label
            setTextColor(getColor(R.color.uvento_text_slate))
        }
        rowView.findViewById<TextView>(R.id.tvRowValue).apply {
            text = if (value.isNullOrEmpty()) "N/A" else value
            setTextColor(getColor(R.color.uvento_text_white))
        }
        binding.llSummaryDetails.addView(rowView)
    }

    private fun animateEntry() {
        binding.ivReviewProfile.alpha = 0f
        binding.ivReviewProfile.scaleX = 0.5f
        binding.ivReviewProfile.scaleY = 0.5f
        binding.ivReviewProfile.animate()
            .alpha(1f).scaleX(1f).scaleY(1f)
            .setDuration(600)
            .setInterpolator(DecelerateInterpolator())
            .start()

        binding.llSummaryDetails.alpha = 0f
        binding.llSummaryDetails.translationY = 100f
        binding.llSummaryDetails.animate()
            .alpha(1f).translationY(0f)
            .setDuration(800)
            .setStartDelay(200)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }
}