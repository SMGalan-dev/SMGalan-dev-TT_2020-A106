package com.example.tt_a106_v0

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class TestFragment : Fragment(R.layout.fragment_test) {
    /*
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test,  container, false)
    }

     */

    /*  //Internet 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_deudas, container, false)
        fabAgregarDeuda = view.findViewById(R.id.fabAgregarDeuda)
        fabAgregarDeuda.setOnClickListener {
            // Aquí código del click :)
        }
        return view
    }

     */
    // Internet 2
    private lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView=inflater.inflate(R.layout.fragment_test,container,false)
        val btn = mView.findViewById<Button>(R.id.testBtn)
        btn.setOnClickListener {
            Toast.makeText(activity, "Por favor, revise sus credenciales", Toast.LENGTH_SHORT).show()
        }
        return mView
    }

}