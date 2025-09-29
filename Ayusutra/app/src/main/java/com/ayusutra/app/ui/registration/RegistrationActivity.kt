package com.ayusutra.app.ui.registration

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ayusutra.app.R
import com.ayusutra.app.databinding.ActivityRegistrationBinding
import com.ayusutra.app.utils.PreferenceManager
import com.ayusutra.app.utils.UhidGenerator
import java.text.SimpleDateFormat
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var preferenceManager: PreferenceManager
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        setupGenderDropdown()
        setupDatePicker()
        setupRegisterButton()
        setupLoginPrompt()
    }

    private fun setupGenderDropdown() {
        val genders = arrayOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genders)
        binding.actvGender.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        binding.etDateOfBirth.setOnClickListener {
            showDatePickerDialog()
        }

        binding.tilDateOfBirth.setEndIconOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        
        // Set max date to current date (no future dates)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDateInView() {
        binding.etDateOfBirth.setText(dateFormatter.format(calendar.time))
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            if (validateInputs()) {
                registerUser()
            }
        }
    }

    private fun setupLoginPrompt() {
        binding.tvLoginPrompt.setOnClickListener {
            // In a real app, this would navigate to a login screen
            // For now, we'll just show a toast
            Toast.makeText(this, "Login functionality will be implemented soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate name
        if (binding.etFullName.text.toString().trim().isEmpty()) {
            binding.tilFullName.error = "Name is required"
            isValid = false
        } else {
            binding.tilFullName.error = null
        }

        // Validate mobile
        val mobile = binding.etMobile.text.toString().trim()
        if (mobile.isEmpty()) {
            binding.tilMobile.error = "Mobile number is required"
            isValid = false
        } else if (mobile.length != 10) {
            binding.tilMobile.error = "Enter a valid 10-digit mobile number"
            isValid = false
        } else {
            binding.tilMobile.error = null
        }

        // Validate email (optional)
        val email = binding.etEmail.text.toString().trim()
        if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Enter a valid email address"
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        // Validate date of birth
        if (binding.etDateOfBirth.text.toString().trim().isEmpty()) {
            binding.tilDateOfBirth.error = "Date of birth is required"
            isValid = false
        } else {
            binding.tilDateOfBirth.error = null
        }

        // Validate gender
        if (binding.actvGender.text.toString().trim().isEmpty()) {
            binding.tilGender.error = "Gender is required"
            isValid = false
        } else {
            binding.tilGender.error = null
        }

        return isValid
    }

    private fun registerUser() {
        // Get user inputs
        val name = binding.etFullName.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        // Generate UHID
        val uhid = UhidGenerator.generateUhid()

        // Save user data
        preferenceManager.saveUserProfile(name, email, mobile)
        preferenceManager.saveUserUhid(uhid)
        preferenceManager.setUserRegistered(true)

        // Navigate to UHID confirmation screen
        val intent = Intent(this, UhidConfirmationActivity::class.java)
        intent.putExtra(UhidConfirmationActivity.EXTRA_UHID, uhid)
        intent.putExtra(UhidConfirmationActivity.EXTRA_NAME, name)
        startActivity(intent)
        finish()
    }
}