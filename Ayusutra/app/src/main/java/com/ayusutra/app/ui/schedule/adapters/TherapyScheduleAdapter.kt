package com.ayusutra.app.ui.schedule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ayusutra.app.R
import com.ayusutra.app.data.models.Therapy

class TherapyScheduleAdapter(
    private val therapies: List<Therapy>,
    private val onTherapyClick: (Therapy) -> Unit
) : RecyclerView.Adapter<TherapyScheduleAdapter.TherapyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TherapyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_therapy_schedule, parent, false)
        return TherapyViewHolder(view)
    }

    override fun onBindViewHolder(holder: TherapyViewHolder, position: Int) {
        val therapy = therapies[position]
        holder.bind(therapy)
    }

    override fun getItemCount(): Int = therapies.size

    inner class TherapyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTherapyTime: TextView = itemView.findViewById(R.id.tvTherapyTime)
        private val tvTherapyName: TextView = itemView.findViewById(R.id.tvTherapyName)
        private val tvTherapistName: TextView = itemView.findViewById(R.id.tvTherapistName)
        private val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        private val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)

        fun bind(therapy: Therapy) {
            tvTherapyTime.text = therapy.formattedDateTime
            tvTherapyName.text = therapy.name
            tvTherapistName.text = therapy.therapistName ?: ""
            tvDuration.text = "${therapy.durationMinutes} minutes"
            tvLocation.text = therapy.location
            
            // Set click listener for the entire item
            itemView.setOnClickListener { onTherapyClick(therapy) }
        }
    }
}