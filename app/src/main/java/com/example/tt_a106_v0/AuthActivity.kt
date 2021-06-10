package com.example.tt_a106_v0

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_a106_v0.Users_register.MainRegisterActivity
import com.example.tt_a106_v0.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {
    //SplashScreen
  //  private lateinit var binding: ActivityAuthBinding

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

        title = "Autenticación"
        var seleccionar: Int



        signUpButton.setOnClickListener {
            /*
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email ?: "", providerType.BASIC)
                    } else{
                        showAlert()
                    }
                }
            }*/
            val registerUserIntent = Intent(this, MainRegisterActivity::class.java)
            startActivity(registerUserIntent)
        }

            logInButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString()).addOnCompleteListener {
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



}