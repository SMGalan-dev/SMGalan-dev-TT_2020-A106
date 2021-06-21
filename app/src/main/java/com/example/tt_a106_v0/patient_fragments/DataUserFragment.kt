package com.example.tt_a106_v0.patient_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tt_a106_v0.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class DataUserFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val user = Firebase.auth.currentUser
        mView=inflater.inflate(R.layout.fragment_data_user,container,false)

        //db.collection("users").document(user?.email.toString()).get().addOnSuccessListener {
        db.collection("persons").document(user?.email.toString()).get().addOnSuccessListener {
            mView.findViewById<EditText>(R.id.twNamePatient).setText(it.get("name") as String?)
            mView.findViewById<EditText>(R.id.twLastNamePatient).setText(it.get("lastName") as String?)
            mView.findViewById<EditText>(R.id.twMLastNamePatient).setText(it.get("mLastName") as String?)
            mView.findViewById<EditText>(R.id.twGenrePatient).setText(it.get("genre") as String?)
            mView.findViewById<EditText>(R.id.twPhonePatient).setText(it.get("phone") as String?)
            mView.findViewById<EditText>(R.id.twBirthDPatient).setText(it.get("birthday") as String?)
            mView.findViewById<EditText>(R.id.twDiabetesType).setText(it.get("diabetes_type") as String?)
        }
        db.collection("persons").document(user?.email.toString()).collection("patient").document("patientInfo").get().addOnSuccessListener {
            mView.findViewById<EditText>(R.id.twDiabetesType).setText(it.get("diabetes_type") as String?)
        }

        val update = mView.findViewById<Button>(R.id.updateDataPatientBtn)
        update.setOnClickListener {
            val nameText = mView.findViewById<EditText>(R.id.twNamePatient)
            val apPText = mView.findViewById<EditText>(R.id.twLastNamePatient)
            val apMText = mView.findViewById<EditText>(R.id.twMLastNamePatient)
            val phoneReg = mView.findViewById<EditText>(R.id.twPhonePatient)
            val genreReg = mView.findViewById<EditText>(R.id.twGenrePatient)
            val birthday = mView.findViewById<EditText>(R.id.twBirthDPatient)
            val typeDiab = mView.findViewById<EditText>(R.id.twDiabetesType)
            if (nameText.text.isNotEmpty() && apPText.text.isNotEmpty() && apMText.text.isNotEmpty() && phoneReg.text.isNotEmpty() && genreReg.text.isNotEmpty()){
                db.collection("persons").document(user?.email.toString()).set(
                    hashMapOf(
                        "genre" to genreReg.text.toString(),
                        "name" to nameText.text.toString(),
                        "lastName" to apPText.text.toString(),
                        "mLastName" to apMText.text.toString(),
                        "birthday" to birthday.text.toString(),
                        "phone" to phoneReg.text.toString()
                    )
                )
                db.collection("persons").document(user?.email.toString()).collection("patient").document("patientInfo").set(
                    hashMapOf(
                        "diabetes_type" to typeDiab.text.toString()
                    )
                )
                Toast.makeText(activity, "Datos actualizados", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        return mView
    }

}