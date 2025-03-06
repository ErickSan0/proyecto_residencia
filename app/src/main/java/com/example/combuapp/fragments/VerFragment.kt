package com.example.combuapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.combuapp.R
import com.example.combuapp.adapters.Ticket
import com.example.combuapp.adapters.TicketAdapter

class VerFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ticketAdapter: TicketAdapter
    private val listaTickets = mutableListOf<Ticket>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ver, container, false)

        recyclerView = view.findViewById(R.id.recycler_tickets)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val btnFiltrar: Button = view.findViewById(R.id.btn_filtrar)
        btnFiltrar.setOnClickListener {



        }

        cargarTickets()

        return view
    }

    private fun cargarTickets() {
        listaTickets.clear()

        listaTickets.add(Ticket("Fuga de Gasolina", "Abierto", "12/03/2025", "Hay una fuga en la estación 3."))
        listaTickets.add(Ticket("Falla en Bomba", "En Proceso", "13/03/2025", "La bomba de la estación 1 está fallando."))
        listaTickets.add(Ticket("Problema Eléctrico", "Cerrado", "10/03/2025", "Cortocircuito en la estación 2."))

        ticketAdapter = TicketAdapter(listaTickets, requireActivity())
        recyclerView.adapter = ticketAdapter
    }

}
