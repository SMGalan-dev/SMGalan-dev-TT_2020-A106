package com.example.tt_a106_v0.Users_register

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_a106_v0.R

enum class providerType{
    BASIC
}
class EndRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_register)

        // Patient Data Upload
        val bundle = intent.extras
        val typeUser = bundle?.getString("typeUser")
        val genre = bundle?.getInt("genre")
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        val genre2 = genre.toString()

        val dataPat = findViewById<Button>(R.id.regDataPatientBtn)
        dataPat.setOnClickListener {
            dataPatientReg(  typeUser?: "", genre2?: "", email ?: "")
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
            Log.d(typeUser, typeUser.toString())
            Log.d(genre.toString(), genre.toString())
            Log.d(email, email.toString())
            Log.d("Nombre", nameText.text.toString())
            Log.d("ApPaterno", apPText.text.toString())
            Log.d("ApMaterno", apMText.text.toString())
            Log.d("Telefono", phoneReg.text.toString())


            } else{
            Toast.makeText(this, "Por favor, rellene todos lso campos", Toast.LENGTH_SHORT).show()
        }

    }
}