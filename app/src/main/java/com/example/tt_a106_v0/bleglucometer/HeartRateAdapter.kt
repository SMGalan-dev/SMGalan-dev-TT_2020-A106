package com.example.tt_a106_v0.bleglucometer

import android.annotation.SuppressLint
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


class HeartRateAdapter(
    query: Query,
    private val listener: HeartRateAdapterListener
) : FirestoreAdapter<HeartRateAdapter.HeartRateViewHolder>(query) {

    class HeartRateViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val cardView: MaterialCardView = itemView.findViewById(R.id.item_hr_card)
        private val ppm: TextView = itemView.findViewById(R.id.twItemHROther)
        private val time: TextView = itemView.findViewById(R.id.twItemHRTime)
        private val date: TextView = itemView.findViewById(R.id.twItemHRDate)

        @SuppressLint("SetTextI18n")    //Solo para agregar el texto a url.text
        fun bind(snapshot: DocumentSnapshot, listener: HeartRateAdapterListener) {
            val heartRate: HeartRateStructInDB? = snapshot.toObject(HeartRateStructInDB::class.java)
            ppm.text = String.format("Ritmo cardiaco: %s ppm", heartRate?.ppm.toString())
            time.text = heartRate?.time
            date.text = heartRate?.date

            cardView.setOnClickListener {
                listener.onHeartRateSelected(heartRate)
            }
        }
    }

    interface HeartRateAdapterListener {
        fun onHeartRateSelected(heartRate: HeartRateStructInDB?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeartRateViewHolder {
        return HeartRateViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_heartrate, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HeartRateViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }
}