package com.ayusutra.app.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.ayusutra.app.R
import com.ayusutra.app.databinding.ActivityOnboardingBinding
import com.ayusutra.app.ui.registration.RegistrationActivity
import com.ayusutra.app.utils.PreferenceManager
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        setupOnboardingPages()
        setupViewPager()
        setupButtons()
    }

    private fun setupOnboardingPages() {
        val onboardingItems = listOf(
            OnboardingItem(
                R.drawable.ic_launcher_foreground,
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_desc_1)
            ),
            OnboardingItem(
                R.drawable.ic_launcher_foreground,
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_desc_2)
            ),
            OnboardingItem(
                R.drawable.ic_launcher_foreground,
                getString(R.string.onboarding_title_3),
                getString(R.string.onboarding_desc_3)
            )
        )

        onboardingAdapter = OnboardingAdapter(onboardingItems)
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = onboardingAdapter

        // Connect TabLayout with ViewPager2
        TabLayoutMediator(binding.tabIndicator, binding.viewPager) { _, _ -> }.attach()

        // ViewPager page change listener
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtonsForPosition(position)
            }
        })
    }

    private fun setupButtons() {
        // Skip button click
        binding.btnSkip.setOnClickListener {
            navigateToRegistration()
        }

        // Next button click
        binding.btnNext.setOnClickListener {
            val currentPosition = binding.viewPager.currentItem
            if (currentPosition < onboardingAdapter.itemCount - 1) {
                // Go to next page
                binding.viewPager.currentItem = currentPosition + 1
            } else {
                // On last page, navigate to registration
                navigateToRegistration()
            }
        }

        // Initialize buttons for first page
        updateButtonsForPosition(0)
    }

    private fun updateButtonsForPosition(position: Int) {
        // On last page, change Next button text to "Get Started"
        if (position == onboardingAdapter.itemCount - 1) {
            binding.btnNext.text = getString(R.string.get_started)
            binding.btnSkip.visibility = View.GONE
        } else {
            binding.btnNext.text = getString(R.string.next)
            binding.btnSkip.visibility = View.VISIBLE
        }
    }

    private fun navigateToRegistration() {
        // Mark onboarding as completed
        preferenceManager.setOnboardingCompleted(true)
        
        // Navigate to registration
        startActivity(Intent(this, RegistrationActivity::class.java))
        finish()
    }
}