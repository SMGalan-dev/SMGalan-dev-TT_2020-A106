package com.example.tt_a106_v0.patient_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.tt_a106_v0.R


class MedicationFragment : Fragment() {
    private lateinit var mView: View
    private lateinit var mView2: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_medication,container,false)
        mView2=inflater.inflate(R.layout.fragment_add_medicine,container,false)
        val newMedicine = mView.findViewById<Button>(R.id.newMedicamentBtn)
        newMedicine.setOnClickListener {
     /*
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.nav_host_fragment_content_main_activity_patient1, R.layout,fragment_)
            transaction?.commit()

      */
            val transaction2 = activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.nav_host_fragment_container, AddMedicineFragment())
                commit()
            }

        }
        return inflater.inflate(R.layout.fragment_medication, container, false)
    }

}