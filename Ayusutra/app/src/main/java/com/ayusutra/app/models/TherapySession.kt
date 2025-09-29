package com.ayusutra.app.models

import java.util.Date

data class TherapySession(
    val id: String,
    val therapyName: String,
    val date: Date,
    val practitioner: String,
    val effectivenessRating: Int, // 1-10 scale
    val comfortRating: Int, // 1-10 scale
    val symptomChange: String,
    val notes: String,
    val userId: String
)