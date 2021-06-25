package com.example.tt_a106_v0
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tt_a106_v0.databinding.ActivityMainPatient1Binding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth



class MainActivityPatient1 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var binding: ActivityMainPatient1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMainPatient1Binding.inflate(layoutInflater)
    setContentView(binding.root)

        setSupportActionBar(binding.appBarMainActivityPatient1.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main_activity_patient1)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_homePatient, R.id.nav_dispGlucometer, R.id.nav_familiar,R.id.nav_Medicos), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val signoutMenuItem = binding.navView.menu.findItem(R.id.nav_logOut)
        signoutMenuItem.setOnMenuItemClickListener {
            logOutIntent()
            true
        }
        binding.appBarMainActivityPatient1.circleImage.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            val menuIntent = Intent(this, MainActivityPatient1::class.java)
            startActivity(menuIntent)
        }

        reqPermission()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity_patient1, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main_activity_patient1)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun logOutIntent(){
        FirebaseAuth.getInstance().signOut()
        val intoUserIntent = Intent(this, AuthActivity::class.java)
        startActivity(intoUserIntent)
    }


    private fun reqPermission() {

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_CALENDAR) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_CALENDAR)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf( android.Manifest.permission.WRITE_CALENDAR), 1)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf( android.Manifest.permission.WRITE_CALENDAR), 1)
            }
        }
        /*
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CALENDAR) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_CALENDAR)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf( android.Manifest.permission.READ_CALENDAR), 1)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf( android.Manifest.permission.READ_CALENDAR), 1)
            }
        }
         */
    }
}






