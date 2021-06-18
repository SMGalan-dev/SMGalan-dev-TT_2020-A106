package com.example.tt_a106_v0.patient_fragments


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tt_a106_v0.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class DietFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    var user = Firebase.auth.currentUser
    private lateinit var mView: View
    lateinit var alertDialog: AlertDialog
    lateinit var storageReference: StorageReference
    var storage = Firebase.storage//
    lateinit var currentDate: String

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
        val person = user?.email.toString()
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
        return mView
    }




}