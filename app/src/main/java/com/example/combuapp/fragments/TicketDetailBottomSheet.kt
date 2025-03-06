package com.example.combuapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.combuapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TicketDetailBottomSheet(
    private val titulo: String,
    private val estado: String,
    private val fecha: String,
    private val descripcion: String
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottomsheet_ticket_detail, container, false)

        val txtTitulo = view.findViewById<TextView>(R.id.txt_ticket_titulo)
        val txtEstado = view.findViewById<TextView>(R.id.txt_ticket_estado)
        val txtFecha = view.findViewById<TextView>(R.id.txt_ticket_fecha)
        val txtDescripcion = view.findViewById<TextView>(R.id.txt_ticket_descripcion)
        val btnChat = view.findViewById<Button>(R.id.btn_abrir_chat)

        txtTitulo.text = titulo
        txtEstado.text = "Estado: $estado"
        txtFecha.text = "Fecha: $fecha"
        txtDescripcion.text = descripcion

        btnChat.setOnClickListener {





            dismiss()
        }

        return view
    }
}
