package com.example.tt_a106_v0.patient_fragments

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_a106_v0.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TT_A106_v0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activityforgotpsswd)

        val rpsssw = findViewById<Button>(R.id.btnSendEmailRecoverPssw)
        rpsssw.setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmailForgotpssw)
            if (email.text.isEmpty()){
                Toast.makeText(this, "Ingrese una cuenta de correo", Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this, "Siga las instrucciones enviadas al correo para cambiar su contraseña", Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}