package com.ayusutra.app.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ayusutra.app.R
import com.ayusutra.app.data.models.Therapy
import com.google.android.material.button.MaterialButton

class UpcomingTherapyAdapter(
    private val therapies: List<Therapy>,
    private val onViewDetailsClick: (Therapy) -> Unit,
    private val onRescheduleClick: (Therapy) -> Unit
) : RecyclerView.Adapter<UpcomingTherapyAdapter.TherapyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TherapyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_upcoming_therapy, parent, false)
        return TherapyViewHolder(view)
    }

    override fun onBindViewHolder(holder: TherapyViewHolder, position: Int) {
        val therapy = therapies[position]
        holder.bind(therapy)
    }

    override fun getItemCount(): Int = therapies.size

    inner class TherapyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvTherapyDate)
        private val tvName: TextView = itemView.findViewById(R.id.tvTherapyName)
        private val tvDuration: TextView = itemView.findViewById(R.id.tvTherapyDuration)
        private val tvLocation: TextView = itemView.findViewById(R.id.tvTherapyLocation)
        private val btnReschedule: MaterialButton = itemView.findViewById(R.id.btnReschedule)
        private val btnViewDetails: MaterialButton = itemView.findViewById(R.id.btnViewDetails)

        fun bind(therapy: Therapy) {
            tvDate.text = therapy.formattedDateTime
            tvName.text = therapy.name
            tvDuration.text = "${therapy.durationMinutes} minutes"
            tvLocation.text = therapy.location
            
            // Set click listeners
            btnViewDetails.setOnClickListener { onViewDetailsClick(therapy) }
            btnReschedule.setOnClickListener { onRescheduleClick(therapy) }
        }
    }
}