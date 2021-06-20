package com.example.tt_a106_v0.patient_fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_a106_v0.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit



class FitApiActivity : AppCompatActivity(){
    private val db = FirebaseFirestore.getInstance()
    val user = Firebase.auth.currentUser
    val person = user?.email.toString()
    private var GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1 //whatever you want
    //private var dataPointListener: OnDataPointListener? = null



    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_ACTIVITY_SEGMENT)
        .build()

    @InternalCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitapi)

        val myJob: Job = startRepeatingJob(2000L)
        val btn = findViewById<Button>(R.id.buttontestfit)
        btn.setOnClickListener {
            Log.e("GOOGLE_FIT", "HeartRateScanSTOPED")
            myJob.cancel()
            Toast.makeText(this, "Monitoreo de pulso detenido", Toast.LENGTH_SHORT).show()
        }
        connectToGoogleFit()

        // To Stop:
        // myJob.cancel()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @InternalCoroutinesApi
    private fun startRepeatingJob(timeInterval: Long): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive) {
                // add your task here
                connectToGoogleFit()
                delay(timeInterval)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun connectToGoogleFit() {
        //Log.e("GOOGLE_FIT", "google fit init")
        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
        if (!GoogleSignIn.hasPermissions(
                account,
                fitnessOptions
            )
        ) {
            Log.e("GOOGLE_FIT", "no permission")
            GoogleSignIn.requestPermissions(
                this, // your activity
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                account,
                fitnessOptions
            )
        } else {
            //Log.d("GOOGLE_FIT", "has permission")
            accessGoogleFit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("GOOGLE_FIT", "connection success")
                accessGoogleFit()
            } else {
                Log.d("GOOGLE_FIT", "connection failed")
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun accessGoogleFit() {
        //Log.e("GOOGLE_FIT", "Heartratescan")
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val startTime = endTime.minusMinutes(10)                                              //MODIFICAR INTERVALO AL MINIMO
        //val startTime = endTime.minusWeeks(1)
        //Log.e("accessGoogleFit1", "Range Start: $endTime")
        //Log.e("accessGoogleFit2", "Range End  : $endTime")

        val readRequest =
            DataReadRequest.Builder()
                .aggregate(DataType.TYPE_HEART_RATE_BPM)
                .bucketByTime(1, TimeUnit.MINUTES)
                .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
                .build()

        Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
            .readData(readRequest)
            .addOnSuccessListener { response ->
                // The aggregate query puts datasets into buckets, so flatten into a
                // single list of datasets
                for (dataSet in response.buckets.flatMap { it.dataSets }) {
                    dumpDataSet(dataSet)
                }
            }
            .addOnFailureListener { e ->
                Log.e("accessGoogleFit", "addOnFailureListener", e)
                //Log.w(TAG,"There was an error reading data from Google Fit", e)
            }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("LogNotTimber")
    fun dumpDataSet(dataSet: DataSet) {
        //Log.i("dumpDataSet", "Data returned dumpDataSet")
        for (dp in dataSet.dataPoints) {
            val dateID = String.format("%16.16s", dp.getEndTimeString())
            val dateR = String.format("%10.10s", dateID)
            val timeR = dateID.substring(dateID.length - 5)
            /*

            Log.i("dumpDataSet","Data point:")
            Log.i("dumpDataSet","\tType: ${dp.dataType.name}")
            Log.i("dumpDataSet","\tStart: ${dp.getStartTimeString()}")
            Log.i("dumpDataSet","\tEnd: ${dp.getEndTimeString()}")
             */
            for (field in dp.dataType.fields) {
                //Log.e("dumpDatasetField","\tField: ${field.name.toString()} Value: ${dp.getValue(field)}") //field.name can be [averagre], [max], [min]
                val heartRateV = dp.getValue(field).asFloat()
                Log.e("HEART_RATE_","${heartRateV.toInt().toFloat()}ppm at $dateR $timeR") //field.name can be [averagre], [max], [min]
                if(field.name.toString() == "average" && (80>heartRateV) && (heartRateV>130)){                         //Definir intervalo alerta
                    Log.e("HEART_RATE_NOTIFICATION","Register ${heartRateV.toInt().toFloat()}ppm at $dateR $timeR") //field.name can be [averagre], [max], [min]
                    db.collection("persons").document(person).collection("patient").document("patientInfo").collection("heartRateEvent").document(dateID).set(
                        hashMapOf(
                            "date" to dateR,
                            "time" to timeR,
                            "ppm" to heartRateV.toInt().toFloat().toString(),
                            "unit" to "ppm"
                        )
                    )
                }
            }
        }
    }

    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

    @RequiresApi(Build.VERSION_CODES.O)
    fun DataPoint.getStartTimeString() = Instant.ofEpochSecond(this.getStartTime(TimeUnit.SECONDS))
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime().toString()

    @RequiresApi(Build.VERSION_CODES.O)
    fun DataPoint.getEndTimeString() = Instant.ofEpochSecond(this.getEndTime(TimeUnit.SECONDS))
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime().toString()
}