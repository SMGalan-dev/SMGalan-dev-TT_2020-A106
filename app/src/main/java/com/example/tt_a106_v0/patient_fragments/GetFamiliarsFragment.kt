package com.example.tt_a106_v0.patient_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_a106_v0.R
import com.example.tt_a106_v0.bleglucometer.CiteRegisterStructInDB
import com.example.tt_a106_v0.bleglucometer.CitesAdapter
import com.example.tt_a106_v0.bleglucometer.FamiliarAdapter
import com.example.tt_a106_v0.bleglucometer.FamiliarRegisterStructInDB
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class GetFamiliarsFragment : Fragment(), FamiliarAdapter.FamiliarAdapterListener {
    private lateinit var mView: View
    private lateinit var adapter: FamiliarAdapter
    var user = Firebase.auth.currentUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_familiars,container,false)
        val newFamiliar = mView.findViewById<Button>(R.id.btnNewFamiliar)
        newFamiliar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_Familiar_to_addFamiliarFragment, null))


        val person = user?.email.toString()
        val query: Query = FirebaseFirestore.getInstance().collection("persons").document(person).collection("patient").document("patientInfo").collection("FamiliarReg")

        // Inflate the layout for this fragment
        val recyclerView = mView.findViewById<RecyclerView>(R.id.rwFamiliar)
        adapter = FamiliarAdapter(query, this)
        recyclerView.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY


        return mView
    }

    override fun onFamiliarSelected(familiar: FamiliarRegisterStructInDB?) {
        Toast.makeText(activity, "No OnCiteSelected Detail frame", Toast.LENGTH_SHORT).show()
    }
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.startListening()
    }
}