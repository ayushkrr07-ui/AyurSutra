package com.ayusutra.app.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ayusutra.app.R
import com.ayusutra.app.data.models.FamilyMember

class FamilyMemberAdapter(
    private val familyMembers: List<FamilyMember>,
    private val onItemClick: (FamilyMember) -> Unit,
    private val onManageClick: (FamilyMember, View) -> Unit
) : RecyclerView.Adapter<FamilyMemberAdapter.FamilyMemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyMemberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_family_member, parent, false)
        return FamilyMemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: FamilyMemberViewHolder, position: Int) {
        val familyMember = familyMembers[position]
        holder.bind(familyMember)
    }

    override fun getItemCount(): Int = familyMembers.size

    inner class FamilyMemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivFamilyMemberAvatar)
        private val tvName: TextView = itemView.findViewById(R.id.tvFamilyMemberName)
        private val tvRelation: TextView = itemView.findViewById(R.id.tvFamilyMemberRelation)
        private val btnManage: ImageButton = itemView.findViewById(R.id.btnManageFamilyMember)

        fun bind(familyMember: FamilyMember) {
            tvName.text = familyMember.name
            tvRelation.text = familyMember.relation
            
            // Set click listeners
            itemView.setOnClickListener { onItemClick(familyMember) }
            btnManage.setOnClickListener { onManageClick(familyMember, btnManage) }
        }
    }
}