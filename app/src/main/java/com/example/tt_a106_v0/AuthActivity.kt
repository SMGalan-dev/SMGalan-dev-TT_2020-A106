package com.example.tt_a106_v0

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_a106_v0.Users_register.MainRegisterActivity
import com.example.tt_a106_v0.patient_fragments.ForgotPasswordActivity
import com.example.tt_a106_v0.utils.CurrentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class AuthActivity : AppCompatActivity() {
    //SplashScreen
  //  private lateinit var binding: ActivityAuthBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(3000)
        setTheme(R.style.Theme_TT_A106_v0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        //  Setup
        setup()
    }

    private fun setup(){
    //    binding = ActivityAuthBinding.inflate(layoutInflater)
      //  setContentView(binding.root)

        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val logInButton = findViewById<Button>(R.id.logInButton)
        val radioGrupo = findViewById<RadioGroup>(R.id.radioGroup1)
        val Paciente = findViewById<RadioButton>(R.id.usePaciente)
        val Familiar = findViewById<RadioButton>(R.id.useFamiliar)
        val Doctor = findViewById<RadioButton>(R.id.useDoctor)
        val ForgotPssw = findViewById<TextView>(R.id.twForgotPassword)
        title = "Autenticación"
        var seleccionar: Int

        ForgotPssw.setOnClickListener {
            val psswIntent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(psswIntent)

        }

        signUpButton.setOnClickListener {
            val registerUserIntent = Intent(this, MainRegisterActivity::class.java)
            startActivity(registerUserIntent)
        }

            logInButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString()).addOnCompleteListener {
                    setUserDetails(emailEditText.text.toString())
                    if(it.isSuccessful){
                       if(Paciente.isChecked) {
                            val intoUserIntent = Intent(this, MainActivityPatient1::class.java)
                            startActivity(intoUserIntent)
                       }else if(Familiar.isChecked) {
                            val intoUserIntent = Intent(this, MainActivityFamiliar::class.java)
                            startActivity(intoUserIntent)
                        }else if (Doctor.isChecked){
                            val intoUserIntent = Intent(this, MainActivityDoctor::class.java)
                            startActivity(intoUserIntent)
                        }
                    } else{
                        showAlert()
                    }
                }
            }
        }
    }
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun setUserDetails(email:String){
            var hand = Handler(Looper.getMainLooper())
            hand.postDelayed(object : Runnable {
                override  fun run(){
                    db.collection("persons").document(email).get().addOnSuccessListener { data ->
                        if(data.exists()){
                            CurrentUser.id = data.id
                            CurrentUser.name = data.data?.get("name") as String
                            CurrentUser.lastName = data.data?.get("lastName") as String
                            CurrentUser.notifications = data.data?.get("notifications") as ArrayList<Any>
                        }
                    }
                    hand.postDelayed(this, 500)
                }
            }, 0)
    }


}