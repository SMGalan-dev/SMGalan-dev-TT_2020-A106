package com.example.tt_a106_v0.Users_register

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_a106_v0.R

class MainRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_register)
        firstRegister()
    }

    private fun firstRegister(){
        val typeUser = findViewById<Spinner>(R.id.SpinnerUserType)
        val genero = findViewById<RadioGroup>(R.id.opciones_sexo)
        val email = findViewById<TextView>(R.id.emailEditText)
        val password = findViewById<EditText>(R.id.passwordEditText)
        val passwordcheck = findViewById<EditText>(R.id.passwordConfirmEditText)
        val cancelBtn = findViewById<Button>(R.id.cancelRegisterBtn)
        val continueRegBtn = findViewById<Button>(R.id.continueRegisterBtn)
        title = "Autenticación"

        cancelBtn.setOnClickListener {
            onBackPressed()
        }

        continueRegBtn.setOnClickListener {

                if (email.text.isNotEmpty() && password.text.isNotEmpty() && password.text.toString() == passwordcheck.text.toString()){
                    /*
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                        if(it.isSuccessful){
                            showHome(it.result?.user?.email ?: "", providerType.BASIC)
                        } else{
                            showAlert()
                        }
                    }*/
                    Toast.makeText(this, "Continua el registro", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "ERRRRRROR", Toast.LENGTH_SHORT).show()

                }


        }
    }
}