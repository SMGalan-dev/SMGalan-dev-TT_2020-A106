package com.example.tt_a106_v0.patient_fragments

import android.app.AlertDialog
import android.content.Context
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
    val user = Firebase.auth.currentUser
    private val person = user?.email.toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = Firebase.auth.currentUser
        mView=inflater.inflate(R.layout.fragment_glucose_measurements,container,false)
        val query: Query = FirebaseFirestore.getInstance().collection("persons").document(person).collection("patient").document("patientInfo").collection("glucoseTestRecords")
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
        //Toast.makeText(activity, "No OnGlucoseSelected Detail frame", Toast.LENGTH_SHORT).show()
        val docID = glucose?.date.toString()
        val builder = AlertDialog.Builder(activity as Context)
        builder.setTitle("ELIMINAR")
        builder.setMessage("Desea eliminar este registro?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("SI"){dialogInterface, which ->
            FirebaseFirestore.getInstance().collection("persons").document(person).collection("patient").document("patientInfo").collection("glucoseTestRecords").document(docID)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(activity as Context,"Medici??n eliminada",Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { //e -> Log.w("TAG", "Error deleting document", e)
                    Toast.makeText(activity as Context,"Ha ocurrido un error, por favor int??ntelo de nuevo m??s tarde",Toast.LENGTH_SHORT).show()}
        }
        //performing cancel action
        builder.setNeutralButton("CANCELAR"){dialogInterface , which ->
            Toast.makeText(activity as Context,"Operaci??n cancelada",Toast.LENGTH_SHORT).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


}