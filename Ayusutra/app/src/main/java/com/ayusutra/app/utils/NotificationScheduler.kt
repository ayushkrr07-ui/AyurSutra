package com.ayusutra.app.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.ayusutra.app.data.models.NotificationCategory
import com.ayusutra.app.data.models.NotificationType
import com.ayusutra.app.receivers.NotificationReceiver
import java.util.*

/**
 * Utility class to schedule notifications using AlarmManager
 */
class NotificationScheduler {

    companion object {
        
        /**
         * Schedule a therapy reminder notification
         */
        fun scheduleTherapyReminder(
            context: Context,
            therapyId: String,
            therapyName: String,
            therapyTime: String,
            reminderTimeMillis: Long
        ) {
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                action = NotificationReceiver.ACTION_THERAPY_REMINDER
                putExtra(NotificationReceiver.EXTRA_THERAPY_ID, therapyId)
                putExtra(NotificationReceiver.EXTRA_THERAPY_NAME, therapyName)
                putExtra(NotificationReceiver.EXTRA_THERAPY_TIME, therapyTime)
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                therapyId.hashCode(),
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )
            
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent)
            }
        }
        
        /**
         * Schedule a medication reminder notification
         */
        fun scheduleMedicationReminder(
            context: Context,
            medicationId: String,
            medicationName: String,
            instructions: String,
            reminderTimeMillis: Long
        ) {
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                action = NotificationReceiver.ACTION_MEDICATION_REMINDER
                putExtra(NotificationReceiver.EXTRA_MEDICATION_NAME, medicationName)
                putExtra(NotificationReceiver.EXTRA_INSTRUCTIONS, instructions)
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                medicationId.hashCode(),
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )
            
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent)
            }
        }
        
        /**
         * Cancel a scheduled therapy reminder
         */
        fun cancelTherapyReminder(context: Context, therapyId: String) {
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                action = NotificationReceiver.ACTION_THERAPY_REMINDER
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                therapyId.hashCode(),
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
                } else {
                    PendingIntent.FLAG_NO_CREATE
                }
            )
            
            if (pendingIntent != null) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
        
        /**
         * Cancel a scheduled medication reminder
         */
        fun cancelMedicationReminder(context: Context, medicationId: String) {
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                action = NotificationReceiver.ACTION_MEDICATION_REMINDER
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                medicationId.hashCode(),
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
                } else {
                    PendingIntent.FLAG_NO_CREATE
                }
            )
            
            if (pendingIntent != null) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }
}