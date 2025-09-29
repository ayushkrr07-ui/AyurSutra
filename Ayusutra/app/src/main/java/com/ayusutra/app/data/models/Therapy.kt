package com.ayusutra.app.data.models

import java.util.Date

data class Therapy(
    val id: String,
    val name: String,
    val description: String,
    val dateTime: Date,
    val formattedDateTime: String,
    val durationMinutes: Int,
    val location: String,
    val therapistName: String? = null,
    val status: TherapyStatus = TherapyStatus.SCHEDULED,
    val patientUhid: String,
    val patientName: String
)

enum class TherapyStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED,
    RESCHEDULED
}