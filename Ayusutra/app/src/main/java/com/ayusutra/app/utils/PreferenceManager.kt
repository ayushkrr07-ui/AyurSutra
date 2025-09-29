package com.ayusutra.app.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Utility class to manage SharedPreferences for the app
 */
class PreferenceManager(context: Context) {

    companion object {
        private const val PREF_NAME = "AyurSutraPrefs"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_USER_REGISTERED = "user_registered"
        private const val KEY_USER_UHID = "user_uhid"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_MOBILE = "user_mobile"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * Check if onboarding is completed
     */
    fun isOnboardingCompleted(): Boolean {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    /**
     * Set onboarding completed status
     */
    fun setOnboardingCompleted(completed: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }

    /**
     * Check if user is registered
     */
    fun isUserRegistered(): Boolean {
        return sharedPreferences.getBoolean(KEY_USER_REGISTERED, false)
    }

    /**
     * Set user registration status
     */
    fun setUserRegistered(registered: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_USER_REGISTERED, registered).apply()
    }

    /**
     * Save user UHID
     */
    fun saveUserUhid(uhid: String) {
        sharedPreferences.edit().putString(KEY_USER_UHID, uhid).apply()
    }

    /**
     * Get user UHID
     */
    fun getUserUhid(): String {
        return sharedPreferences.getString(KEY_USER_UHID, "") ?: ""
    }

    /**
     * Save user profile information
     */
    fun saveUserProfile(name: String, email: String, mobile: String) {
        sharedPreferences.edit()
            .putString(KEY_USER_NAME, name)
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_USER_MOBILE, mobile)
            .apply()
    }

    /**
     * Get user name
     */
    fun getUserName(): String {
        return sharedPreferences.getString(KEY_USER_NAME, "") ?: ""
    }

    /**
     * Get user email
     */
    fun getUserEmail(): String {
        return sharedPreferences.getString(KEY_USER_EMAIL, "") ?: ""
    }

    /**
     * Get user mobile
     */
    fun getUserMobile(): String {
        return sharedPreferences.getString(KEY_USER_MOBILE, "") ?: ""
    }

    /**
     * Clear all preferences (for logout)
     */
    fun clearAllPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}