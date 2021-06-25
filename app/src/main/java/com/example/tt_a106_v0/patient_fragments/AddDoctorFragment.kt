package com.example.tt_a106_v0.patient_fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tt_a106_v0.R
import com.example.tt_a106_v0.bleglucometer.DatePickerFragment
import com.example.tt_a106_v0.bleglucometer.TimePickerFragment
import com.example.tt_a106_v0.utils.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddDoctorFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var mView: View
    private val doctorsRef = FirebaseFirestore.getInstance().collection("persons")
    @SuppressLint("LogNotTimber")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView=inflater.inflate(R.layout.fragment_add_doctor,container,false)
        val user = Firebase.auth.currentUser
        Log.d("Name", user?.displayName.toString())
        val bttnAddDoctor = mView.findViewById<TextView>(R.id.btnAddDoctorAccept)
        bttnAddDoctor.setOnClickListener {
            val mail = mView.findViewById<EditText>(R.id.tvAddDoctorMail).text.toString()
            if(isValidString(mail)){
                val doctorRef = doctorsRef.document(mail)
                doctorRef.get()
                .addOnSuccessListener { data ->
                    val user = Firebase.auth.currentUser
                    if(data.exists()){
                        var notification = Notification(
                            MessageTypes.AddPatient,
                            "Hola, ${CurrentUser.name} ${CurrentUser.lastName} te mandó una solicitud para ser tu paciente",
                            CurrentUser.id
                        ).toMap()
                        doctorRef.update("notifications", FieldValue.arrayUnion(notification))
                        .addOnCompleteListener {
                            Toast.makeText(this.context, "Solicitud enviada", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this.context, "Hubo un error favor de intentar mas tarde", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else Toast.makeText(this.context, "El correo que ingresaste no este registrado :C. \nPor favor revise que lo haya ingresado de forma correcta", Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents: ", exception)
                }
            }
            else Toast.makeText(this.context, "Por favor ingresa un correo válido", Toast.LENGTH_SHORT).show()


        }


        // Inflate the layout for this fragment
        return mView
    }
    private fun showDatePickerDialog() {
        val datePicker= DatePickerFragment{ day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(activity?.supportFragmentManager!!, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int)  {
        mView.findViewById<EditText>(R.id.tvAddCiteDate).setText("$day-$month-$year")
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { hourOfDay, minute ->  onTimeSelected(hourOfDay, minute)}
        timePicker.show(activity?.supportFragmentManager!!, "datePicker")
    }

    private fun onTimeSelected(hourOfDay: Int, minute: Int) {
        mView.findViewById<EditText>(R.id.tvAddCiteTime).setText("$hourOfDay:$minute")

    }

}