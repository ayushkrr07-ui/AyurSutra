package com.ayusutra.app.ui.notifications.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ayusutra.app.R
import com.ayusutra.app.data.models.Notification
import com.ayusutra.app.data.models.NotificationActionType
import com.ayusutra.app.data.models.NotificationType
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import java.util.*

class NotificationAdapter(
    private val notifications: List<Notification>,
    private val onNotificationClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int = notifications.size

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivNotificationType: ImageView = itemView.findViewById(R.id.ivNotificationType)
        private val tvNotificationTitle: TextView = itemView.findViewById(R.id.tvNotificationTitle)
        private val tvNotificationMessage: TextView = itemView.findViewById(R.id.tvNotificationMessage)
        private val tvNotificationTime: TextView = itemView.findViewById(R.id.tvNotificationTime)
        private val chipCategory: Chip = itemView.findViewById(R.id.chipCategory)
        private val btnAction: MaterialButton = itemView.findViewById(R.id.btnAction)

        fun bind(notification: Notification) {
            // Set notification icon based on type
            setNotificationIcon(notification.type)
            
            // Set title and message
            tvNotificationTitle.text = notification.title
            tvNotificationMessage.text = notification.message
            
            // Set relative time (e.g., "2 hours ago")
            tvNotificationTime.text = getRelativeTimeSpan(notification.timestamp)
            
            // Set category chip
            chipCategory.text = notification.category.name.lowercase().capitalize(Locale.ROOT)
            
            // Set action button if available
            if (notification.actionType != null && notification.actionType != NotificationActionType.NONE) {
                btnAction.visibility = View.VISIBLE
                btnAction.text = getActionButtonText(notification.actionType)
                btnAction.setOnClickListener { onNotificationClick(notification) }
            } else {
                btnAction.visibility = View.GONE
            }
            
            // Set background color based on read status
            itemView.setBackgroundColor(
                if (notification.isRead) {
                    ContextCompat.getColor(itemView.context, android.R.color.transparent)
                } else {
                    ContextCompat.getColor(itemView.context, R.color.unread_notification_bg)
                }
            )
            
            // Set click listener for the entire item
            itemView.setOnClickListener { onNotificationClick(notification) }
        }

        private fun setNotificationIcon(type: NotificationType) {
            val iconResId = when (type) {
                NotificationType.THERAPY_REMINDER, 
                NotificationType.THERAPY_CONFIRMATION,
                NotificationType.THERAPY_CANCELLATION,
                NotificationType.THERAPY_RESCHEDULED -> R.drawable.ic_therapy
                
                NotificationType.DIETARY_ADVICE -> R.drawable.ic_diet
                
                NotificationType.MEDICATION_REMINDER -> R.drawable.ic_medication
                
                NotificationType.GENERAL_ANNOUNCEMENT,
                NotificationType.SYSTEM_ALERT -> R.drawable.ic_announcement
            }
            
            // Fallback to system icon if custom icon not found
            try {
                ivNotificationType.setImageResource(iconResId)
            } catch (e: Exception) {
                ivNotificationType.setImageResource(android.R.drawable.ic_popup_reminder)
            }
        }

        private fun getRelativeTimeSpan(date: Date): String {
            return DateUtils.getRelativeTimeSpanString(
                date.time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ).toString()
        }

        private fun getActionButtonText(actionType: NotificationActionType): String {
            return when (actionType) {
                NotificationActionType.VIEW_THERAPY -> "View Therapy"
                NotificationActionType.RESCHEDULE_THERAPY -> "Reschedule"
                NotificationActionType.CONFIRM_THERAPY -> "Confirm"
                NotificationActionType.VIEW_DIET_PLAN -> "View Diet Plan"
                NotificationActionType.VIEW_MEDICATION -> "View Medication"
                NotificationActionType.NONE -> ""
            }
        }
    }
}