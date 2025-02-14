package com.example.combuapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class loginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        val txtAviso: TextView = findViewById(R.id.txtaviso)

        //codigo barra de sistema transparente
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


        //Codigo AlertView
        txtAviso.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Comunicate con el departamento de TI para activar tu usuario!").setCancelable(false)
                .setPositiveButton("OK"){
                    dialog, _-> dialog.dismiss()
                }

            val alerta = builder.create()
            alerta.show()
            alerta.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.azul_marino))

        }


        val btnAcceder = findViewById<Button>(R.id.btnlogin)
        btnAcceder.setOnClickListener {
            btnAcceder.isEnabled = false
            val intent = Intent(this@loginActivity, PanelUsuarioActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(0, 0)
            btnAcceder.postDelayed({ btnAcceder.isEnabled = true }, 1000)
        }
        fun onButtonClick(view: View) {
        }

    }
}