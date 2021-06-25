package com.example.tt_a106_v0.patient_fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tt_a106_v0.R
import com.example.tt_a106_v0.bleglucometer.DatePickerFragment
import com.example.tt_a106_v0.bleglucometer.TimePickerFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.util.*


class AddCiteFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    val user = Firebase.auth.currentUser
    private val person = user?.email.toString()
    private lateinit var mView: View
    private lateinit var title: String

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var hourOfDay: Int = 0
    private var minute: Int = 0

    private val callbackId = 42







    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    private val EVENT_PROJECTION: Array<String> = arrayOf(
        CalendarContract.Calendars._ID,                     // 0
        CalendarContract.Calendars.ACCOUNT_NAME,            // 1
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,   // 2
        CalendarContract.Calendars.OWNER_ACCOUNT            // 3
    )
/*

    // The indices for the projection array above.
    private const val PROJECTION_ID_INDEX: Int = 0
    private const val PROJECTION_ACCOUNT_NAME_INDEX: Int = 1
    private const val PROJECTION_DISPLAY_NAME_INDEX: Int = 2
    private const val PROJECTION_OWNER_ACCOUNT_INDEX: Int = 3

 */















    private fun asSyncAdapter(uri: Uri, account: String?, accountType: String?): Uri? {
        return uri.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account)
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType).build()
    }
















    @SuppressLint("LogNotTimber")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = Firebase.auth.currentUser
        mView=inflater.inflate(R.layout.fragment_add_cite,container,false)
        val date = mView.findViewById<EditText>(R.id.tvAddCiteDate)
        val time = mView.findViewById<EditText>(R.id.tvAddCiteTime)
        date.setOnClickListener { showDatePickerDialog() }
        time.setOnClickListener { showTimePickerDialog() }
        val title = mView.findViewById<EditText>(R.id.tvAddCiteTitle)
        val place = mView.findViewById<EditText>(R.id.tvAddCitePlace)
        val medic = mView.findViewById<EditText>(R.id.tvAddCiteMedic)
        val comment = mView.findViewById<EditText>(R.id.tvAddCiteComment)
        //val citeID = String.format("%s %s", date.text.toString(), time.text.toString())


















        val newCite = mView.findViewById<TextView>(R.id.btnAddNewCite)
        newCite.setOnClickListener {
            if (date.text.isNotEmpty() && time.text.isNotEmpty()){
                val dateF = String.format("%02d-%02d-%02d", day, month, year)
                val timeF = String.format("%02d:%02d", hourOfDay, minute)
                val citeID = String.format("%s %s", dateF, timeF)

                db.collection("persons").document(person).collection("patient").document("patientInfo").collection("citesRegister").document(citeID).set(
                    hashMapOf(
                        "title" to title.text.toString(),
                        "date" to dateF,
                        "time" to timeF,
                        "place" to place.text.toString(),
                        "medic" to medic.text.toString(),
                        "comment" to comment.text.toString()
                    )
                )

/*

                val uri = asSyncAdapter(
                    //CalendarContract.Calendars.CONTENT_URI,
                    CalendarContract.Events.CONTENT_URI,
                    getGoogleAccount().toString(),
                    CalendarContract.ACCOUNT_TYPE_LOCAL,
                )
                Log.e("URI", uri.toString())


                var target = Uri.parse(CalendarContract.Events.CONTENT_URI.toString())
                target = target.buildUpon()
                    .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, getGoogleAccount().toString())
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                    .appendQueryParameter(CalendarContract.Events.ACCOUNT_NAME,  getGoogleAccount().toString() )
                    .appendQueryParameter(CalendarContract.Events.ACCOUNT_TYPE,  CalendarContract.ACCOUNT_TYPE_LOCAL )
                    //.appendQueryParameter(CalendarContract.Events.SYNC_EVENTS, 1)
                    .build()
                Log.e("URItarget", target.toString())
 */
                var target = Uri.parse(CalendarContract.Events.CONTENT_URI.toString())
                target = target.buildUpon()
                    .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                    .appendQueryParameter(CalendarContract.SyncState.ACCOUNT_NAME, "getGoogleAccount().toString()")
                    .appendQueryParameter(CalendarContract.SyncState.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                    //.appendQueryParameter(CalendarContract.Events.SYNC_EVENTS, 1)
                    .build()
                Log.e("URItarget", target.toString())




                val uri = asSyncAdapter(
                    CalendarContract.Calendars.CONTENT_URI,
                    getGoogleAccount().toString(),
                    CalendarContract.ACCOUNT_TYPE_LOCAL,
                )
                Log.e("URI", uri.toString())





















                val calID: Long = 3
                val beginTime: Long = Calendar.getInstance().run {
                    set(year, month, day, hourOfDay, minute)
                    timeInMillis
                }
                val endTime: Long = Calendar.getInstance().run {
                    set(year, month, day, hourOfDay+1, minute)
                    timeInMillis
                }

                val values = ContentValues().apply {
                    put(CalendarContract.Events.DTSTART, beginTime)
                    put(CalendarContract.Events.DTEND, endTime)
                    put(CalendarContract.Events.TITLE, "Jazzercise")
                    put(CalendarContract.Events.DESCRIPTION, "Group workout")
                    put(CalendarContract.Events.CALENDAR_ID, calID)
                    put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles")
                    //put(CalendarContract.Events.ACCOUNT_TYPE,  CalendarContract.ACCOUNT_TYPE_LOCAL ))
                    put(CalendarContract.Events.ORGANIZER, getGoogleAccount().toString())
                    put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                    //put(CalendarContract.Events.ACCOUNT_NAME,  getGoogleAccount().toString() )  //java.lang.IllegalArgumentException: Only the provider may write
                    // put(CalendarContract.Events.SYNC_EVENTS, 1)                          //java.lang.IllegalArgumentException: Only the provider may write to sync_events
                    //put(CalendarContract.Events.VISIBLE, 1)   // java.lang.IllegalArgumentException: Only the provider may write to visible
                }
                //val uri: Uri? = context?.contentResolver?.insert(CalendarContract.Events.CONTENT_URI, values)
                //val uri: Uri? = context?.contentResolver?.insert(uri!!, values)
                Log.e("CalendarContract", CalendarContract.Events.CONTENT_URI.toString())

                /*
                    val asd = context?.contentResolver?.query(target,
                        arrayOf(CalendarContract.Calendars._ID), null, null, null);
                Log.e("URIasd", asd.toString())
                 */

                try {
                    val insertUri=  context?.contentResolver?.insert(target, values)
                    Log.e("URIasd", insertUri.toString())
                    // get the event ID that is the last element in the Uri
                    val eventID= insertUri?.lastPathSegment?.toLong()
                    Toast.makeText(activity, "${eventID.toString()}", Toast.LENGTH_SHORT).show()
                }catch (e: IOException){
                    e.printStackTrace()
                    Toast.makeText(activity, "Conceda los permisos para imgresar consultas", Toast.LENGTH_SHORT).show()
                }



                //Toast.makeText(activity, "Cita creada con éxito", Toast.LENGTH_SHORT).show()




            }else{
                Toast.makeText(activity, "Seleccione una fecha y hora", Toast.LENGTH_SHORT).show()
            }

        }


        // Inflate the layout for this fragment
        return mView
    }

    private fun getGoogleAccount() = GoogleSignIn.getLastSignedInAccount(activity as Context)


    private fun showDatePickerDialog() {
        val datePicker= DatePickerFragment{ day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(activity?.supportFragmentManager!!, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int)  {
        mView.findViewById<EditText>(R.id.tvAddCiteDate).setText("$day-$month-$year")
        this.day = day
        this.month = month
        this.year = year
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { hourOfDay, minute ->  onTimeSelected(hourOfDay, minute)}
        timePicker.show(activity?.supportFragmentManager!!, "datePicker")
    }

    private fun onTimeSelected(hourOfDay: Int, minute: Int) {
        mView.findViewById<EditText>(R.id.tvAddCiteTime).setText("$hourOfDay:$minute")
        this.hourOfDay = hourOfDay
        this.minute = minute

    }

}

/*
    @SuppressLint("LogNotTimber")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = Firebase.auth.currentUser
        mView=inflater.inflate(R.layout.fragment_add_cite,container,false)
        val date = mView.findViewById<EditText>(R.id.tvAddCiteDate)
        val time = mView.findViewById<EditText>(R.id.tvAddCiteTime)
        date.setOnClickListener { showDatePickerDialog() }
        time.setOnClickListener { showTimePickerDialog() }
        val title = mView.findViewById<EditText>(R.id.tvAddCiteTitle)
        val place = mView.findViewById<EditText>(R.id.tvAddCitePlace)
        val medic = mView.findViewById<EditText>(R.id.tvAddCiteMedic)
        val comment = mView.findViewById<EditText>(R.id.tvAddCiteComment)
        //val citeID = String.format("%s %s", date.text.toString(), time.text.toString())

        val newCite = mView.findViewById<TextView>(R.id.btnAddNewCite)
        newCite.setOnClickListener {
            if (date.text.isNotEmpty() && time.text.isNotEmpty()){
                val dateF = String.format("%02d-%02d-%02d", day, month, year)
                val timeF = String.format("%02d:%02d", hourOfDay, minute)
                val citeID = String.format("%s %s", dateF, timeF)

                db.collection("persons").document(person).collection("patient").document("patientInfo").collection("citesRegister").document(citeID).set(
                    hashMapOf(
                        "title" to title.text.toString(),
                        "date" to dateF,
                        "time" to timeF,
                        "place" to place.text.toString(),
                        "medic" to medic.text.toString(),
                        "comment" to comment.text.toString()
                    )
                )


                val intent = Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI).apply {
                    val beginTime: Calendar = Calendar.getInstance().apply {
                        set(year, month, day, hourOfDay, minute)
                    }
                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
                    putExtra(CalendarContract.Events.CAN_INVITE_OTHERS, medic.text.toString())
                    putExtra(CalendarContract.Events.TITLE, title.text.toString())
                    putExtra(CalendarContract.Events.DESCRIPTION, comment.text.toString())
                    putExtra(CalendarContract.Events.EVENT_LOCATION, place.text.toString());
                    putExtra(CalendarContract.Events.HAS_ALARM, 1)
                }
                startActivity(intent)

                //Toast.makeText(activity, "Cita creada con éxito", Toast.LENGTH_SHORT).show()



            }else{
                Toast.makeText(activity, "Seleccione una fecha y hora", Toast.LENGTH_SHORT).show()
            }

        }


        // Inflate the layout for this fragment
        return mView
    }
 */