package com.ayusutra.app.ui.schedule

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayusutra.app.R
import com.ayusutra.app.data.models.Therapy
import com.ayusutra.app.data.models.TherapyStatus
import com.ayusutra.app.ui.schedule.TherapyBookingActivity
import com.ayusutra.app.databinding.ActivityTherapyScheduleBinding
import com.ayusutra.app.ui.schedule.adapters.TherapyScheduleAdapter
import com.ayusutra.app.utils.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*

class TherapyScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTherapyScheduleBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var therapyAdapter: TherapyScheduleAdapter
    
    private val therapiesMap = mutableMapOf<String, List<Therapy>>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
    private val calendar = Calendar.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTherapyScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        preferenceManager = PreferenceManager(this)
        
        setupToolbar()
        setupCalendar()
        setupTherapiesForDate(calendar.time)
        setupFab()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    
    private fun setupCalendar() {
        updateMonthYearTitle()
        
        binding.btnPreviousMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateMonthYearTitle()
        }
        
        binding.btnNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateMonthYearTitle()
        }
        
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            setupTherapiesForDate(calendar.time)
        }
        
        // Generate sample therapies for the current month
        generateSampleTherapies()
    }
    
    private fun updateMonthYearTitle() {
        val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.tvMonthYear.text = monthYearFormat.format(calendar.time)
    }
    
    private fun setupTherapiesForDate(date: Date) {
        val dateKey = dateFormat.format(date)
        binding.tvSelectedDate.text = displayDateFormat.format(date)
        
        val therapiesForDate = therapiesMap[dateKey] ?: emptyList()
        
        if (therapiesForDate.isEmpty()) {
            binding.rvTherapiesForDate.visibility = View.GONE
            binding.tvNoTherapies.visibility = View.VISIBLE
        } else {
            binding.rvTherapiesForDate.visibility = View.VISIBLE
            binding.tvNoTherapies.visibility = View.GONE
            
            therapyAdapter = TherapyScheduleAdapter(
                therapiesForDate,
                onTherapyClick = { therapy ->
                    // Navigate to therapy details
                    Toast.makeText(this, "Selected therapy: ${therapy.name}", Toast.LENGTH_SHORT).show()
                }
            )
            
            binding.rvTherapiesForDate.apply {
                layoutManager = LinearLayoutManager(this@TherapyScheduleActivity)
                adapter = therapyAdapter
            }
        }
    }
    
    private fun setupFab() {
        binding.fabBookTherapy.setOnClickListener {
            // Navigate to therapy booking screen
            val intent = Intent(this, TherapyBookingActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun generateSampleTherapies() {
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        
        // Create a new calendar instance for generating dates
        val tempCalendar = Calendar.getInstance()
        tempCalendar.set(Calendar.YEAR, currentYear)
        tempCalendar.set(Calendar.MONTH, currentMonth)
        
        // Generate therapies for random days in the current month
        val therapyTypes = listOf(
            "Abhyanga Therapy", "Shirodhara", "Nasya", "Basti", "Virechana", 
            "Swedana", "Pizhichil", "Navarakizhi", "Udvartana", "Takradhara"
        )
        
        val locations = listOf(
            "Ayusutra Wellness Center, Room 101",
            "Ayusutra Wellness Center, Room 102",
            "Ayusutra Wellness Center, Room 203",
            "Ayusutra Wellness Center, Room 105"
        )
        
        val therapists = listOf(
            "Dr. Rajesh Verma", "Dr. Priya Patel", "Dr. Amit Singh", 
            "Dr. Meera Gupta", "Dr. Sanjay Sharma"
        )
        
        // Generate 10 random therapies for the month
        for (i in 1..10) {
            // Random day in the current month
            val randomDay = (1..28).random() // Avoiding edge cases with month lengths
            tempCalendar.set(Calendar.DAY_OF_MONTH, randomDay)
            
            // Random hour between 9 AM and 5 PM
            val randomHour = (9..17).random()
            tempCalendar.set(Calendar.HOUR_OF_DAY, randomHour)
            tempCalendar.set(Calendar.MINUTE, 0)
            
            val therapyDate = tempCalendar.time
            val dateKey = dateFormat.format(therapyDate)
            
            // Format the time for display
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val formattedTime = timeFormat.format(therapyDate)
            
            // Create a therapy for this date and time
            val therapy = Therapy(
                id = UUID.randomUUID().toString(),
                name = therapyTypes.random(),
                description = "Ayurvedic therapy session",
                dateTime = therapyDate,
                formattedDateTime = formattedTime,
                durationMinutes = listOf(30, 45, 60, 90).random(),
                location = locations.random(),
                therapistName = therapists.random(),
                status = TherapyStatus.SCHEDULED,
                patientUhid = preferenceManager.getUserUhid() ?: "",
                patientName = preferenceManager.getUserName() ?: ""
            )
            
            // Add to the map
            val existingTherapies = therapiesMap[dateKey] ?: emptyList()
            therapiesMap[dateKey] = existingTherapies + therapy
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}