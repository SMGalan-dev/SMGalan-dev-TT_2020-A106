
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


class CitesAdapter(
    query: Query,
    private val listener: CitesAdapterListener
) : FirestoreAdapter<CitesAdapter.CiteViewHolder>(query) {

    class CiteViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val cardView: MaterialCardView = itemView.findViewById(R.id.item_cite_card)
        private val comment: TextView = itemView.findViewById(R.id.twItemCiteComment)
        private val date: TextView = itemView.findViewById(R.id.twItemCiteDate)
        private val medic: TextView = itemView.findViewById(R.id.twItemCiteMedic)
        private val place: TextView = itemView.findViewById(R.id.twItemCitePlace)
        private val time: TextView = itemView.findViewById(R.id.twItemCiteTime)
        private val title: TextView = itemView.findViewById(R.id.twItemCiteTitle)

        fun bind(snapshot: DocumentSnapshot, listener: CitesAdapterListener) {
            val cite: CiteRegisterStructInDB? = snapshot.toObject(CiteRegisterStructInDB::class.java)
            comment.text = cite?.comment
            date.text = cite?.date
            medic.text = cite?.medic
            place.text = cite?.place
            time.text = cite?.time
            title.text = cite?.title

            cardView.setOnClickListener {
                listener.onCiteSelected(cite)
            }
        }
    }

    interface CitesAdapterListener {
        fun onCiteSelected(cite: CiteRegisterStructInDB?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CiteViewHolder {
        return CiteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_cite_register, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CiteViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }
}