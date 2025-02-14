package com.example.combuapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio_layout)


        val btnAcceder = findViewById<Button>(R.id.btnAcceder)
        btnAcceder.setOnClickListener {
            btnAcceder.isEnabled = false

            val intent = Intent(this@InicioActivity, loginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            btnAcceder.postDelayed({ btnAcceder.isEnabled = true }, 1000)
        }
        fun onButtonClick(view: View) {
        }

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
}
