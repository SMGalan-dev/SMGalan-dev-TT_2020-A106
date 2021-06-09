package com.example.tt_a106_v0

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TestFragment : Fragment(R.layout.fragment_test) {
    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var user = Firebase.auth.currentUser
        mView=inflater.inflate(R.layout.fragment_test,container,false)

        val update = mView.findViewById<Button>(R.id.testBtn)
        update.setOnClickListener {
            if (user != null) {
                Toast.makeText(activity, user.email.toString(), Toast.LENGTH_SHORT).show()
            } else {
                // No user is signed in
                Toast.makeText(activity, "No hay sesion activa", Toast.LENGTH_SHORT).show()
            }
        }
        return mView
    }
}