package com.example.tt_a106_v0.Users_register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_a106_v0.R
import com.example.tt_a106_v0.providerType


class MainRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_register)
        firstRegister()
    }

    private fun firstRegister() {
        val typeUser = findViewById<Spinner>(R.id.SpinnerUserType)
        val genero = findViewById<RadioGroup>(R.id.opciones_sexo)
        val email = findViewById<TextView>(R.id.emailEditText)
        val password = findViewById<EditText>(R.id.passwordEditText)
        val passwordcheck = findViewById<EditText>(R.id.passwordConfirmEditText)
        val cancelBtn = findViewById<Button>(R.id.cancelRegisterBtn)
        val continueRegBtn = findViewById<Button>(R.id.continueRegisterBtn)
        title = "Autenticación"

        cancelBtn.setOnClickListener {
            Log.d("lala", "cancelBtn.setOnClickListener")
            onBackPressed()
        }

        continueRegBtn.setOnClickListener {
            val size = typeUser.selectedItem.toString()
            Log.d(size, size.toString())
            Log.d("lala", "lalalalalalallalal")
            val texto: String = genero.toString()
            Log.d(texto, texto.toString())
            //onRadioButtonGenre()

            if (email.text.isNotEmpty() && password.text.isNotEmpty() && password.text.toString() == passwordcheck.text.toString()) {

                /*
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                        if(it.isSuccessful){
                            showHome(it.result?.user?.email ?: "", providerType.BASIC)
                        } else{
                            showAlert()
                        }
                    }*/
                /*
                    Timber.i("SUCCESS: Notify set to '%s' for %s", isNotifying, characteristic.getUuid())
                    Toast.makeText(this, "Continua el registro", Toast.LENGTH_SHORT).show()

                     */
            } else {
                Toast.makeText(this, "ERRRRRROR", Toast.LENGTH_SHORT).show()

            }


        }
    }

    private fun continueReg(typeUser: String, genero: String, email:String, provider: providerType){
        val continueRegIntent = Intent(this, EndRegister::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(continueRegIntent)
    }



    private fun onRadioButtonGenre(view: View): String {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_masculino ->
                    if (checked) {
                        // Pirates are the best
                        return "Masculino"
                    }
                R.id.radio_femenino ->
                    if (checked) {
                        // Ninjas rule
                        return "Femenino"
                    }
            }
        }
    }
}