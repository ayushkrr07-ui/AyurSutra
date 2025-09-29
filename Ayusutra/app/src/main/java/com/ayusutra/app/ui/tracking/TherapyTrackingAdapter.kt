package com.ayusutra.app.ui.tracking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayusutra.app.databinding.ItemTherapyTrackingBinding
import com.ayusutra.app.models.TherapySession
import java.text.SimpleDateFormat
import java.util.*

class TherapyTrackingAdapter(
    private val therapySessions: List<TherapySession>,
    private val onItemClick: (TherapySession) -> Unit
) : RecyclerView.Adapter<TherapyTrackingAdapter.TherapyTrackingViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TherapyTrackingViewHolder {
        val binding = ItemTherapyTrackingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TherapyTrackingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TherapyTrackingViewHolder, position: Int) {
        holder.bind(therapySessions[position])
    }

    override fun getItemCount(): Int = therapySessions.size

    inner class TherapyTrackingViewHolder(private val binding: ItemTherapyTrackingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(therapySession: TherapySession) {
            binding.apply {
                tvTherapyName.text = therapySession.therapyName
                tvTherapyDate.text = dateFormat.format(therapySession.date)
                tvPractitioner.text = therapySession.practitioner
                
                // Set ratings
                ratingEffectiveness.rating = therapySession.effectivenessRating.toFloat() / 2 // Convert to 5-star scale
                ratingComfort.rating = therapySession.comfortRating.toFloat() / 2 // Convert to 5-star scale
                
                tvSymptomChange.text = therapySession.symptomChange
                
                // Set click listener
                btnViewDetails.setOnClickListener {
                    onItemClick(therapySession)
                }
                
                // Make the whole card clickable
                root.setOnClickListener {
                    onItemClick(therapySession)
                }
            }
        }
    }
}