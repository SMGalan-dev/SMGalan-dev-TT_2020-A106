package com.example.tt_a106_v0.Users_register

import com.example.tt_a106_v0.DateInputMask
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_a106_v0.MainActivityFamiliar
import com.example.tt_a106_v0.MainActivityPatient1
import com.example.tt_a106_v0.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EndRegisterFam : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_register_fam)

        // Patient Data Upload
        val bundle = intent.extras
        val typeUser = bundle?.getString("typeUser")
        val genre = bundle?.getInt("genre")
        val email = bundle?.getString("email")
        val genre2 = genre.toString()
        val genre3 = genreEsp(genre?.toInt()!!)
        val dataPat = findViewById<Button>(R.id.regDataPatientBtn)


        val date = findViewById<EditText>(R.id.editTextBirth)

        //date format

        date.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val ddmmyyyy = "DDMMYYYY"
            private val cal: Calendar = Calendar.getInstance()
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    var clean = s.toString().replace("[^\\d.]".toRegex(), "")
                    val cleanC = current.replace("[^\\d.]".toRegex(), "")
                    val cl = clean.length
                    var sel = cl
                    var i = 2
                    while (i <= cl && i < 6) {
                        sel++
                        i += 2
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean == cleanC) sel--
                    if (clean.length < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length)
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        var day = clean.substring(0, 2).toInt()
                        var mon = clean.substring(2, 4).toInt()
                        var year = clean.substring(4, 8).toInt()
                        if (mon > 12) mon = 12
                        cal.set(Calendar.MONTH, mon - 1)
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        cal.set(Calendar.YEAR, year)
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012
                        day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(
                            Calendar.DATE
                        ) else day
                        clean = String.format("%02d%02d%02d", day, mon, year)
                    }
                    clean = String.format(
                        "%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8)
                    )
                    sel = if (sel < 0) 0 else sel
                    current = clean
                    date.setText(current)
                    date.setSelection(if (sel < current.length) sel else current.length)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })


        dataPat.setOnClickListener {
            dataPatientReg(  typeUser?: "", genre3?: "", email ?: "")
        }

    }
    private fun dataPatientReg(typeUser: String, genre: String, email:String){
        val nameText = findViewById<EditText>(R.id.editTextRegName)
        val apPText = findViewById<EditText>(R.id.editTextTRegApPaterno)
        val apMText = findViewById<EditText>(R.id.editTextRegApMaterno)
        val phoneReg = findViewById<EditText>(R.id.editTextRegPhone)
        val birth = findViewById<EditText>(R.id.editTextBirth)

        //val intoUserIntent = Intent(this, MainActivityPatient::class.java)
        //startActivity(intoUserIntent)
        if (nameText.text.isNotEmpty() && apPText.text.isNotEmpty() && apMText.text.isNotEmpty() && phoneReg.text.isNotEmpty()){
            Log.d("typeUser", typeUser.toString())
            Log.d("Genero", genre.toString())
            Log.d("email", email.toString())
            Log.d("Nombre", nameText.text.toString())
            Log.d("ApPaterno", apPText.text.toString())
            Log.d("ApMaterno", apMText.text.toString())
            Log.d("Telefono", phoneReg.text.toString())
            Log.d("Nacimiento", birth.text.toString())
            db.collection("persons").document(email).set(
                hashMapOf(
                    "name" to nameText.text.toString(),
                    "lastName" to apPText.text.toString(),
                    "mLastName" to apMText.text.toString(),
                    "user_Type" to typeUser,
                    "email" to email,
                    //Añadir fecha de nacimiento
                    "genre" to genre,
                    "phone" to phoneReg.text.toString(),
                    "birthday" to birth.text.toString()
                )
            )
            /*
            db.collection("users").document(email).set(
                hashMapOf(
                    "user_Type" to typeUser,
                    "genre" to genre,
                    //"email" to email,
                    "name" to nameText.text.toString(),
                    "lastName" to apPText.text.toString(),
                    "mLastName" to apMText.text.toString(),
                    "phone" to phoneReg.text.toString()
                )
            )
             */
            val intoUserIntent = Intent(this, MainActivityFamiliar::class.java)
            startActivity(intoUserIntent)

        } else{
            Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
        }

    }
    private fun genreEsp(genre: Int): String {
        if (genre == 0){
            return "Masculino"
        } else{
            return  "Femenino"
        }

    }
}