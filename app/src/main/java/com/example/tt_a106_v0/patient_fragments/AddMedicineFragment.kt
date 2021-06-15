package com.example.tt_a106_v0.patient_fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.tt_a106_v0.R
import com.example.tt_a106_v0.bleglucometer.DatePickerFragment
import com.example.tt_a106_v0.bleglucometer.TimePickerFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AddMedicineFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val user = Firebase.auth.currentUser
        mView=inflater.inflate(R.layout.fragment_add_medicine,container,false)
        val dateCh = mView.findViewById<EditText>(R.id.etDate)
        val timeCh = mView.findViewById<EditText>(R.id.etTime)
        dateCh.setOnClickListener { showDatePickerDialog() }
        timeCh.setOnClickListener { showTimePickerDialog() }

        /*

         db.collection("persons").document(user?.email.toString()).get().addOnSuccessListener {
             mView.findViewById<EditText>(R.id.twNamePatient).setText(it.get("name") as String?)
         }

         val newMedicine = mView.findViewById<Button>(R.id.addNewMedicamentBtn)
         newMedicine.setOnClickListener {

         }
         */
        /*

        update.setOnClickListener {
            val nameText = mView.findViewById<EditText>(R.id.twNamePatient)
            if (nameText.text.isNotEmpty() && apPText.text.isNotEmpty() && apMText.text.isNotEmpty() && phoneReg.text.isNotEmpty() && genreReg.text.isNotEmpty()){
                db.collection("persons").document(user?.email.toString()).set(
                    hashMapOf(
                        "name" to nameText.text.toString()
                    )
                )
                Toast.makeText(activity, "Datos actualizados", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
         */
        return mView
    }

    private fun showDatePickerDialog() {
        val datePicker= DatePickerFragment{ day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(activity?.supportFragmentManager!!, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int)  {
        mView.findViewById<EditText>(R.id.etDate).setText("Has seleccionado el $day del $month del año $year")
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { hourOfDay, minute ->  onTimeSelected(hourOfDay, minute)}
        timePicker.show(activity?.supportFragmentManager!!, "datePicker")
    }

    private fun onTimeSelected(hourOfDay: Int, minute: Int) {
        mView.findViewById<EditText>(R.id.etTime).setText("Has seleccionado el $hourOfDay : $minute")

    }


}