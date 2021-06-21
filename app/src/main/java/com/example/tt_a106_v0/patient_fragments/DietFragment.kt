package com.example.tt_a106_v0.patient_fragments


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_a106_v0.R
import com.example.tt_a106_v0.bleglucometer.DietsAdapter
import com.example.tt_a106_v0.bleglucometer.DietsDocsStructInDB
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class DietFragment : Fragment(), DietsAdapter.DietsAdapterListener {
    private val db = FirebaseFirestore.getInstance()
    var user = Firebase.auth.currentUser
    private val person = user?.email.toString()
    private lateinit var mView: View
    lateinit var alertDialog: AlertDialog
    lateinit var storageReference: StorageReference
    var storage = Firebase.storage//
    lateinit var currentDate: String
    private lateinit var adapter: DietsAdapter

    companion object{
        private val PICK_IMAGE_CODE = 1000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_CODE){
            //alertDialog.show()
            val uploadTask = storageReference!!.putFile(data!!.data!!)
            Toast.makeText(activity, "Procesando...", Toast.LENGTH_LONG).show()

            //Verificar carga
            uploadTask.continueWithTask{
                task ->
                if (!task.isSuccessful){
                    Toast.makeText(activity, "Carga fallida", Toast.LENGTH_SHORT).show()
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val downloadUri = task.result
                    val url = downloadUri!!.toString()
                    Log.d("URI", url)

                    db.collection("persons").document(user?.email.toString()).collection("patient").document("patientInfo").collection("dietsDocsRef").document(currentDate).set(
                        hashMapOf(
                            "date" to currentDate,
                            "url" to url
                        )
                    )

                    Toast.makeText(activity, "Carga con éxito", Toast.LENGTH_SHORT).show()
                    //alertDialog.dismiss()
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView=inflater.inflate(R.layout.fragment_diet,container,false)
        //alertDialog = SpotsDialog
        val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
        currentDate = sdf.format(Date())
        var pathref = String.format("%s/diets/%s", person, currentDate.toString())
        storageReference = FirebaseStorage.getInstance().getReference(pathref)

        val upDiet = mView.findViewById<Button>(R.id.btnUploadDiet)
        upDiet.setOnClickListener {
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "select Document"), PICK_IMAGE_CODE)
        }


        Log.e("query", "start")
        val query: Query = FirebaseFirestore.getInstance().collection("persons").document(person).collection("patient").document("patientInfo").collection("dietsDocsRef")

        // Inflate the layout for this fragment
        val recyclerView = mView.findViewById<RecyclerView>(R.id.rwDiets)
        adapter = DietsAdapter(query, this)
        recyclerView.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        Log.e("end", "ENDDIET")

        return mView
    }

    override fun onDietSelected(diet: DietsDocsStructInDB?) {
        //Toast.makeText(activity, "Descargando Dieta", Toast.LENGTH_SHORT).show()
        val uriStr = diet?.url.toString()
        //Log.e("onDietSelected", uriStr)
        val uriDiet = uriStr.toUri()
        val defaultBrowser =
        Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
        defaultBrowser.data = uriDiet

        val docID = diet?.date.toString()
        val builder = AlertDialog.Builder(activity as Context)
        builder.setTitle("Archivos de dietas")
        builder.setMessage("¿Que desea hacer con este documento?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Descargar"){dialogInterface, which ->
            startActivity(defaultBrowser)
        }
        //performing cancel action
        builder.setNeutralButton("Eliminar"){dialogInterface , which ->
            //Toast.makeText(activity as Context,"Operación cancelada",Toast.LENGTH_SHORT).show()
            FirebaseFirestore.getInstance().collection("persons").document(person).collection("patient").document("patientInfo").collection("dietsDocsRef").document(docID)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(activity as Context,"Archivo de dieta eliminado",Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { //e -> Log.w("TAG", "Error deleting document", e)
                    Toast.makeText(activity as Context,"Ha ocurrido un error, por favor inténtelo de nuevo más tarde",Toast.LENGTH_SHORT).show()}
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.startListening()
    }


}