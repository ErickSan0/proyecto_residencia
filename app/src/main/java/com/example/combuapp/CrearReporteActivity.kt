package com.example.combuapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class CrearReporteActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crear_reportes_layout)

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


        val btnVolver = findViewById<Button>(R.id.btnvolver)

        btnVolver.setOnClickListener {
            mostrarDialogoConfirmacion()
        }



    }
    private fun mostrarDialogoConfirmacion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
            .setMessage("¿Seguro que quieres salir? Los cambios no se guardarán.")
            .setPositiveButton("Sí") { _, _ ->
                val intent = Intent(this@CrearReporteActivity, PanelUsuarioActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish() // Cierra la actividad actual
            }
            .setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.show()



    }
    override fun onBackPressed() {

    }
}