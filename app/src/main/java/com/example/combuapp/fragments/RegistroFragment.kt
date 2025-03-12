package com.example.combuapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.combuapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistroFragment : Fragment() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etPassword: EditText
    private lateinit var autoCompleteRol: AutoCompleteTextView
    private lateinit var btnRegistrar: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_registro, container, false)

        // Inicialización de Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Enlazar vistas con findViewById
        etNombre = root.findViewById(R.id.et_nombre)
        etApellido = root.findViewById(R.id.et_apellido)
        etCorreo = root.findViewById(R.id.et_correo)
        etTelefono = root.findViewById(R.id.et_telefono)
        etPassword = root.findViewById(R.id.et_password)
        autoCompleteRol = root.findViewById(R.id.autocompletarol)
        btnRegistrar = root.findViewById(R.id.btn_registrar)

        setupAutoCompleteFields(root)
        setupButtonListener()

        return root
    }

    private fun setupAutoCompleteFields(root: View) {
        val opcionesRoles = resources.getStringArray(R.array.rol)
        val adapterRoles = ArrayAdapter(requireContext(), R.layout.seleccionarray, opcionesRoles)
        autoCompleteRol.setAdapter(adapterRoles)
    }

    private fun setupButtonListener() {
        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val rol = autoCompleteRol.text.toString().trim()

            // Validaciones
            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || telefono.isEmpty() || password.isEmpty() || rol.isEmpty()) {
                Toast.makeText(requireContext(), "Todos los campos deben estar llenos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Toast.makeText(requireContext(), "Correo inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (telefono.length < 10) {
                Toast.makeText(requireContext(), "Número de teléfono inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(requireContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Registro en Firebase Authentication
            auth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                        guardarUsuarioEnFirestore(userId, nombre, apellido, correo, telefono, rol)
                    } else {
                        Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun guardarUsuarioEnFirestore(userId: String, nombre: String, apellido: String, correo: String, telefono: String, rol: String) {
        val usuario = mapOf(
            "nombre" to nombre,
            "apellido" to apellido,
            "email" to correo,
            "telefono" to telefono,
            "rol" to rol
        )

        db.collection("usuarios").document(userId)
            .set(usuario)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun limpiarCampos() {
        etNombre.text.clear()
        etApellido.text.clear()
        etCorreo.text.clear()
        etTelefono.text.clear()
        etPassword.text.clear()
        autoCompleteRol.text.clear()
    }
}
