package com.ayusutra.app.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val uhid: String,
    val age: Int,
    val gender: String,
    val address: String
)