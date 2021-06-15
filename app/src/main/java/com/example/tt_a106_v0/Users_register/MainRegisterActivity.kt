package com.example.tt_a106_v0.Users_register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_a106_v0.R
import com.google.firebase.auth.FirebaseAuth


class MainRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_register)
        firstRegister()
    }

    private fun firstRegister() {
        val typeUserCheck = findViewById<Spinner>(R.id.SpinnerUserType)
        val generoOpc = findViewById<RadioGroup>(R.id.opciones_sexo)
        val email = findViewById<TextView>(R.id.emailEditText)
        val password = findViewById<EditText>(R.id.passwordEditText)
        val passwordcheck = findViewById<EditText>(R.id.passwordConfirmEditText)
        val cancelBtn = findViewById<Button>(R.id.cancelRegisterBtn)
        val continueRegBtn = findViewById<Button>(R.id.continueRegisterBtn)
        title = "Autenticación"

        cancelBtn.setOnClickListener {
            Log.d("Cancel", "cancelBtn.setOnClickListener")
            onBackPressed()
        }

        continueRegBtn.setOnClickListener {
            //Tipo ed usuario
            val typeUser = typeUserCheck.selectedItem.toString()
            Log.d(typeUser, typeUser.toString())
            //Genero
            val radioButtonID: Int = generoOpc.checkedRadioButtonId
            val radioButton: View = generoOpc.findViewById<RadioButton>(radioButtonID)
            val genre: Int = generoOpc.indexOfChild(radioButton) //0 masculino, 1 Femenino
            Log.d(genre.toString(), genre.toString())

            //val selectedtext = genero.getChildAt(idx).text.toString()
            if (email.text.isNotEmpty() && password.text.isNotEmpty() && password.text.toString() == passwordcheck.text.toString()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                            if(typeUser=="Paciente")
                                continuePat(typeUser.toString(), genre, it.result?.user?.email ?: "")
                            if(typeUser=="Familiar")
                                continueFam(typeUser.toString(), genre, it.result?.user?.email ?: "")
                            if (typeUser=="Médico")
                                continueDoc(typeUser.toString(), genre, it.result?.user?.email ?: "")
                        } else{
                            Toast.makeText(this, "Campos incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, revise sus credenciales", Toast.LENGTH_SHORT).show()

            }


        }
    }

    private fun continuePat(typeUser: String, genre: Int, email:String){
        val continueRegIntent = Intent(this, EndRegister::class.java).apply {
            putExtra("typeUser", typeUser)
            putExtra("genre", genre)
            putExtra("email", email)
        }
        startActivity(continueRegIntent)
    }
    private fun continueFam(typeUser: String, genre: Int, email:String){
        val continueRegIntent = Intent(this, EndRegisterFam::class.java).apply {
            putExtra("typeUser", typeUser)
            putExtra("genre", genre)
            putExtra("email", email)
        }
        startActivity(continueRegIntent)
    }

    private fun continueDoc(typeUser: String, genre: Int, email:String){
        val continueRegIntent = Intent(this, EndRegisterDoc::class.java).apply {
            putExtra("typeUser", typeUser)
            putExtra("genre", genre)
            putExtra("email", email)
        }
        startActivity(continueRegIntent)
    }


}