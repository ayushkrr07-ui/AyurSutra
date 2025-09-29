package com.ayusutra.app.ui.booking

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ayusutra.app.R
import com.ayusutra.app.data.models.NotificationCategory
import com.ayusutra.app.data.models.NotificationType
import com.ayusutra.app.data.models.Therapy
import com.ayusutra.app.data.models.TherapyStatus
import com.ayusutra.app.databinding.ActivityTherapyBookingBinding
import com.ayusutra.app.services.NotificationService
import com.ayusutra.app.utils.NotificationScheduler
import com.ayusutra.app.utils.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*

class TherapyBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTherapyBookingBinding
    private lateinit var preferenceManager: PreferenceManager
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTherapyBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        preferenceManager = PreferenceManager(this)
        
        setupToolbar()
        setupDatePicker()
        setupTimePicker()
        setupTherapyTypeSpinner()
        setupBookButton()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        title = "Book New Therapy"
    }
    
    private fun setupDatePicker() {
        // Set minimum date to today
        binding.datePicker.minDate = calendar.timeInMillis
        
        // Set maximum date to 3 months from now
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.MONTH, 3)
        binding.datePicker.maxDate = maxDate.timeInMillis
    }
    
    private fun setupTimePicker() {
        // Set time picker to 24-hour view
        binding.timePicker.setIs24HourView(true)
        
        // Set default time to 9:00 AM
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            binding.timePicker.hour = 9
            binding.timePicker.minute = 0
        } else {
            binding.timePicker.currentHour = 9
            binding.timePicker.currentMinute = 0
        }
    }
    
    private fun setupTherapyTypeSpinner() {
        // Populate spinner with therapy types
        val therapyTypes = arrayOf(
            "Abhyanga (Full Body Massage)",
            "Shirodhara (Oil Flow Therapy)",
            "Nasya (Nasal Treatment)",
            "Basti (Enema Therapy)",
            "Virechana (Purgation Therapy)",
            "Vamana (Emesis Therapy)",
            "Raktamokshana (Bloodletting)",
            "Netra Tarpana (Eye Treatment)"
        )
        
        binding.spinnerTherapyType.adapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            therapyTypes
        )
    }
    
    private fun setupBookButton() {
        binding.btnBookTherapy.setOnClickListener {
            // Get selected date
            val year = binding.datePicker.year
            val month = binding.datePicker.month
            val day = binding.datePicker.dayOfMonth
            
            // Get selected time
            val hour = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                binding.timePicker.hour
            } else {
                binding.timePicker.currentHour
            }
            
            val minute = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                binding.timePicker.minute
            } else {
                binding.timePicker.currentMinute
            }
            
            // Create calendar for selected date and time
            val selectedDateTime = Calendar.getInstance()
            selectedDateTime.set(year, month, day, hour, minute)
            
            // Check if selected time is in the past
            if (selectedDateTime.timeInMillis < System.currentTimeMillis()) {
                Toast.makeText(this, "Please select a future date and time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Check if selected time is within working hours (8 AM to 6 PM)
            if (hour < 8 || hour >= 18) {
                Toast.makeText(this, "Please select a time between 8 AM and 6 PM", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Get selected therapy type
            val therapyType = binding.spinnerTherapyType.selectedItem.toString()
            
            // Get therapy description based on type
            val therapyDescription = getTherapyDescription(therapyType)
            
            // Get therapy duration based on type
            val therapyDuration = getTherapyDuration(therapyType)
            
            // Create therapy object
            val therapy = Therapy(
                UUID.randomUUID().toString(),
                therapyType,
                therapyDescription,
                selectedDateTime.time,
                displayDateFormat.format(selectedDateTime.time),
                therapyDuration,
                "Ayusutra Wellness Center",
                "Dr. Ayush Sharma",
                TherapyStatus.SCHEDULED,
                preferenceManager.getUserUhid() ?: "",
                preferenceManager.getUserName() ?: ""
            )
            
            // In a real app, save therapy to database
            // For demo, just show success message
            Toast.makeText(this, "Therapy booked successfully!", Toast.LENGTH_SHORT).show()
            
            // Schedule notification for 24 hours before therapy
            scheduleTherapyNotification(therapy)
            
            // Send immediate confirmation notification
            sendConfirmationNotification(therapy)
            
            // Return to previous screen
            finish()
        }
    }
    
    private fun getTherapyDescription(therapyType: String): String {
        return when (therapyType) {
            "Abhyanga (Full Body Massage)" -> "Traditional Ayurvedic oil massage for relaxation and rejuvenation"
            "Shirodhara (Oil Flow Therapy)" -> "Continuous oil flow on forehead for mental relaxation"
            "Nasya (Nasal Treatment)" -> "Nasal administration of herbal oils for sinus and respiratory health"
            "Basti (Enema Therapy)" -> "Medicated enema for intestinal cleansing and detoxification"
            "Virechana (Purgation Therapy)" -> "Therapeutic purgation for detoxification"
            "Vamana (Emesis Therapy)" -> "Therapeutic emesis for respiratory conditions"
            "Raktamokshana (Bloodletting)" -> "Controlled bloodletting for skin disorders and inflammation"
            "Netra Tarpana (Eye Treatment)" -> "Eye treatment with medicated ghee for eye health"
            else -> "Ayurvedic therapy for holistic wellness"
        }
    }
    
    private fun getTherapyDuration(therapyType: String): Int {
        return when (therapyType) {
            "Abhyanga (Full Body Massage)" -> 60
            "Shirodhara (Oil Flow Therapy)" -> 45
            "Nasya (Nasal Treatment)" -> 30
            "Basti (Enema Therapy)" -> 45
            "Virechana (Purgation Therapy)" -> 90
            "Vamana (Emesis Therapy)" -> 90
            "Raktamokshana (Bloodletting)" -> 30
            "Netra Tarpana (Eye Treatment)" -> 30
            else -> 60
        }
    }
    
    private fun scheduleTherapyNotification(therapy: Therapy) {
        // Calculate time for notification (24 hours before therapy)
        val notificationTime = Calendar.getInstance()
        notificationTime.time = therapy.date
        notificationTime.add(Calendar.DAY_OF_MONTH, -1)
        
        // Schedule notification
        NotificationScheduler.scheduleTherapyReminder(
            this,
            therapy.id,
            therapy.name,
            therapy.dateFormatted,
            notificationTime.timeInMillis
        )
    }
    
    private fun sendConfirmationNotification(therapy: Therapy) {
        // Send immediate confirmation notification
        NotificationService.sendNotification(
            this,
            NotificationService.getNextNotificationId(NotificationCategory.THERAPY),
            "Therapy Booking Confirmed",
            "Your ${therapy.name} therapy has been scheduled for ${therapy.dateFormatted}.",
            NotificationType.THERAPY_CONFIRMATION,
            NotificationCategory.THERAPY
        )
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}