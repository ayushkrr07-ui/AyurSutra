package com.ayusutra.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ayusutra.app.data.models.NotificationCategory
import com.ayusutra.app.data.models.NotificationType
import com.ayusutra.app.services.NotificationService

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                // Initialize notification channels on device boot
                NotificationService.createNotificationChannels(context)
                
                // Reschedule any pending notifications
                rescheduleNotifications(context)
            }
            
            ACTION_THERAPY_REMINDER -> {
                // Handle therapy reminder notification
                val therapyId = intent.getStringExtra(EXTRA_THERAPY_ID) ?: return
                val therapyName = intent.getStringExtra(EXTRA_THERAPY_NAME) ?: "Therapy"
                val therapyTime = intent.getStringExtra(EXTRA_THERAPY_TIME) ?: "scheduled time"
                
                sendTherapyReminderNotification(context, therapyId, therapyName, therapyTime)
            }
            
            ACTION_MEDICATION_REMINDER -> {
                // Handle medication reminder notification
                val medicationName = intent.getStringExtra(EXTRA_MEDICATION_NAME) ?: "medication"
                val instructions = intent.getStringExtra(EXTRA_INSTRUCTIONS) ?: ""
                
                sendMedicationReminderNotification(context, medicationName, instructions)
            }
        }
    }
    
    private fun rescheduleNotifications(context: Context) {
        // In a real app, this would retrieve scheduled notifications from a database
        // and reschedule them using AlarmManager
        // For this demo, we'll just show a system notification
        NotificationService.sendNotification(
            context,
            NotificationService.getNextNotificationId(NotificationCategory.SYSTEM),
            "AyurSutra Restarted",
            "Your notifications have been restored.",
            NotificationType.SYSTEM_ALERT,
            NotificationCategory.SYSTEM
        )
    }
    
    private fun sendTherapyReminderNotification(context: Context, therapyId: String, therapyName: String, therapyTime: String) {
        // Create an intent for when the user taps the notification
        val actionIntent = Intent(context, Class.forName("com.ayusutra.app.ui.schedule.TherapyScheduleActivity"))
        actionIntent.putExtra("therapy_id", therapyId)
        
        NotificationService.sendNotification(
            context,
            NotificationService.getNextNotificationId(NotificationCategory.THERAPY),
            "Upcoming Therapy Reminder",
            "Your $therapyName therapy is scheduled for $therapyTime. Please arrive 15 minutes early.",
            NotificationType.THERAPY_REMINDER,
            NotificationCategory.THERAPY,
            actionIntent
        )
    }
    
    private fun sendMedicationReminderNotification(context: Context, medicationName: String, instructions: String) {
        val message = if (instructions.isNotEmpty()) {
            "Time to take your $medicationName. $instructions"
        } else {
            "Time to take your $medicationName."
        }
        
        NotificationService.sendNotification(
            context,
            NotificationService.getNextNotificationId(NotificationCategory.REMINDER),
            "Medication Reminder",
            message,
            NotificationType.MEDICATION_REMINDER,
            NotificationCategory.REMINDER
        )
    }
    
    companion object {
        // Action constants
        const val ACTION_THERAPY_REMINDER = "com.ayusutra.app.ACTION_THERAPY_REMINDER"
        const val ACTION_MEDICATION_REMINDER = "com.ayusutra.app.ACTION_MEDICATION_REMINDER"
        
        // Extra constants
        const val EXTRA_THERAPY_ID = "therapy_id"
        const val EXTRA_THERAPY_NAME = "therapy_name"
        const val EXTRA_THERAPY_TIME = "therapy_time"
        const val EXTRA_MEDICATION_NAME = "medication_name"
        const val EXTRA_INSTRUCTIONS = "instructions"
    }
}