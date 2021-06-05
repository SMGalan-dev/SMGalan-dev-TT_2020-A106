package com.example.tt_a106_v0

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tt_a106_v0.patient_fragments.DoctorsFragment
import com.example.tt_a106_v0.patient_fragments.FamiliarsFragment
import com.example.tt_a106_v0.patient_fragments.GlucometerFragment
import com.example.tt_a106_v0.patient_fragments.HomeFragmentPatient
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityPatient : AppCompatActivity() {

    private lateinit var toogle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_patient)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutPatient) //Activity_main_patient
        toogle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()  //Sincroniza entre el icono del menu y el panel de navegacion

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navView: NavigationView = findViewById(R.id.nav_viewPatient)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_homePatient -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView_patient, HomeFragmentPatient())
                        commit()
                    }
                }
                R.id.nav_dispGlucometer -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView_patient, GlucometerFragment())
                        commit()
                    }
                }
                R.id.nav_familiar -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView_patient, FamiliarsFragment())
                        commit()
                    }
                }
                R.id.nav_Medicos -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView_patient, DoctorsFragment())
                        commit()
                    }
                }
                R.id.nav_logOut -> {
                    logOutIntent()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    //Funcione el icono del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun logOutIntent(){
            FirebaseAuth.getInstance().signOut()
            val intoUserIntent = Intent(this, AuthActivity::class.java)
            startActivity(intoUserIntent)
    }


}