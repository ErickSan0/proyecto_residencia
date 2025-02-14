package com.example.combuapp

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PanelUsuarioActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.panel_usuario_layout)




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.statusBarColor = android.graphics.Color.TRANSPARENT

    }
    private fun mostrarDialogoAjustes() {
        val opciones = arrayOf("Cambiar Contraseña", "Cerrar Sesión")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ajustes")
            .setItems(opciones) { dialog, which ->
                when (which) {
                    0 -> cambiarContrasena()
                    1 -> mostrarConfirmacionCerrarSesion()
                }

            }
            .setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.setOnShowListener{
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(this,R.color.azul_marino))
        }
        dialog.show()
    }

    private fun cambiarContrasena() {
        // Aquí puedes abrir otra pantalla o mostrar otro diálogo
    }

    private fun mostrarConfirmacionCerrarSesion() {
        val builder = AlertDialog.Builder(this)
            .setMessage("¿Estás seguro de que quieres cerrar sesión?")
            .setPositiveButton("Sí") { _, _ -> cerrarSesion() } // Si elige "Sí", cierra sesión
            .setNegativeButton("No", null) // Si elige "No", se cierra el diálogo

        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
                ContextCompat.getColor(this, R.color.azul_marino)
            )
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                ContextCompat.getColor(this, R.color.azul_marino)
            )
        }
        dialog.show()
    }


    private fun cerrarSesion() {

        val sharedPreferences = getSharedPreferences("Sesion", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val intent = Intent(this, loginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(0, 0)
        finishAffinity()
    }
    override fun onBackPressed() {
    }

}
