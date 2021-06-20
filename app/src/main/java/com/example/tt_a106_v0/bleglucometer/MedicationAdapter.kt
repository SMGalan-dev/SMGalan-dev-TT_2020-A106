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


class MedicationAdapter(
    query: Query,
    private val listener: MedicationAdapterListener
) : FirestoreAdapter<MedicationAdapter.MedicationViewHolder>(query) {

    class MedicationViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView.findViewById(R.id.item_medication_card)
        private val date: TextView = itemView.findViewById(R.id.twItemMedFirstDate)
        private val name: TextView = itemView.findViewById(R.id.twItemMedName)
        private val type: TextView = itemView.findViewById(R.id.twItemMedType)
        private val time: TextView = itemView.findViewById(R.id.twItemMedFirstTime)
        private val dosis: TextView = itemView.findViewById(R.id.twItemMedDosis)
        private val frequency: TextView = itemView.findViewById(R.id.twItemMedFrec)
        private val comment: TextView = itemView.findViewById(R.id.twItemMedComment)

        fun bind(snapshot: DocumentSnapshot, listener: MedicationAdapterListener) {
            val medication: MedicationStructInDB? = snapshot.toObject(MedicationStructInDB::class.java)
            date.text = medication?.date
            name.text = medication?.name
            type.text = medication?.type
            time.text = medication?.time
            dosis.text = medication?.dosis
            frequency.text = medication?.frequency
            comment.text = medication?.comment

            cardView.setOnClickListener {
                listener.onMedicationSelected(medication)
            }
        }
    }

    interface MedicationAdapterListener {
        fun onMedicationSelected(medication: MedicationStructInDB?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        return MedicationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_medication, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }
}