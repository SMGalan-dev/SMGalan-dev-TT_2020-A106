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
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class AddDoctorFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var mView: View

    @SuppressLint("LogNotTimber")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = Firebase.auth.currentUser
        mView=inflater.inflate(R.layout.fragment_add_doctor,container,false)
        val mail = mView.findViewById<EditText>(R.id.tvAddDoctorMail)

        val newCite = mView.findViewById<TextView>(R.id.btnAddFamiliarAccept)
        newCite.setOnClickListener {
            val title = mView.findViewById<EditText>(R.id.tvAddCiteTitle)
            Log.d("TITLE", title.text.toString())
            //val date = mView.findViewById<EditText>(R.id.tvAddCiteDate)


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