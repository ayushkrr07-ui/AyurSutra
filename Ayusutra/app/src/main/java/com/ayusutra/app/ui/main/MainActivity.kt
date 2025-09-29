package com.ayusutra.app.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayusutra.app.ui.notifications.NotificationsActivity
import com.ayusutra.app.ui.schedule.TherapyScheduleActivity
import com.ayusutra.app.ui.tracking.TherapyTrackingActivity
import com.ayusutra.app.R
import com.ayusutra.app.data.models.FamilyMember
import com.ayusutra.app.data.models.Therapy
import com.ayusutra.app.data.models.TherapyStatus
import com.ayusutra.app.databinding.ActivityMainBinding
import com.ayusutra.app.ui.main.adapters.FamilyMemberAdapter
import com.ayusutra.app.ui.main.adapters.UpcomingTherapyAdapter
import com.ayusutra.app.utils.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var familyMemberAdapter: FamilyMemberAdapter
    private lateinit var upcomingTherapyAdapter: UpcomingTherapyAdapter
    
    private val familyMembers = mutableListOf<FamilyMember>()
    private val upcomingTherapies = mutableListOf<Therapy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        setupToolbar()
        setupUserInfo()
        setupSmartToken()
        setupFamilyMembers()
        setupUpcomingTherapies()
        setupBottomNavigation()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
    }

    private fun setupUserInfo() {
        // Get user information from preferences
        val userName = preferenceManager.getUserName() ?: "User"
        val uhid = preferenceManager.getUserUhid() ?: "AYUR-0000-0000-00"

        // Set welcome message
        binding.tvWelcomeMessage.text = getString(R.string.welcome_user, userName)
        
        // Set UHID in the UHID card
        binding.uhidCard.tvUhidValue.text = uhid
        
        // Set up UHID copy functionality
        binding.uhidCard.btnCopyUhid.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("UHID", uhid)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, R.string.uhid_copied, Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupSmartToken() {
        // For demo purposes, generate a random token
        val tokenNumber = "A-${(100..999).random()}"
        binding.smartToken.tvTokenNumber.text = tokenNumber
        binding.smartToken.tvTokenStatus.text = "Active - Valid for today"
        binding.smartToken.tvEstimatedTime.text = "Estimated waiting time: ${(5..30).random()} minutes"
    }
    
    private fun setupFamilyMembers() {
        // Add sample family members for demo
        familyMembers.clear()
        familyMembers.addAll(getSampleFamilyMembers())
        
        familyMemberAdapter = FamilyMemberAdapter(
            familyMembers,
            onItemClick = { familyMember ->
                // Handle family member click
                Toast.makeText(this, "Selected: ${familyMember.name}", Toast.LENGTH_SHORT).show()
            },
            onManageClick = { familyMember, view ->
                // Show options menu for family member
                Toast.makeText(this, "Manage: ${familyMember.name}", Toast.LENGTH_SHORT).show()
            }
        )
        
        binding.rvFamilyMembers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = familyMemberAdapter
        }
        
        binding.btnAddFamilyMember.setOnClickListener {
            // Navigate to add family member screen
            Toast.makeText(this, "Add family member", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupUpcomingTherapies() {
        // Add sample therapies for demo
        upcomingTherapies.clear()
        upcomingTherapies.addAll(getSampleTherapies())
        
        upcomingTherapyAdapter = UpcomingTherapyAdapter(
            upcomingTherapies,
            onViewDetailsClick = { therapy ->
                // Navigate to therapy details via schedule activity
                val intent = Intent(this, TherapyScheduleActivity::class.java)
                startActivity(intent)
            },
            onRescheduleClick = { therapy ->
                // Navigate to reschedule screen
                val intent = Intent(this, TherapyScheduleActivity::class.java)
                startActivity(intent)
            }
        )
        
        binding.rvUpcomingTherapies.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = upcomingTherapyAdapter
        }
        
        binding.btnBookNewTherapy.setOnClickListener {
            // Navigate to therapy booking screen
            val intent = Intent(this, TherapyScheduleActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Already on home
                    true
                }
                R.id.nav_schedule -> {
                    // Navigate to schedule
                    val intent = Intent(this, TherapyScheduleActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_notifications -> {
                    // Navigate to notifications
                    val intent = Intent(this, NotificationsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // Navigate to profile
                    val intent = Intent(this, TherapyTrackingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
    
    private fun getSampleFamilyMembers(): List<FamilyMember> {
        return listOf(
            FamilyMember("1", "Ananya Sharma", "Spouse", "AYUR-1234-5678-90"),
            FamilyMember("2", "Arjun Sharma", "Son", "AYUR-2345-6789-01"),
            FamilyMember("3", "Meera Sharma", "Daughter", "AYUR-3456-7890-12")
        )
    }
    
    private fun getSampleTherapies(): List<Therapy> {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        
        // Today
        val today = calendar.time
        val todayFormatted = "Today, ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(today)}"
        
        // Tomorrow
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val tomorrow = calendar.time
        val tomorrowFormatted = "Tomorrow, ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(tomorrow)}"
        
        // Day after tomorrow
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val dayAfter = calendar.time
        val dayAfterFormatted = SimpleDateFormat("EEE, MMM d, hh:mm a", Locale.getDefault()).format(dayAfter)
        
        return listOf(
            Therapy(
                "1",
                "Abhyanga Therapy",
                "Full body oil massage",
                today,
                todayFormatted,
                60,
                "Ayusutra Wellness Center, Room 101",
                "Dr. Rajesh Verma",
                TherapyStatus.SCHEDULED,
                preferenceManager.getUserUhid() ?: "",
                preferenceManager.getUserName() ?: ""
            ),
            Therapy(
                "2",
                "Shirodhara",
                "Continuous oil flow on forehead",
                tomorrow,
                tomorrowFormatted,
                45,
                "Ayusutra Wellness Center, Room 203",
                "Dr. Priya Patel",
                TherapyStatus.SCHEDULED,
                preferenceManager.getUserUhid() ?: "",
                preferenceManager.getUserName() ?: ""
            ),
            Therapy(
                "3",
                "Nasya",
                "Nasal administration of herbal oils",
                dayAfter,
                dayAfterFormatted,
                30,
                "Ayusutra Wellness Center, Room 105",
                "Dr. Amit Singh",
                TherapyStatus.SCHEDULED,
                preferenceManager.getUserUhid() ?: "",
                preferenceManager.getUserName() ?: ""
            )
        )
    }
}