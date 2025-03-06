package com.example.combuapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.combuapp.fragments.AjustesFragment
import com.example.combuapp.fragments.PanelFragment
import com.example.combuapp.fragments.ReportesFragment
import com.example.combuapp.fragments.VerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, loginActivity::class.java))
            finish()
            return
        }

        val panelFragment = PanelFragment()
        val reportesFragment = ReportesFragment()
        val verFragment = VerFragment()
        val ajustesFragment = AjustesFragment()

        makeCurrentFragment(panelFragment)

        val bottomnav = findViewById<BottomNavigationView>(R.id.menu_navuser)

        bottomnav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_lista -> makeCurrentFragment(verFragment)
                R.id.ic_crear -> makeCurrentFragment(reportesFragment)
                R.id.ic_usuario -> makeCurrentFragment(panelFragment)
                R.id.ic_ajustes -> makeCurrentFragment(ajustesFragment)
            }
            true
        }

        bottomnav.selectedItemId = R.id.ic_usuario

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

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmento_contenedor, fragment)
            commit()
        }
}
