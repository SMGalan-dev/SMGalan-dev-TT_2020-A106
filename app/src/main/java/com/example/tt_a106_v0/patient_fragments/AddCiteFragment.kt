package com.example.tt_a106_v0.patient_fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
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
import java.util.*

class AddCiteFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    val user = Firebase.auth.currentUser
    private val person = user?.email.toString()
    private lateinit var mView: View
    private lateinit var title: String

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var hourOfDay: Int = 0
    private var minute: Int = 0

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
        val title = mView.findViewById<EditText>(R.id.tvAddCiteTitle)
        val place = mView.findViewById<EditText>(R.id.tvAddCitePlace)
        val medic = mView.findViewById<EditText>(R.id.tvAddCiteMedic)
        val comment = mView.findViewById<EditText>(R.id.tvAddCiteComment)
        //val citeID = String.format("%s %s", date.text.toString(), time.text.toString())

        val newCite = mView.findViewById<TextView>(R.id.btnAddNewCite)
        newCite.setOnClickListener {
            if (date.text.isNotEmpty() && time.text.isNotEmpty()){
                val dateF = String.format("%02d-%02d-%02d", day, month, year)
                val timeF = String.format("%02d:%02d", hourOfDay, minute)
                val citeID = String.format("%s %s", dateF, timeF)

                db.collection("persons").document(person).collection("patient").document("patientInfo").collection("citesRegister").document(citeID).set(
                    hashMapOf(
                        "title" to title.text.toString(),
                        "date" to dateF,
                        "time" to timeF,
                        "place" to place.text.toString(),
                        "medic" to medic.text.toString(),
                        "comment" to comment.text.toString()
                    )
                )


                val intent = Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI).apply {
                    val beginTime: Calendar = Calendar.getInstance().apply {
                        set(year, month, day, hourOfDay, minute)
                    }
                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
                    putExtra(CalendarContract.Events.CAN_INVITE_OTHERS, medic.text.toString())
                    putExtra(CalendarContract.Events.TITLE, title.text.toString())
                    putExtra(CalendarContract.Events.DESCRIPTION, comment.text.toString())
                    putExtra(CalendarContract.Events.EVENT_LOCATION, place.text.toString());
                    putExtra(CalendarContract.Events.HAS_ALARM, 1)
                }
                startActivity(intent)

                //Toast.makeText(activity, "Cita creada con éxito", Toast.LENGTH_SHORT).show()



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
        this.day = day
        this.month = month
        this.year = year
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { hourOfDay, minute ->  onTimeSelected(hourOfDay, minute)}
        timePicker.show(activity?.supportFragmentManager!!, "datePicker")
    }

    private fun onTimeSelected(hourOfDay: Int, minute: Int) {
        mView.findViewById<EditText>(R.id.tvAddCiteTime).setText("$hourOfDay:$minute")
        this.hourOfDay = hourOfDay
        this.minute = minute

    }

}