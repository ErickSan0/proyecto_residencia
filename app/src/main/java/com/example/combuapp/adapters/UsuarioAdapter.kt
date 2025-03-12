package com.example.combuapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.combuapp.R
import com.example.combuapp.models.Usuario

class UsuarioAdapter(
    private var listaUsuarios: MutableList<Usuario>, // ✅ Ahora es mutable
    private val onUsuarioClick: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreUsuario: TextView = view.findViewById(R.id.txtNombreUsuario)
        val apellidoUsuario: TextView = view.findViewById(R.id.txtApellidoUsuario)
        val rolUsuario: TextView = view.findViewById(R.id.txtRolUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usuario_formato, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = listaUsuarios[position]
        holder.nombreUsuario.text = usuario.nombre
        holder.apellidoUsuario.text = usuario.apellido
        holder.rolUsuario.text = usuario.rol

        holder.itemView.setOnClickListener {
            onUsuarioClick(usuario)
        }
    }

    override fun getItemCount(): Int = listaUsuarios.size

    /**
     * 🔄 Método para actualizar la lista de usuarios cuando Firebase cambia.
     */
    fun actualizarLista(nuevaLista: List<Usuario>) {
        listaUsuarios.clear()
        listaUsuarios.addAll(nuevaLista)
        notifyDataSetChanged() // 🔄 Refrescar el RecyclerView
    }
}

