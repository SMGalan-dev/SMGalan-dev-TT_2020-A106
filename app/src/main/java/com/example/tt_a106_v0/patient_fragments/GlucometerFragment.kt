package com.example.tt_a106_v0.patient_fragments

//import com.example.blessed.BluetoothCentralManager
//import com.example.blessed.BluetoothPeripheral

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_a106_v0.MainActivityPatient1
import com.example.tt_a106_v0.R
import com.example.tt_a106_v0.bleglucometer.BluetoothHandler
import com.example.tt_a106_v0.bleglucometer.GlucoseMeasurement
import com.example.tt_a106_v0.bleglucometer.GlucoseMeasurementUnit
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.BluetoothPeripheral
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GlucometerFragment : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private var measurementValue: TextView? = null
    private val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_glucometer)
        measurementValue = findViewById<View>(R.id.searchingDispText) as? TextView
        registerReceiver(locationServiceStateReceiver, IntentFilter(LocationManager.MODE_CHANGED_ACTION))
        registerReceiver(glucoseDataReceiver, IntentFilter(BluetoothHandler.MEASUREMENT_GLUCOSE))



        val intoUserIntent = Intent(this, MainActivityPatient1::class.java)
        val cancelRegDispData = findViewById<Button>(R.id.cancelGlucbtn)
        cancelRegDispData.setOnClickListener{
            Toast.makeText(this, "Guardado cancelado", Toast.LENGTH_SHORT).show()
            startActivity(intoUserIntent)

        }

    }

    private fun saveMeasurementData(measurement: String, unit: String, date: String, disp: String){
        var user = Firebase.auth.currentUser

        val glucoseLevel = String.format("%.1f", measurement)
        val unit = unit
        val device = disp
        val date = date
        val intoUserIntent = Intent(this, MainActivityPatient1::class.java)
        val Terminate = findViewById<Button>(R.id.saveGlucbtn)

        Terminate.setOnClickListener {
            Toast.makeText(this, "Datos Guardados", Toast.LENGTH_SHORT).show()
            db.collection("persons").document(user.email.toString()).collection("patient").document("patientInfo").collection("glucoseTestRecords").document(date).set(
                hashMapOf(
                    "glucoseLevel" to glucoseLevel,
                    "unit" to unit,
                    "device" to device,
                    "date" to date
                )
            )
            /*
            db.collection("users").document(user.email.toString()).collection("glucoseTestRecords").document(date).set(
                hashMapOf(
                    "glucoseLevel" to glucoseLevel,
                    "unit" to unit,
                    "device" to device,
                    "date" to date
                )
            )
             */
            startActivity(intoUserIntent)
        }

    }


    override fun onResume() {
        super.onResume()
        if (BluetoothAdapter.getDefaultAdapter() != null) {
            if (!isBluetoothEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                checkPermissions()
            }
        } else {
            Timber.e("This device has no Bluetooth hardware")
        }
    }

    private val isBluetoothEnabled: Boolean
        get() {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    ?: return false
            return bluetoothAdapter.isEnabled
        }

    private fun initBluetoothHandler() {
        BluetoothHandler.getInstance(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(locationServiceStateReceiver)
        unregisterReceiver(glucoseDataReceiver)
    }

    private val locationServiceStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null && action == LocationManager.MODE_CHANGED_ACTION) {
                val isEnabled = areLocationServicesEnabled()
                Timber.i("Location service state changed to: %s", if (isEnabled) "on" else "off")
                checkPermissions()
            }
        }
    }
    private val glucoseDataReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val peripheral = getPeripheral(intent.getStringExtra(BluetoothHandler.MEASUREMENT_EXTRA_PERIPHERAL))
            val measurement: GlucoseMeasurement? = intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_GLUCOSE_EXTRA) as GlucoseMeasurement?
            if (measurement != null) {
                measurementValue!!.text = java.lang.String.format(Locale.ENGLISH, "%.1f %s\n%s\n\nfrom %s", measurement.value, if (measurement.unit === GlucoseMeasurementUnit.MmolPerLiter) "mmol/L" else "mg/dL", dateFormat.format(measurement.timestamp), peripheral.name)
                //Log.e("MMMMMMMMMMMMMMMMEEEEsuu",measurementValue!!.text.toString() )
                val measurementV = measurement.value.toString()
                val unitM = if (measurement.unit === GlucoseMeasurementUnit.MmolPerLiter) "mmol/L" else "mg/dL"
                val dateM = dateFormat.format(measurement.timestamp).toString()
                val dispM = peripheral.name.toString()
                Log.d("MMMMMMMMMMMMMMMMEEEmeas", measurementV   )
                Log.d("MMMMMMMMMMMMMMMMEEEunit", unitM   )
                Log.d("MMMMMMMMMMMMMMMMEEEdate", dateM   )
                Log.d("MMMMMMMMMMMMMMMMEEEdisp", dispM   )
                saveMeasurementData(measurementV, unitM, dateM, dispM)
            }
        }
    }

    private fun getPeripheral(peripheralAddress: String?): BluetoothPeripheral {
        val central: BluetoothCentralManager = BluetoothHandler.getInstance(applicationContext).central
        return central.getPeripheral(peripheralAddress!!)
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val missingPermissions = getMissingPermissions(requiredPermissions)
            if (missingPermissions.isNotEmpty()) {
                requestPermissions(missingPermissions, ACCESS_LOCATION_REQUEST)
            } else {
                permissionsGranted()
            }
        }
    }

    private fun getMissingPermissions(requiredPermissions: Array<String>): Array<String> {
        val missingPermissions: MutableList<String> = ArrayList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (requiredPermission in requiredPermissions) {
                if (applicationContext.checkSelfPermission(requiredPermission) != PackageManager.PERMISSION_GRANTED) {
                    missingPermissions.add(requiredPermission)
                }
            }
        }
        return missingPermissions.toTypedArray()
    }

    private val requiredPermissions: Array<String>
        get() {
            val targetSdkVersion = applicationInfo.targetSdkVersion
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && targetSdkVersion >= Build.VERSION_CODES.Q) arrayOf(Manifest.permission.ACCESS_FINE_LOCATION) else arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

    private fun permissionsGranted() {
        // Check if Location services are on because they are required to make scanning work
        if (checkLocationServices()) {
            initBluetoothHandler()
        }
    }

    private fun areLocationServicesEnabled(): Boolean {
        val locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager == null) {
            Timber.e("could not get location manager")
            return false
        }
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return isGpsEnabled || isNetworkEnabled
    }

    private fun checkLocationServices(): Boolean {
        return if (!areLocationServicesEnabled()) {
            AlertDialog.Builder(this@GlucometerFragment)
                    .setTitle("Location services are not enabled")
                    .setMessage("Scanning for Bluetooth peripherals requires locations services to be enabled.") // Want to enable?
                    .setPositiveButton("Enable") { dialogInterface, i ->
                        dialogInterface.cancel()
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                    .setNegativeButton("Cancel") { dialog, which -> // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel()
                    }
                    .create()
                    .show()
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Check if all permission were granted
        var allGranted = true
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                allGranted = false
                break
            }
        }
        if (allGranted) {
            permissionsGranted()
        } else {
            AlertDialog.Builder(this@GlucometerFragment)
                    .setTitle("Location permission is required for scanning Bluetooth peripherals")
                    .setMessage("Please grant permissions")
                    .setPositiveButton("Retry") { dialogInterface, i ->
                        dialogInterface.cancel()
                        checkPermissions()
                    }
                    .create()
                    .show()
        }
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        private const val ACCESS_LOCATION_REQUEST = 2
    }
}
