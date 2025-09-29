package com.ayusutra.app.data.models

import java.util.Date

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Date,
    val type: NotificationType,
    val category: NotificationCategory,
    val isRead: Boolean = false,
    val actionType: NotificationActionType? = null,
    val actionData: String? = null
)

enum class NotificationType {
    THERAPY_REMINDER,
    THERAPY_CONFIRMATION,
    THERAPY_CANCELLATION,
    THERAPY_RESCHEDULED,
    DIETARY_ADVICE,
    MEDICATION_REMINDER,
    GENERAL_ANNOUNCEMENT,
    SYSTEM_ALERT
}

enum class NotificationCategory {
    THERAPY,
    REMINDER,
    SYSTEM
}

enum class NotificationActionType {
    VIEW_THERAPY,
    RESCHEDULE_THERAPY,
    CONFIRM_THERAPY,
    VIEW_DIET_PLAN,
    VIEW_MEDICATION,
    NONE
}