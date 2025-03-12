package com.example.combuapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.combuapp.R
import com.example.combuapp.adapters.UsuarioAdapter
import com.example.combuapp.models.Usuario
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class UsuariosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var usuarioAdapter: UsuarioAdapter
    private lateinit var database: DatabaseReference
    private val listaUsuarios = mutableListOf<Usuario>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_usuarios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        database = FirebaseDatabase.getInstance().reference.child("usuarios")
        usuarioAdapter = UsuarioAdapter(listaUsuarios) { usuarioSeleccionado ->
            abrirUsuarioBottomSheet(usuarioSeleccionado)
        }

        recyclerView.adapter = usuarioAdapter

        view.findViewById<FloatingActionButton>(R.id.btnAgregarUsuario).setOnClickListener {
            abrirUsuarioBottomSheet(null) // Para agregar un nuevo usuario
        }

        cargarUsuariosEnTiempoReal()
    }

    /**
     * 🔹 Escucha cambios en tiempo real en la lista de usuarios.
     */
    private fun cargarUsuariosEnTiempoReal() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nuevaLista = mutableListOf<Usuario>()
                for (usuarioSnapshot in snapshot.children) {
                    val usuario = usuarioSnapshot.getValue(Usuario::class.java)
                    usuario?.let {
                        it.id = usuarioSnapshot.key ?: ""
                        nuevaLista.add(it)
                    }
                }
                usuarioAdapter.actualizarLista(nuevaLista) // ✅ Ahora se actualiza correctamente
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error al cargar usuarios", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun abrirUsuarioBottomSheet(usuario: Usuario?) {
        val bottomSheet = UsuarioBottomSheet.newInstance(usuario) {
            cargarUsuariosEnTiempoReal() // 🔄 Actualizar la lista después de cambios
        }
        bottomSheet.show(parentFragmentManager, "UsuarioBottomSheet")
    }
}


