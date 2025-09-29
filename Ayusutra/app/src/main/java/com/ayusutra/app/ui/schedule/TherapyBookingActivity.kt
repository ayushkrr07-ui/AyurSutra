package com.ayusutra.app.ui.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ayusutra.app.R
import com.ayusutra.app.data.models.Therapy
import com.ayusutra.app.data.models.TherapyStatus
import com.ayusutra.app.databinding.ActivityTherapyBookingBinding
import com.ayusutra.app.utils.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*

class TherapyBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTherapyBookingBinding
    private lateinit var preferenceManager: PreferenceManager
    private val calendar = Calendar.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTherapyBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        preferenceManager = PreferenceManager(this)
        
        setupToolbar()
        setupPatientInfo()
        setupTherapyTypeDropdown()
        setupDatePicker()
        setupTimePicker()
        setupDurationDropdown()
        setupTherapistDropdown()
        setupBookingButton()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    
    private fun setupPatientInfo() {
        val patientName = preferenceManager.getUserName() ?: "Unknown"
        val patientUhid = preferenceManager.getUserUhid() ?: "Unknown"
        
        binding.tvPatientName.text = patientName
        binding.tvPatientUhid.text = "UHID: $patientUhid"
    }
    
    private fun setupTherapyTypeDropdown() {
        val therapyTypes = listOf(
            "Abhyanga Therapy", "Shirodhara", "Nasya", "Basti", "Virechana", 
            "Swedana", "Pizhichil", "Navarakizhi", "Udvartana", "Takradhara"
        )
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, therapyTypes)
        (binding.actvTherapyType as? AutoCompleteTextView)?.setAdapter(adapter)
    }
    
    private fun setupDatePicker() {
        // Set current date as default
        updateDateField()
        
        // Setup date picker dialog
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateField()
        }
        
        // Show date picker when clicking on the field or icon
        binding.etDate.setOnClickListener {
            showDatePicker(dateSetListener)
        }
        
        binding.tilDate.setEndIconOnClickListener {
            showDatePicker(dateSetListener)
        }
    }
    
    private fun showDatePicker(dateSetListener: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // Set minimum date to today
            datePicker.minDate = System.currentTimeMillis() - 1000
            show()
        }
    }
    
    private fun updateDateField() {
        val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        binding.etDate.setText(dateFormat.format(calendar.time))
    }
    
    private fun setupTimePicker() {
        // Set current time as default
        calendar.set(Calendar.HOUR_OF_DAY, 10) // Default to 10 AM
        calendar.set(Calendar.MINUTE, 0)
        updateTimeField()
        
        // Setup time picker dialog
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateTimeField()
        }
        
        // Show time picker when clicking on the field or icon
        binding.etTime.setOnClickListener {
            showTimePicker(timeSetListener)
        }
        
        binding.tilTime.setEndIconOnClickListener {
            showTimePicker(timeSetListener)
        }
    }
    
    private fun showTimePicker(timeSetListener: TimePickerDialog.OnTimeSetListener) {
        TimePickerDialog(
            this,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false // 12-hour format
        ).show()
    }
    
    private fun updateTimeField() {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        binding.etTime.setText(timeFormat.format(calendar.time))
    }
    
    private fun setupDurationDropdown() {
        val durations = listOf("30 minutes", "45 minutes", "60 minutes", "90 minutes")
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, durations)
        (binding.actvDuration as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.actvDuration as? AutoCompleteTextView)?.setText(durations[2], false) // Default to 60 minutes
    }
    
    private fun setupTherapistDropdown() {
        val therapists = listOf(
            "Dr. Rajesh Verma", "Dr. Priya Patel", "Dr. Amit Singh", 
            "Dr. Meera Gupta", "Dr. Sanjay Sharma"
        )
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, therapists)
        (binding.actvTherapist as? AutoCompleteTextView)?.setAdapter(adapter)
    }
    
    private fun setupBookingButton() {
        binding.btnBookAppointment.setOnClickListener {
            if (validateInputs()) {
                // Create therapy object and save it
                bookTherapy()
            }
        }
    }
    
    private fun validateInputs(): Boolean {
        var isValid = true
        
        // Validate therapy type
        if (binding.actvTherapyType.text.isNullOrEmpty()) {
            binding.tilTherapyType.error = "Please select a therapy type"
            isValid = false
        } else {
            binding.tilTherapyType.error = null
        }
        
        // Validate therapist
        if (binding.actvTherapist.text.isNullOrEmpty()) {
            binding.tilTherapist.error = "Please select a therapist"
            isValid = false
        } else {
            binding.tilTherapist.error = null
        }
        
        // Validate duration
        if (binding.actvDuration.text.isNullOrEmpty()) {
            binding.tilDuration.error = "Please select a duration"
            isValid = false
        } else {
            binding.tilDuration.error = null
        }
        
        return isValid
    }
    
    private fun bookTherapy() {
        // Extract duration value from string (e.g., "60 minutes" -> 60)
        val durationText = binding.actvDuration.text.toString()
        val durationMinutes = durationText.split(" ")[0].toIntOrNull() ?: 60
        
        // Create a new therapy object
        val therapy = Therapy(
            id = UUID.randomUUID().toString(),
            name = binding.actvTherapyType.text.toString(),
            description = "Ayurvedic therapy session",
            dateTime = calendar.time,
            formattedDateTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time),
            durationMinutes = durationMinutes,
            location = "Ayusutra Wellness Center",
            therapistName = binding.actvTherapist.text.toString(),
            status = TherapyStatus.SCHEDULED,
            patientUhid = preferenceManager.getUserUhid() ?: "",
            patientName = preferenceManager.getUserName() ?: ""
        )
        
        // In a real app, you would save this to a database or send to an API
        // For now, just show a success message and finish the activity
        Toast.makeText(
            this,
            "Therapy booked successfully for ${SimpleDateFormat("MMMM d, yyyy 'at' hh:mm a", Locale.getDefault()).format(calendar.time)}",
            Toast.LENGTH_LONG
        ).show()
        
        finish()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}