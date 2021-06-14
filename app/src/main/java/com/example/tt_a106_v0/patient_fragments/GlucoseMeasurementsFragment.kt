package com.example.tt_a106_v0.patient_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_a106_v0.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class GlucoseMeasurementsFragment : Fragment(), GlucoseAdapter.GlucoseAdapterListener {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var mView: View
    private lateinit var adapter: GlucoseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = Firebase.auth.currentUser
        mView=inflater.inflate(R.layout.fragment_glucose_measurements,container,false)
        val query: Query = FirebaseFirestore.getInstance().collection("persons").document(user?.email.toString()).collection("patient").document("patientInfo").collection("glucoseTestRecords")
        //val query: Query = FirebaseFirestore.getInstance().collection("users").document(user?.email.toString()).collection("glucoseTestRecords")

            // Inflate the layout for this fragment
        val recyclerView = mView.findViewById<RecyclerView>(R.id.recyclerGlucoseData)
        adapter = GlucoseAdapter(query, this)
        recyclerView.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY






        return mView
    }
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.startListening()
    }

    override fun onGlucoseSelected(glucose: GlucoseRecordsStructInDB?) {
        Toast.makeText(activity, "No OnGlucoseSelected Detail frame", Toast.LENGTH_SHORT).show()
        /*
        val intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra("SPORTS_DETAIL_DATA", sports)
        startActivity(intent)

         */
    }

}