package com.ayusutra.app.utils

import java.util.*

/**
 * Utility class to generate Unique Health ID (UHID) for patients
 */
object UhidGenerator {

    private const val PREFIX = "AYUR"
    private const val SEPARATOR = "-"
    
    /**
     * Generates a unique health ID in the format AYUR-XXXX-XXXX-XX
     * where X is an alphanumeric character
     */
    fun generateUhid(): String {
        val random = Random()
        
        // Generate three groups of random alphanumeric characters
        val firstGroup = generateRandomString(random, 4)
        val secondGroup = generateRandomString(random, 4)
        val thirdGroup = generateRandomString(random, 2)
        
        // Combine all parts with separators
        return "$PREFIX$SEPARATOR$firstGroup$SEPARATOR$secondGroup$SEPARATOR$thirdGroup"
    }
    
    /**
     * Generates a random alphanumeric string of specified length
     */
    private fun generateRandomString(random: Random, length: Int): String {
        val allowedChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        return (1..length)
            .map { allowedChars[random.nextInt(allowedChars.length)] }
            .joinToString("")
    }
    
    /**
     * Validates if the given string is a valid UHID format
     */
    fun isValidUhid(uhid: String): Boolean {
        val pattern = "^$PREFIX$SEPARATOR[0-9A-Z]{4}$SEPARATOR[0-9A-Z]{4}$SEPARATOR[0-9A-Z]{2}$"
        return uhid.matches(pattern.toRegex())
    }
}