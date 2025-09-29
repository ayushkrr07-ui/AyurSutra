package com.ayusutra.app.ui.registration

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ayusutra.app.R
import com.ayusutra.app.databinding.ActivityUhidConfirmationBinding
import com.ayusutra.app.ui.main.MainActivity

class UhidConfirmationActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_UHID = "extra_uhid"
        const val EXTRA_NAME = "extra_name"
    }

    private lateinit var binding: ActivityUhidConfirmationBinding
    private var uhid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUhidConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get UHID from intent
        uhid = intent.getStringExtra(EXTRA_UHID) ?: ""
        val name = intent.getStringExtra(EXTRA_NAME) ?: ""

        if (uhid.isEmpty()) {
            // If UHID is empty, something went wrong
            Toast.makeText(this, "Error generating UHID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Display UHID
        binding.tvUhidValue.text = uhid

        // Personalize message if name is available
        if (name.isNotEmpty()) {
            binding.tvCongratulations.text = getString(R.string.registration_success_name, name)
        }

        setupCopyButton()
        setupContinueButton()
    }

    private fun setupCopyButton() {
        binding.btnCopyUhid.setOnClickListener {
            // Copy UHID to clipboard
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("UHID", uhid)
            clipboard.setPrimaryClip(clip)
            
            // Show confirmation toast
            Toast.makeText(this, "UHID copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupContinueButton() {
        binding.btnContinue.setOnClickListener {
            // Navigate to main activity
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity() // Close all previous activities
        }
    }
}