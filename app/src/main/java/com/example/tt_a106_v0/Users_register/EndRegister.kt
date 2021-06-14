package com.example.tt_a106_v0.Users_register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_a106_v0.MainActivityPatient1
import com.example.tt_a106_v0.R
import com.google.firebase.firestore.FirebaseFirestore

class EndRegister : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_register)

        // Patient Data Upload
        val bundle = intent.extras
        val typeUser = bundle?.getString("typeUser")
        val genre = bundle?.getInt("genre")
        val email = bundle?.getString("email")
        val genre2 = genre.toString()
        val genre3 = genreEsp(genre?.toInt()!!)

        val dataPat = findViewById<Button>(R.id.regDataPatientBtn)
        dataPat.setOnClickListener {
            dataPatientReg(  typeUser?: "", genre3?: "", email ?: "")
        }

    }
    private fun dataPatientReg(typeUser: String, genre: String, email:String){
        val nameText = findViewById<EditText>(R.id.editTextRegName)
        val apPText = findViewById<EditText>(R.id.editTextTRegApPaterno)
        val apMText = findViewById<EditText>(R.id.editTextRegApMaterno)
        val phoneReg = findViewById<EditText>(R.id.editTextRegPhone)

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
            db.collection("persons").document(email).set(
                hashMapOf(
                    "name" to nameText.text.toString(),
                    "lastName" to apPText.text.toString(),
                    "mLastName" to apMText.text.toString(),
                    "user_Type" to typeUser,
                    "email" to email,
                    //Añadir fecha de nacimiento
                    "genre" to genre,
                    "phone" to phoneReg.text.toString()
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
            val intoUserIntent = Intent(this, MainActivityPatient1::class.java)
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