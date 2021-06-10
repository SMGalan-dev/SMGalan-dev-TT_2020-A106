package com.example.tt_a106_v0.ui.home

import HomeFamiliarViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tt_a106_v0.R
import com.example.tt_a106_v0.databinding.FragmentHomeFamiliarBinding

class HomeFamiliarFragment : Fragment() {

    private lateinit var homeFamiliarViewModel: HomeFamiliarViewModel
    private var _binding: FragmentHomeFamiliarBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeFamiliarViewModel = ViewModelProvider(this).get(HomeFamiliarViewModel::class.java)
        _binding = FragmentHomeFamiliarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        /* val textView: TextView = binding.textHome
         homeFamiliarViewModel.text.observe(viewLifecycleOwner, Observer {
           textView.text = it
         })*/
        return root
    }
/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.menuPerfilFamiliar.setOnClickListener {
            findNavController().navigate(R.id.action_nav_homePatient_to_nav_Perfil)
        }
        binding.menuCitas.setOnClickListener {
            findNavController().navigate(R.id.action_nav_homePatient_to_nav_Citas)
            //Toast.makeText(activity, "Datos actualizados", Toast.LENGTH_SHORT).show()
        }
        binding.menuMedicamentos.setOnClickListener {
            findNavController().navigate(R.id.action_nav_homePatient_to_nav_Medicamentos)
            //Toast.makeText(activity, "Datos actualizados", Toast.LENGTH_SHORT).show()
        }
        binding.menuDietas.setOnClickListener {
            findNavController().navigate(R.id.action_nav_homePatient_to_nav_Dieta)
            //Toast.makeText(activity, "Datos actualizados", Toast.LENGTH_SHORT).show()
        }
        binding.menuGlucosa.setOnClickListener {
            findNavController().navigate(R.id.action_nav_homePatient_to_nav_Glucosa)
            //Toast.makeText(activity, "Datos actualizados", Toast.LENGTH_SHORT).show()
        }
    }
*/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}