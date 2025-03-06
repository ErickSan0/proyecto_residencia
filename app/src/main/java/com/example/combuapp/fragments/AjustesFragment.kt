package com.example.combuapp.fragments

import com.example.combuapp.loginActivity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.combuapp.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class AjustesFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ajustes, container, false)

        val btnCerrarSesion = view.findViewById<Button>(R.id.btn_cerrar_sesion)
        val btnCambiarContrasena = view.findViewById<Button>(R.id.btn_cambiar_contrasena)

        btnCerrarSesion.setOnClickListener { confirmarCerrarSesion() }
        btnCambiarContrasena.setOnClickListener { cambiarContrasena(view) }

        return view
    }

    private fun cerrarSesion() {
        auth.signOut()
        val intent = Intent(requireContext(), loginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }

    private fun confirmarCerrarSesion() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ -> cerrarSesion() }
            .setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.azul_marino)
            )
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.azul_marino)
            )
        }
        dialog.show()
    }

    private fun cambiarContrasena(view: View) {
        val etContrasenaActual = view.findViewById<EditText>(R.id.et_password_actual)
        val etNuevaContrasena = view.findViewById<EditText>(R.id.et_password_nueva)
        val etConfirmarContrasena = view.findViewById<EditText>(R.id.et_password_confirmar)

        val actual = etContrasenaActual.text.toString()
        val nueva = etNuevaContrasena.text.toString()
        val confirmar = etConfirmarContrasena.text.toString()

        if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (nueva.length < 6) {
            Toast.makeText(requireContext(), "La nueva contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        if (nueva != confirmar) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = auth.currentUser

        if (usuario != null && usuario.email != null) {
            val credential = EmailAuthProvider.getCredential(usuario.email!!, actual)

            usuario.reauthenticate(credential).addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    usuario.updatePassword(nueva).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(requireContext(), "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Error: ${updateTask.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Contraseña actual incorrecta", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
