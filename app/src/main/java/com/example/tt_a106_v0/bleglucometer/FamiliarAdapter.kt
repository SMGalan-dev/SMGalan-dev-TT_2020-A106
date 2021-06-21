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


class FamiliarAdapter(
    query: Query,
    private val listener: FamiliarAdapterListener
) : FirestoreAdapter<FamiliarAdapter.FamiliarViewHolder>(query) {

    class FamiliarViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val cardView: MaterialCardView = itemView.findViewById(R.id.item_familiar_card)
        private val name: TextView = itemView.findViewById(R.id.twItemFamiliarName)
        private val lastName: TextView = itemView.findViewById(R.id.twItemFamiliarLastName)
        private val mail: TextView = itemView.findViewById(R.id.twItemFamiliarEmail)
        private val phone: TextView = itemView.findViewById(R.id.twItemFamiliarPhone)

        fun bind(snapshot: DocumentSnapshot, listener: FamiliarAdapterListener) {
            val familiar: FamiliarRegisterStructInDB? = snapshot.toObject(FamiliarRegisterStructInDB::class.java)
            name.text = familiar?.name
            lastName.text = familiar?.lastName
            mail.text = snapshot.id
            phone.text = familiar?.phone
            cardView.setOnClickListener {
                listener.onFamiliarSelected(familiar)
            }
        }
    }

    interface FamiliarAdapterListener {
        fun onFamiliarSelected(familar: FamiliarRegisterStructInDB?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamiliarViewHolder {
        return FamiliarViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_familiar, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FamiliarViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }
}