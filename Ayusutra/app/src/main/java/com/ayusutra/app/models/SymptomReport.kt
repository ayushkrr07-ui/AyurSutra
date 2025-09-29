package com.ayusutra.app.models

import java.util.Date

data class SymptomReport(
    val id: String,
    val date: Date,
    val painLevel: Int, // 1-10 scale (10 being worst pain)
    val mobilityLevel: Int, // 1-10 scale (10 being full mobility)
    val sleepQuality: Int, // 1-10 scale (10 being excellent sleep)
    val stressLevel: Int, // 1-10 scale (10 being highest stress)
    val notes: String,
    val userId: String
)