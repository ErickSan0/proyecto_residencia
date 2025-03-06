package com.example.combuapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.combuapp.R

class ReportesFragment : Fragment() {

    private lateinit var autocompletaEstacion: AutoCompleteTextView
    private lateinit var autocompletarProblema: AutoCompleteTextView
    private lateinit var descText: EditText
    private lateinit var btnGenerar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_reportes, container, false)

        autocompletaEstacion = root.findViewById(R.id.autocompletarestacion)
        autocompletarProblema = root.findViewById(R.id.autocompletararea)
        descText = root.findViewById(R.id.desctxt)
        btnGenerar = root.findViewById(R.id.btnGenerar)

        setupAutoCompleteFields(root)
        setupButtonListener()

        return root
    }

    private fun setupAutoCompleteFields(root: View) {
        val opcionesEstacion = resources.getStringArray(R.array.Estacion)
        val adapterEstacion = ArrayAdapter(requireContext(), R.layout.seleccionarray, opcionesEstacion)
        autocompletaEstacion.setAdapter(adapterEstacion)

        val opcionesArea = resources.getStringArray(R.array.area)
        val adapterArea = ArrayAdapter(requireContext(), R.layout.seleccionarray, opcionesArea)
        autocompletarProblema.setAdapter(adapterArea)
    }

    private fun setupButtonListener() {
        btnGenerar.setOnClickListener {
            val desc = descText.text.toString()
            val estacion = autocompletaEstacion.text.toString()
            val area = autocompletarProblema.text.toString()

            if (desc.isNotEmpty() && estacion.isNotEmpty() && area.isNotEmpty()) {
                descText.text.clear()
                autocompletaEstacion.text.clear()
                autocompletarProblema.text.clear()
                Toast.makeText(requireContext(), "Ticket generado exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Todos los campos deben estar llenos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}