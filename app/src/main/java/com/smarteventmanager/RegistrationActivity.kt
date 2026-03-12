package com.smarteventmanager

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.smarteventmanager.databinding.ActivityRegistrationBinding
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private var selectedImageUri: Uri? = null
    private var selectedDate: String = ""

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            Glide.with(this).load(uri).circleCrop().into(binding.ivProfile)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        setupListeners()
        animateEntry()
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.event_types,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEventType.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnChooseImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, R.style.Theme_SmartEventManager_Dialog, { _, year, month, day ->
                selectedDate = String.format("%02d/%02d/%d", day, month + 1, year)
                binding.tvSelectedDate.text = selectedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.btnSubmit.setOnClickListener {
            if (validateForm()) {
                // Navigate to Verified Data (Summary) Page
                val intent = Intent(this, SummaryActivity::class.java).apply {
                    putExtra("NAME", binding.etFullName.text.toString().trim())
                    putExtra("EMAIL", binding.etEmail.text.toString().trim())
                    putExtra("PHONE", binding.etPhone.text.toString().trim())
                    putExtra("EVENT", binding.spinnerEventType.selectedItem.toString())
                    putExtra("DATE", selectedDate)
                    val genderId = binding.rgGender.checkedRadioButtonId
                    val gender = when(genderId) {
                        R.id.rbMale -> "Male"
                        R.id.rbFemale -> "Female"
                        else -> "Other"
                    }
                    putExtra("GENDER", gender)
                    putExtra("IMAGE", selectedImageUri?.toString())
                }
                startActivity(intent)
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true
        val name = binding.etFullName.text.toString().trim()
        if (name.isEmpty() || name.length <= 2) {
            binding.tilFullName.error = "Name must be more than 2 chars"
            isValid = false
        } else { binding.tilFullName.error = null }

        val email = binding.etEmail.text.toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Enter valid email"
            isValid = false
        } else { binding.tilEmail.error = null }

        val phone = binding.etPhone.text.toString().trim()
        if (phone.length != 11 || !phone.startsWith("03")) {
            binding.tilPhone.error = "Exactly 11 digits, must start with 03"
            isValid = false
        } else { binding.tilPhone.error = null }

        if (binding.spinnerEventType.selectedItemPosition == 0) {
            Toast.makeText(this, "Select a Pass Type", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Pick an Event Date", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (binding.rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Select your Gender", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (selectedImageUri == null) {
            Toast.makeText(this, "Choose an identity image", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (!binding.cbTerms.isChecked) {
            Toast.makeText(this, "Accept Terms and Conditions", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        return isValid
    }

    private fun animateEntry() {
        val views = arrayOf(
            binding.tilFullName, binding.tilEmail, binding.tilPhone, 
            binding.cardSpinner, binding.cardDatePicker, binding.cardGender,
            binding.cbTerms, binding.btnSubmit
        )
        views.forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 50f
            view.animate().alpha(1f).translationY(0f).setDuration(500).setStartDelay(80L * index).start()
        }
    }
}