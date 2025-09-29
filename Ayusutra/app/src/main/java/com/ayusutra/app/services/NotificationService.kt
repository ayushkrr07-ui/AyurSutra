package com.ayusutra.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ayusutra.app.R
import com.ayusutra.app.data.models.NotificationCategory
import com.ayusutra.app.data.models.NotificationType
import com.ayusutra.app.ui.notifications.NotificationsActivity

class NotificationService {

    companion object {
        private const val CHANNEL_ID_THERAPY = "ayusutra_therapy_channel"
        private const val CHANNEL_ID_REMINDERS = "ayusutra_reminders_channel"
        private const val CHANNEL_ID_SYSTEM = "ayusutra_system_channel"
        
        private const val NOTIFICATION_GROUP_THERAPY = "ayusutra_therapy_group"
        private const val NOTIFICATION_GROUP_REMINDERS = "ayusutra_reminders_group"
        private const val NOTIFICATION_GROUP_SYSTEM = "ayusutra_system_group"
        
        // Notification IDs start from these base values
        private const val THERAPY_NOTIFICATION_ID_BASE = 1000
        private const val REMINDER_NOTIFICATION_ID_BASE = 2000
        private const val SYSTEM_NOTIFICATION_ID_BASE = 3000
        
        // Initialize notification channels
        fun createNotificationChannels(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                
                // Therapy channel (high importance)
                val therapyChannel = NotificationChannel(
                    CHANNEL_ID_THERAPY,
                    "Therapy Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications related to therapy appointments"
                    enableLights(true)
                    lightColor = Color.BLUE
                    enableVibration(true)
                    setShowBadge(true)
                }
                
                // Reminders channel (default importance)
                val remindersChannel = NotificationChannel(
                    CHANNEL_ID_REMINDERS,
                    "Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Medication and dietary reminders"
                    enableLights(true)
                    lightColor = Color.YELLOW
                    enableVibration(true)
                    setShowBadge(true)
                }
                
                // System channel (low importance)
                val systemChannel = NotificationChannel(
                    CHANNEL_ID_SYSTEM,
                    "System Notifications",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "General system notifications"
                    setShowBadge(false)
                }
                
                // Register all channels
                notificationManager.createNotificationChannels(listOf(therapyChannel, remindersChannel, systemChannel))
            }
        }
        
        // Send a notification
        fun sendNotification(
            context: Context,
            notificationId: Int,
            title: String,
            message: String,
            type: NotificationType,
            category: NotificationCategory,
            actionIntent: Intent? = null
        ) {
            val channelId = getChannelId(category)
            val groupId = getGroupId(category)
            
            // Create intent for notification tap action
            val intent = actionIntent ?: Intent(context, NotificationsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )
            
            // Build the notification
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(getNotificationIcon(type))
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(getPriority(category))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(groupId)
                
            // Send the notification
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        }
        
        // Helper methods
        private fun getChannelId(category: NotificationCategory): String {
            return when (category) {
                NotificationCategory.THERAPY -> CHANNEL_ID_THERAPY
                NotificationCategory.REMINDER -> CHANNEL_ID_REMINDERS
                NotificationCategory.SYSTEM -> CHANNEL_ID_SYSTEM
            }
        }
        
        private fun getGroupId(category: NotificationCategory): String {
            return when (category) {
                NotificationCategory.THERAPY -> NOTIFICATION_GROUP_THERAPY
                NotificationCategory.REMINDER -> NOTIFICATION_GROUP_REMINDERS
                NotificationCategory.SYSTEM -> NOTIFICATION_GROUP_SYSTEM
            }
        }
        
        private fun getPriority(category: NotificationCategory): Int {
            return when (category) {
                NotificationCategory.THERAPY -> NotificationCompat.PRIORITY_HIGH
                NotificationCategory.REMINDER -> NotificationCompat.PRIORITY_DEFAULT
                NotificationCategory.SYSTEM -> NotificationCompat.PRIORITY_LOW
            }
        }
        
        private fun getNotificationIcon(type: NotificationType): Int {
            return when (type) {
                NotificationType.THERAPY_REMINDER,
                NotificationType.THERAPY_CONFIRMATION,
                NotificationType.THERAPY_CANCELLATION,
                NotificationType.THERAPY_RESCHEDULED -> R.drawable.ic_therapy
                
                NotificationType.DIETARY_ADVICE -> R.drawable.ic_diet
                
                NotificationType.MEDICATION_REMINDER -> R.drawable.ic_medication
                
                NotificationType.GENERAL_ANNOUNCEMENT,
                NotificationType.SYSTEM_ALERT -> R.drawable.ic_announcement
            }
        }
        
        // Get next notification ID for a category
        fun getNextNotificationId(category: NotificationCategory): Int {
            return when (category) {
                NotificationCategory.THERAPY -> THERAPY_NOTIFICATION_ID_BASE + (Math.random() * 1000).toInt()
                NotificationCategory.REMINDER -> REMINDER_NOTIFICATION_ID_BASE + (Math.random() * 1000).toInt()
                NotificationCategory.SYSTEM -> SYSTEM_NOTIFICATION_ID_BASE + (Math.random() * 1000).toInt()
            }
        }
    }
}