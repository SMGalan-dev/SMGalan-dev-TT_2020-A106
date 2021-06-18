package com.example.tt_a106_v0.patient_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_a106_v0.R
import com.example.tt_a106_v0.bleglucometer.MedicationAdapter
import com.example.tt_a106_v0.bleglucometer.MedicationStructInDB
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase


class MedicationFragment : Fragment(), MedicationAdapter.MedicationAdapterListener {
    private lateinit var mView: View
    private lateinit var adapter: MedicationAdapter
    var user = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_medication,container,false)
        val newMedicine = mView.findViewById<Button>(R.id.newMedicamentBtn)
        newMedicine.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_Medicamentos_to_addMedicineFragment, null))

        val person = user?.email.toString()
        val query: Query = FirebaseFirestore.getInstance().collection("persons").document(person).collection("patient").document("patientInfo").collection("medicationRegister")

        // Inflate the layout for this fragment
        val recyclerView = mView.findViewById<RecyclerView>(R.id.rwMedication)
        adapter = MedicationAdapter(query, this)
        recyclerView.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        return mView
    }

    override fun onMedicationSelected(medication: MedicationStructInDB?) {
        Toast.makeText(activity, "No OnMedicationSelected Detail frame", Toast.LENGTH_SHORT).show()
    }

}