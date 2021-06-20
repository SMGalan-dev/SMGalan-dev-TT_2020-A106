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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AddCiteFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var mView: View

    @SuppressLint("LogNotTimber")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = Firebase.auth.currentUser
        mView=inflater.inflate(R.layout.fragment_add_cite,container,false)
        val date = mView.findViewById<EditText>(R.id.tvAddCiteDate)
        val time = mView.findViewById<EditText>(R.id.tvAddCiteTime)
        date.setOnClickListener { showDatePickerDialog() }
        time.setOnClickListener { showTimePickerDialog() }

        val newCite = mView.findViewById<TextView>(R.id.btnAddNewCite)
        newCite.setOnClickListener {
            val title = mView.findViewById<EditText>(R.id.tvAddCiteTitle)
            Log.d("TITLE", title.text.toString())
            //val date = mView.findViewById<EditText>(R.id.tvAddCiteDate)
            Log.d("DATE", date.text.toString())
            //val time = mView.findViewById<EditText>(R.id.tvAddCiteTime)
            Log.d("TIME", time.text.toString())
            val place = mView.findViewById<EditText>(R.id.tvAddCitePlace)
            Log.d("place", place.text.toString())
            val medic = mView.findViewById<EditText>(R.id.tvAddCiteMedic)
            Log.d("medic", medic.text.toString())
            val comment = mView.findViewById<EditText>(R.id.tvAddCiteComment)
            Log.d("comment", comment.text.toString())
            val citeID = String.format("%s %s", date.text.toString(), time.text.toString())


            if (date.text.isNotEmpty() && time.text.isNotEmpty()){
                db.collection("persons").document(user?.email.toString()).collection("patient").document("patientInfo").collection("citesRegister").document(citeID).set(
                    hashMapOf(
                        "title" to title.text.toString(),
                        "date" to date.text.toString(),
                        "time" to time.text.toString(),
                        "place" to place.text.toString(),
                        "medic" to medic.text.toString(),
                        "comment" to comment.text.toString()
                    )
                )
                Toast.makeText(activity, "Cita creada con éxito", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity, "Seleccione una fecha y hora", Toast.LENGTH_SHORT).show()
            }

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