package com.ayusutra.app.ui.notifications

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayusutra.app.data.models.Notification
import com.ayusutra.app.data.models.NotificationCategory
import com.ayusutra.app.data.models.NotificationType
import com.ayusutra.app.databinding.ActivityNotificationsBinding
import com.ayusutra.app.ui.notifications.adapters.NotificationAdapter
import com.google.android.material.tabs.TabLayout
import java.util.*

class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var notificationAdapter: NotificationAdapter
    private val allNotifications = mutableListOf<Notification>()
    private val filteredNotifications = mutableListOf<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupTabLayout()
        setupRecyclerView()
        loadNotifications()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                filterNotifications(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter(
            filteredNotifications,
            onNotificationClick = { notification ->
                markNotificationAsRead(notification)
                handleNotificationAction(notification)
            }
        )

        binding.rvNotifications.apply {
            layoutManager = LinearLayoutManager(this@NotificationsActivity)
            adapter = notificationAdapter
        }
    }

    private fun loadNotifications() {
        // In a real app, this would load from a database or API
        // For now, we'll create some sample notifications
        allNotifications.clear()
        allNotifications.addAll(createSampleNotifications())
        
        // Initial filter (All notifications)
        filterNotifications(0)
    }

    private fun filterNotifications(tabPosition: Int) {
        filteredNotifications.clear()
        
        when (tabPosition) {
            0 -> filteredNotifications.addAll(allNotifications) // All
            1 -> filteredNotifications.addAll(allNotifications.filter { it.category == NotificationCategory.THERAPY }) // Therapy
            2 -> filteredNotifications.addAll(allNotifications.filter { it.category == NotificationCategory.REMINDER }) // Reminders
        }
        
        updateNotificationsList()
    }

    private fun updateNotificationsList() {
        if (filteredNotifications.isEmpty()) {
            binding.rvNotifications.visibility = View.GONE
            binding.tvNoNotifications.visibility = View.VISIBLE
        } else {
            binding.rvNotifications.visibility = View.VISIBLE
            binding.tvNoNotifications.visibility = View.GONE
            notificationAdapter.notifyDataSetChanged()
        }
    }

    private fun markNotificationAsRead(notification: Notification) {
        // In a real app, this would update the database
        val index = allNotifications.indexOfFirst { it.id == notification.id }
        if (index != -1) {
            val updatedNotification = notification.copy(isRead = true)
            allNotifications[index] = updatedNotification
            
            val filteredIndex = filteredNotifications.indexOfFirst { it.id == notification.id }
            if (filteredIndex != -1) {
                filteredNotifications[filteredIndex] = updatedNotification
                notificationAdapter.notifyItemChanged(filteredIndex)
            }
        }
    }

    private fun handleNotificationAction(notification: Notification) {
        // In a real app, this would navigate to the appropriate screen based on the action type
        // For now, we'll just print a log message
        println("Handling action for notification: ${notification.id}, action: ${notification.actionType}")
    }

    private fun createSampleNotifications(): List<Notification> {
        val calendar = Calendar.getInstance()
        val now = calendar.time
        
        // Create a list of sample notifications
        return listOf(
            Notification(
                id = "1",
                title = "Abhyanga Therapy Reminder",
                message = "Your Abhyanga therapy is scheduled for tomorrow at 10:00 AM. Please arrive 15 minutes early.",
                timestamp = Date(now.time - 2 * 60 * 60 * 1000), // 2 hours ago
                type = NotificationType.THERAPY_REMINDER,
                category = NotificationCategory.THERAPY,
                actionType = NotificationActionType.VIEW_THERAPY,
                actionData = "therapy_id_123"
            ),
            Notification(
                id = "2",
                title = "Dietary Recommendation",
                message = "Based on your dosha profile, we recommend avoiding cold foods and drinks for the next 3 days.",
                timestamp = Date(now.time - 1 * 24 * 60 * 60 * 1000), // 1 day ago
                type = NotificationType.DIETARY_ADVICE,
                category = NotificationCategory.REMINDER
            ),
            Notification(
                id = "3",
                title = "Therapy Rescheduled",
                message = "Your Shirodhara therapy has been rescheduled to Friday, 3:00 PM due to therapist availability.",
                timestamp = Date(now.time - 3 * 24 * 60 * 60 * 1000), // 3 days ago
                type = NotificationType.THERAPY_RESCHEDULED,
                category = NotificationCategory.THERAPY,
                actionType = NotificationActionType.VIEW_THERAPY,
                actionData = "therapy_id_456"
            ),
            Notification(
                id = "4",
                title = "Medication Reminder",
                message = "Remember to take your Triphala supplement before bedtime.",
                timestamp = Date(now.time - 12 * 60 * 60 * 1000), // 12 hours ago
                type = NotificationType.MEDICATION_REMINDER,
                category = NotificationCategory.REMINDER
            ),
            Notification(
                id = "5",
                title = "New Wellness Program",
                message = "We've launched a new Panchakarma wellness program. Check it out and book your consultation.",
                timestamp = Date(now.time - 5 * 24 * 60 * 60 * 1000), // 5 days ago
                type = NotificationType.GENERAL_ANNOUNCEMENT,
                category = NotificationCategory.SYSTEM
            )
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