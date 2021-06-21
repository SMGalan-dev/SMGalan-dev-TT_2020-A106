package com.example.tt_a106_v0.patient_fragments


import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tt_a106_v0.R
import com.example.tt_a106_v0.bleglucometer.DatePickerFragment
import com.example.tt_a106_v0.bleglucometer.TimePickerFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AddMedicineFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var mView: View
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var hourOfDay: Int = 0
    private var minute: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val user = Firebase.auth.currentUser
        val person = user?.email.toString()
        mView=inflater.inflate(R.layout.fragment_add_medicine,container,false)
        val dateICh = mView.findViewById<EditText>(R.id.etAddMedicineDateBegin)
        val dateFCh = mView.findViewById<EditText>(R.id.etAddMedicineDateFinish)
        val timeCh = mView.findViewById<EditText>(R.id.etAddMedicineTime)
        dateICh.setOnClickListener { showDatePickerDialog() }
        dateFCh.setOnClickListener{showDateFinishPickerDialog()}
        timeCh.setOnClickListener { showTimePickerDialog() }

        val addMedName = mView.findViewById<EditText>(R.id.etAddMedicineName)
        val addDosis = mView.findViewById<EditText>(R.id.etAddMedicineDosis)
        val addFreq = mView.findViewById<EditText>(R.id.etAddMedFrecuencia)
        val addType = mView.findViewById<EditText>(R.id.etAddMedType)
        val addcomment = mView.findViewById<EditText>(R.id.etAddMedComment)
        val addMed = mView.findViewById<Button>(R.id.btnAddNewMed)
        addMed.setOnClickListener {
            val dateF = String.format("%02d-%02d-%02d", day, month, year)
            val dateT = String.format("%02d-%02d-%02d", day, month, year)
            val timeF = String.format("%02d:%02d", hourOfDay, minute)
            val date = String.format("%s %s", dateF, timeF)
            if (dateICh.text.isNotEmpty() && timeCh.text.isNotEmpty() && addDosis.text.isNotEmpty() && addMedName.text.isNotEmpty()){
                db.collection("persons").document(person).collection("patient").document("patientInfo").collection("medicationRegister").document(date).set(
                    hashMapOf(
                        "date" to dateF,
                        "name" to addMedName.text.toString(),
                        "type" to addType.text.toString(),
                        "time" to timeF,
                        "dosis" to addDosis.text.toString(),
                        "frequency" to addFreq.text.toString(),
                        "comment" to addcomment.text.toString(),
                        "dateF" to dateT
                    )
                )
                Log.d("date", dateF)
                Log.d("dateF", dateT)
                Log.d("name", addMedName.text.toString())
                Log.d("type", addType.text.toString())
                Log.d("time", timeF)
                Log.d("dosis", addDosis.text.toString())
                Log.d("freq", addFreq.text.toString())
                Log.d("commen", addcomment.text.toString())
                Log.e("DATE", date)
                Toast.makeText(activity, "Medicación creada", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()



                val intent = Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI).apply {
                    val beginTime: Calendar = Calendar.getInstance().apply {
                        set(year, month, day, hourOfDay, minute)
                    }
                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
                    putExtra(CalendarContract.Events.TITLE, addMedName.text.toString())
                    putExtra(CalendarContract.Events.DESCRIPTION, addcomment.text.toString())
                    putExtra(CalendarContract.Events.HAS_ALARM, 1)
                    putExtra(CalendarContract.Reminders.MINUTES, 5)

                }
                startActivity(intent)

            }else{
                Toast.makeText(activity, "Por favor, rellene los campos basicos", Toast.LENGTH_SHORT).show()
            }

        }
        return mView
    }


    private fun showDatePickerDialog() {
        val datePicker= DatePickerFragment{ day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(activity?.supportFragmentManager!!, "datePicker")
    }
    private fun showDateFinishPickerDialog() {
        val datePicker= DatePickerFragment{ day, month, year -> onDateSelectedFinish(day, month, year)}
        datePicker.show(activity?.supportFragmentManager!!, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int)  {
        mView.findViewById<EditText>(R.id.etAddMedicineDateBegin).setText("$day / $month / $year")
        this.day = day
        this.month = month
        this.year = year

    }
    private fun onDateSelectedFinish(day: Int, month: Int, year: Int)  {
        mView.findViewById<EditText>(R.id.etAddMedicineDateFinish).setText("$day / $month / $year")
        this.day = day
        this.month = month
        this.year = year

    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { hourOfDay, minute ->  onTimeSelected(hourOfDay, minute)}
        timePicker.show(activity?.supportFragmentManager!!, "datePicker")
    }

    private fun onTimeSelected(hourOfDay: Int, minute: Int) {
        mView.findViewById<EditText>(R.id.etAddMedicineTime).setText("$hourOfDay : $minute")
        this.hourOfDay = hourOfDay
        this.minute = minute
    }


}