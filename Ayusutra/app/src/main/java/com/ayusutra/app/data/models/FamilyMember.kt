package com.ayusutra.app.data.models

data class FamilyMember(
    val id: String,
    val name: String,
    val relation: String,
    val uhid: String? = null,
    val gender: String? = null,
    val dateOfBirth: String? = null,
    val contactNumber: String? = null
)