package com.example.combuapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.example.combuapp.R
import com.example.combuapp.models.Usuario
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.functions.FirebaseFunctions

class UsuarioBottomSheet : BottomSheetDialogFragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    private lateinit var tituloTextView: TextView
    private lateinit var edtNombre: EditText
    private lateinit var edtApellido: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var layoutPassword: View
    private lateinit var edtTelefono: EditText
    private lateinit var spinnerRol: AutoCompleteTextView
    private lateinit var btnGuardar: Button
    private lateinit var btnEliminar: Button

    private var usuario: Usuario? = null
    private var onUsuarioActualizado: (() -> Unit)? = null

    companion object {
        fun newInstance(usuario: Usuario?, onUsuarioActualizado: () -> Unit): UsuarioBottomSheet {
            val fragment = UsuarioBottomSheet()
            fragment.usuario = usuario
            fragment.onUsuarioActualizado = onUsuarioActualizado
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_editar_usuario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        functions = FirebaseFunctions.getInstance()

        tituloTextView = view.findViewById(R.id.txtexto)
        edtNombre = view.findViewById(R.id.et_nombre)
        edtApellido = view.findViewById(R.id.et_apellido)
        edtEmail = view.findViewById(R.id.et_correo)
        edtTelefono = view.findViewById(R.id.et_telefono)
        edtPassword = view.findViewById(R.id.et_password)
        layoutPassword = view.findViewById(R.id.layoutPassword)
        spinnerRol = view.findViewById(R.id.autocompletarol)
        btnGuardar = view.findViewById(R.id.btnGuardarUsuario)
        btnEliminar = view.findViewById(R.id.btnEliminarUsuario)

        val roles = listOf("Encargado", "Administrador")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, roles)
        spinnerRol.setAdapter(adapter)

        if (usuario == null) {
            tituloTextView.text = "Registro de usuario"
            layoutPassword.isVisible = true
            btnEliminar.isVisible = false
        } else {
            tituloTextView.text = "Editar usuario"
            usuario?.let { user ->
                edtNombre.setText(user.nombre)
                edtApellido.setText(user.apellido)
                edtEmail.setText(user.email)
                edtTelefono.setText(user.telefono)
                spinnerRol.setText(user.rol, false)
            }
            layoutPassword.isVisible = false
            btnEliminar.isVisible = true
        }

        btnGuardar.setOnClickListener { guardarUsuario() }
        btnEliminar.setOnClickListener { confirmarEliminacion() }
    }

    private fun guardarUsuario() {
        val nombre = edtNombre.text.toString().trim()
        val apellido = edtApellido.text.toString().trim()
        val correo = edtEmail.text.toString().trim()
        val telefono = edtTelefono.text.toString().trim()
        val rol = spinnerRol.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (nombre.isEmpty() || correo.isEmpty()) {
            Toast.makeText(requireContext(), "Completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val databaseRef = database.reference.child("usuarios")

        if (usuario == null) { // Nuevo usuario
            if (password.length < 6) {
                Toast.makeText(requireContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return
            }

            auth.createUserWithEmailAndPassword(correo, password)
                .addOnSuccessListener { result ->
                    val userId = result.user?.uid ?: return@addOnSuccessListener
                    val nuevoUsuario = Usuario(
                        id = userId,
                        nombre = nombre,
                        apellido = apellido,
                        email = correo,
                        telefono = telefono,
                        rol = rol
                    )

                    databaseRef.child(userId).setValue(nuevoUsuario)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                            onUsuarioActualizado?.invoke()
                            dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Error al agregar usuario", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al registrar usuario en Firebase", Toast.LENGTH_SHORT).show()
                }
        } else { // Editar usuario existente
            usuario?.id?.let { userId ->
                val usuarioActualizado = Usuario(
                    id = userId,
                    nombre = nombre,
                    apellido = apellido,
                    email = correo,
                    telefono = telefono,
                    rol = rol
                )

                databaseRef.child(userId).setValue(usuarioActualizado)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
                        onUsuarioActualizado?.invoke()
                        dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Error al actualizar usuario", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun confirmarEliminacion() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este usuario?")
            .setPositiveButton("Eliminar") { _, _ -> eliminarUsuario() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarUsuario() {
        usuario?.id?.let { userId ->
            val databaseRef = database.reference.child("usuarios")

            databaseRef.child(userId).removeValue()
                .addOnSuccessListener {
                    auth.currentUser?.delete()
                    Toast.makeText(requireContext(), "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show()
                    onUsuarioActualizado?.invoke()
                    dismiss()
                }
                .addOnFailureListener { e ->
                    val errorMessage = e.localizedMessage ?: "Error desconocido"
                    Toast.makeText(requireContext(), "Error al eliminar usuario: $errorMessage", Toast.LENGTH_SHORT).show()
                }
        }
    }
}





