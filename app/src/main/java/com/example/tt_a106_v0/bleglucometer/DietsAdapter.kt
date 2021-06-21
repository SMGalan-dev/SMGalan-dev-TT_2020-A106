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


class DietsAdapter(
    query: Query,
    private val listener: DietsAdapterListener
) : FirestoreAdapter<DietsAdapter.DietsViewHolder>(query) {

    class DietsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val cardView: MaterialCardView = itemView.findViewById(R.id.item_diet_card)
        private val url: TextView = itemView.findViewById(R.id.twItemDietOther)
        private val date: TextView = itemView.findViewById(R.id.twItemDietDate)

        @SuppressLint("SetTextI18n")    //Solo para agregar el texto a url.text
        fun bind(snapshot: DocumentSnapshot, listener: DietsAdapterListener) {
            val diet: DietsDocsStructInDB? = snapshot.toObject(DietsDocsStructInDB::class.java)
            url.text = "Dieta agregada el:"
            //url.text = diet?.url
            date.text = diet?.date

            cardView.setOnClickListener {
                listener.onDietSelected(diet)
            }
        }
    }

    interface DietsAdapterListener {
        fun onDietSelected(diet: DietsDocsStructInDB?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietsViewHolder {
        return DietsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_diets, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DietsViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }
}