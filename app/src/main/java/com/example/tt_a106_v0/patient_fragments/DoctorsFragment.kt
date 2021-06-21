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
import com.example.tt_a106_v0.bleglucometer.DoctorAdapter
import com.example.tt_a106_v0.bleglucometer.DoctorRegisterStructInDB
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class DoctorsFragment : Fragment(), DoctorAdapter.DoctorAdapterListener {
    private lateinit var mView: View
    private lateinit var adapter: DoctorAdapter
    var user = Firebase.auth.currentUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_doctors,container,false)
        val newFamiliar = mView.findViewById<Button>(R.id.btnNewDoctor)
        newFamiliar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_Doctor_to_addDoctorFragment, null))


        val person = user?.email.toString()
        val query: Query = FirebaseFirestore.getInstance().collection("persons").document(person).collection("patient").document("patientInfo").collection("DoctorReg")

        // Inflate the layout for this fragment
        val recyclerView = mView.findViewById<RecyclerView>(R.id.rwDoctor)
        adapter = DoctorAdapter(query, this)
        recyclerView.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY


        return mView
    }

    override fun onDoctorSelected(doctor: DoctorRegisterStructInDB?) {
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