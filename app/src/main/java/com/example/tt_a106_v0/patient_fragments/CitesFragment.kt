package com.example.tt_a106_v0.patient_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.tt_a106_v0.R

class CitesFragment : Fragment() {
    private lateinit var mView: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_cites,container,false)
        val newMedicine = mView.findViewById<Button>(R.id.btnNewCite)
        newMedicine.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_Citas_to_addCiteFragment, null))
        return mView
    }
}