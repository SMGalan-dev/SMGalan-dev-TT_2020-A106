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
import com.example.tt_a106_v0.bleglucometer.CiteRegisterStructInDB
import com.example.tt_a106_v0.bleglucometer.CitesAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class CitesFragment : Fragment(), CitesAdapter.CitesAdapterListener {
    private lateinit var mView: View
    private lateinit var adapter: CitesAdapter
    var user = Firebase.auth.currentUser
    var person = user?.email.toString()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_cites,container,false)
        val newMedicine = mView.findViewById<Button>(R.id.btnNewCite)
        newMedicine.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_Citas_to_addCiteFragment, null))


        val person = user?.email.toString()
        val query: Query = FirebaseFirestore.getInstance().collection("persons").document(person).collection("patient").document("patientInfo").collection("citesRegister")

        // Inflate the layout for this fragment
        val recyclerView = mView.findViewById<RecyclerView>(R.id.rwCites)
        adapter = CitesAdapter(query, this)
        recyclerView.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY


        return mView
    }

    override fun onCiteSelected(cite: CiteRegisterStructInDB?) {
        Toast.makeText(activity, "No OnCiteSelected Detail frame", Toast.LENGTH_SHORT).show()
       /*
        val docID = cite?.date.toString()
        val builder = AlertDialog.Builder(activity as Context)
        builder.setTitle("ELIMINAR")
        builder.setMessage("Desea eliminar este registro?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("SI"){dialogInterface, which ->
            FirebaseFirestore.getInstance().collection("persons").document(person).collection("patient").document("patientInfo").collection("citesRegister").document(docID)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(activity as Context,"Cita eliminada",Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { //e -> Log.w("TAG", "Error deleting document", e)
                    Toast.makeText(activity as Context,"Ha ocurrido un error, por favor inténtelo de nuevo más tarde",Toast.LENGTH_SHORT).show()}
        }
        //performing cancel action
        builder.setNeutralButton("CANCELAR"){dialogInterface , which ->
            Toast.makeText(activity as Context,"Operación cancelada",Toast.LENGTH_SHORT).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
        */
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