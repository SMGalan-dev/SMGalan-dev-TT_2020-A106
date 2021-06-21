package com.example.tt_a106_v0.patient_fragments

import android.app.AlertDialog
import android.content.Context
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
    private val person = user?.email.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_medication,container,false)
        val newMedicine = mView.findViewById<Button>(R.id.newMedicamentBtn)
        newMedicine.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_Medicamentos_to_addMedicineFragment, null))

        val query: Query = FirebaseFirestore.getInstance().collection("persons").document(person).collection("patient").document("patientInfo").collection("medicationRegister")

        // Inflate the layout for this fragment
        val recyclerView = mView.findViewById<RecyclerView>(R.id.rwMedication)
        adapter = MedicationAdapter(query, this)
        recyclerView.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        return mView
    }

    override fun onMedicationSelected(medication: MedicationStructInDB?) {
        //Toast.makeText(activity, "No OnMedicationSelected Detail frame", Toast.LENGTH_SHORT).show()
        val docID = String.format("%s %s", medication?.date.toString(), medication?.time.toString())
        val builder = AlertDialog.Builder(activity as Context)
        builder.setTitle("ELIMINAR")
        builder.setMessage("Desea eliminar ${medication?.name.toString()}?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("SI"){dialogInterface, which ->
            FirebaseFirestore.getInstance().collection("persons").document(person).collection("patient").document("patientInfo").collection("medicationRegister").document(docID)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(activity as Context,"Medicación eliminada",Toast.LENGTH_SHORT).show() }
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




/*  CUADO DE DIALOGO AL PRESIONAR UN ITEM DEL RECYCLERVIEW      CUADRODEDIALOGO

    override fun onMedicationSelected(medication: MedicationStructInDB?) {
        //Toast.makeText(activity, "No OnMedicationSelected Detail frame", Toast.LENGTH_SHORT).show()
        val builder = AlertDialog.Builder(activity as Context)
        //set title for alert dialog
        builder.setTitle("R.string.dialogTitle")
        //set message for alert dialog
        builder.setMessage("R.string.dialogMessage")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            Toast.makeText(activity as Context,"clicked yes",Toast.LENGTH_LONG).show()
        }
        //performing cancel action
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(activity as Context,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
        }
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            Toast.makeText(activity as Context,"clicked No",Toast.LENGTH_LONG).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
 */