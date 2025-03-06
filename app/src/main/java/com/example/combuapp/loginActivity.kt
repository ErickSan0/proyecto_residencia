package com.example.combuapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException


class loginActivity : AppCompatActivity() {

    private lateinit var autenticacion: FirebaseAuth
    private lateinit var campoCorreo: TextInputEditText
    private lateinit var campoContrasena: TextInputEditText
    private lateinit var botonIniciarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        autenticacion = FirebaseAuth.getInstance()

        campoCorreo = findViewById(R.id.user)
        campoContrasena = findViewById(R.id.psw)
        botonIniciarSesion = findViewById(R.id.btnlogin)

        val textoAviso: TextView = findViewById(R.id.txtaviso)

        configurarBarraEstado()
        configurarAviso(textoAviso)
        configurarBotonInicioSesion()
    }

    private fun configurarBarraEstado() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.statusBarColor = android.graphics.Color.TRANSPARENT
    }

    private fun configurarAviso(textoAviso: TextView) {
        textoAviso.setOnClickListener {
            val constructorAlerta = AlertDialog.Builder(this)
            constructorAlerta.setMessage("Comunícate con el departamento de TI para activar tu usuario.")
                .setCancelable(false)
                .setPositiveButton("OK") { dialogo, _ ->
                    dialogo.dismiss()
                }

            val alerta = constructorAlerta.create()
            alerta.show()
            alerta.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.azul_marino))
        }
    }

    private fun configurarBotonInicioSesion() {
        botonIniciarSesion.setOnClickListener {
            val correo = campoCorreo.text.toString().trim()
            val contrasena = campoContrasena.text.toString().trim()

            if (correo.isEmpty() || contrasena.isEmpty()) {
                mostrarMensaje("Por favor, completa todos los campos")
                return@setOnClickListener
            }

            botonIniciarSesion.isEnabled = false
            iniciarSesion(correo, contrasena)
        }
    }

    private fun iniciarSesion(correo: String, contrasena: String) {
        autenticacion.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { tarea ->
                if (tarea.isSuccessful) {
                    mostrarMensaje("Inicio de sesión exitoso")
                    navegarAlPanel()
                } else {
                    manejarErrorAutenticacion(tarea.exception)
                    botonIniciarSesion.isEnabled = true
                }
            }
    }

    private fun manejarErrorAutenticacion(excepcion: Exception?) {
        val mensajeError = when (excepcion) {
            is FirebaseAuthInvalidUserException -> "No existe una cuenta con este correo electrónico."
            is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
            is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con este correo."
            is FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil."
            is FirebaseAuthRecentLoginRequiredException -> "Debes iniciar sesión nuevamente para continuar."
            else -> "Error desconocido. Inténtalo nuevamente más tarde."
        }
        mostrarMensaje(mensajeError)
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun navegarAlPanel() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}