package com.example.tt_a106_v0.bleglucometer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_a106_v0.R
import com.example.tt_a106_v0.patient_fragments.FirestoreAdapter
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query


class DoctorAdapter(
    query: Query,
    private val listener: DoctorAdapterListener
) : FirestoreAdapter<DoctorAdapter.DoctorViewHolder>(query) {

    class DoctorViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val cardView: MaterialCardView = itemView.findViewById(R.id.item_doctor_card)
        private val name: TextView = itemView.findViewById(R.id.twItemDoctorName)
        private val lastName: TextView = itemView.findViewById(R.id.twItemDoctorLastName)
        private val mail: TextView = itemView.findViewById(R.id.twItemDoctorEmail)
        private val phone: TextView = itemView.findViewById(R.id.twItemDoctorPhone)

        fun bind(snapshot: DocumentSnapshot, listener: DoctorAdapterListener) {
            val doctor: DoctorRegisterStructInDB? = snapshot.toObject(DoctorRegisterStructInDB::class.java)
            name.text = doctor?.name
            lastName.text = doctor?.lastName
            mail.text = snapshot.id
            phone.text = doctor?.phone
            cardView.setOnClickListener {
                listener.onDoctorSelected(doctor)
            }
        }
    }

    interface DoctorAdapterListener {
        fun onDoctorSelected(doctor: DoctorRegisterStructInDB?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        return DoctorViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_doctor, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }
}