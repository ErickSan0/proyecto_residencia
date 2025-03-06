package com.example.combuapp.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.combuapp.R
import com.example.combuapp.fragments.TicketDetailBottomSheet

data class Ticket(val titulo: String, val estado: String, val fecha: String, val descripcion: String)

class TicketAdapter(private val tickets: List<Ticket>, private val activity: FragmentActivity) :
    RecyclerView.Adapter<TicketAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txt_titulo)
        val estado: TextView = view.findViewById(R.id.txt_estado)
        val fecha: TextView = view.findViewById(R.id.txt_fecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket_formato, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.titulo.text = ticket.titulo
        holder.estado.text = "Estado: ${ticket.estado}"
        holder.fecha.text = "Fecha: ${ticket.fecha}"

        holder.itemView.setOnClickListener {
            val bottomSheet = TicketDetailBottomSheet(
                ticket.titulo, ticket.estado, ticket.fecha, ticket.descripcion
            )
            bottomSheet.show(activity.supportFragmentManager, "TicketDetail")
        }
    }

    override fun getItemCount() = tickets.size
}
