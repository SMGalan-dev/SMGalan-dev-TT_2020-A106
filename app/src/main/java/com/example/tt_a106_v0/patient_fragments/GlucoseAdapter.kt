package com.example.tt_a106_v0.patient_fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_a106_v0.R
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query


class GlucoseAdapter(
    query: Query,
    private val listener: GlucoseAdapterListener
) : FirestoreAdapter<GlucoseAdapter.GlucoseViewHolder>(query) {

    class GlucoseViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val cardView: MaterialCardView = itemView.findViewById(R.id.item_glucose_card)
        private val glucoselevel: TextView = itemView.findViewById(R.id.twItemGLDataGlucose)
        private val unit: TextView = itemView.findViewById(R.id.twItemUnitDataGlucose)
        private val date: TextView = itemView.findViewById(R.id.twItemDateDataGlucose)

        fun bind(snapshot: DocumentSnapshot, listener: GlucoseAdapterListener) {
            val measurement: GlucoseRecordsStructInDB? = snapshot.toObject(GlucoseRecordsStructInDB::class.java)
            glucoselevel.text = measurement?.glucoseLevel
            unit.text = measurement?.unit
            date.text = measurement?.date

            cardView.setOnClickListener {
                listener.onGlucoseSelected(measurement)
            }
        }
    }

    interface GlucoseAdapterListener {
        fun onGlucoseSelected(glucose: GlucoseRecordsStructInDB?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlucoseViewHolder {
        return GlucoseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_glucose_measurement, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GlucoseViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }
}