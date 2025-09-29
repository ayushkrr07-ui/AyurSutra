package com.ayusutra.app.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ayusutra.app.R
import com.ayusutra.app.ui.main.MainActivity
import com.ayusutra.app.ui.onboarding.OnboardingActivity
import com.ayusutra.app.utils.PreferenceManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferenceManager
    private val SPLASH_DELAY = 2000L // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        preferenceManager = PreferenceManager(this)

        // Delay for splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, SPLASH_DELAY)
    }

    private fun navigateToNextScreen() {
        // Check if user has completed onboarding
        if (preferenceManager.isOnboardingCompleted()) {
            // Check if user is registered
            if (preferenceManager.isUserRegistered()) {
                // Navigate to main activity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Navigate to registration
                startActivity(Intent(this, OnboardingActivity::class.java))
            }
        } else {
            // Navigate to onboarding
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
        finish() // Close splash activity
    }
}