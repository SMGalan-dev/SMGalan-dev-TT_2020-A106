package com.example.tt_a106_v0
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.tt_a106_v0.databinding.ActivityMainDoctorBinding
import com.google.firebase.auth.FirebaseAuth



class MainActivityDoctor : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainDoctorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMainActivityDoctor.toolbar)
        binding.appBarMainActivityDoctor.fab.setOnClickListener {
            val intoUserIntent = Intent(this, MainActivityFamiliar::class.java)
            startActivity(intoUserIntent)
        }
        /*binding.appBarMainActivityDoctor.fab.setOnClickListener { view ->
            Snackbar.make(view, "See notifications", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main_activity_doctor
        )
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_homeFamiliar, R.id.nav_Notifications), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val signoutMenuItem = binding.navView.menu.findItem(R.id.nav_logOut)
        signoutMenuItem.setOnMenuItemClickListener {
            logOutIntent()
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity_doctor
            , menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main_activity_doctor
        )
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun logOutIntent(){
        FirebaseAuth.getInstance().signOut()
        val intoUserIntent = Intent(this, AuthActivity::class.java)
        startActivity(intoUserIntent)
    }
}



