package com.example.combuapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.combuapp.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var isAdmin = false // Variable para saber si el usuario es Administrador
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("usuarios")
        bottomNav = findViewById(R.id.menu_navuser)

        if (auth.currentUser == null) {
            startActivity(Intent(this, loginActivity::class.java))
            finish()
            return
        }

        // Ocultar menú mientras se verifica el rol
        bottomNav.visibility = View.GONE

        verificarRolDeUsuario()
        setupStatusBar()
    }

    /**
     * 🔹 Verifica si el usuario es Administrador en Realtime Database
     */
    private fun verificarRolDeUsuario() {
        val currentUser = auth.currentUser

        currentUser?.uid?.let { userId ->
            database.child(userId).child("rol").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val rol = snapshot.getValue(String::class.java) ?: "Encargado"
                    isAdmin = rol == "Administrador"
                    setupBottomNavigation()
                    bottomNav.visibility = View.VISIBLE
                }

                override fun onCancelled(error: DatabaseError) {
                    isAdmin = false
                    setupBottomNavigation()
                    bottomNav.visibility = View.VISIBLE
                }
            })
        }
    }

    /**
     * 🔹 Configura el menú de navegación dependiendo del rol del usuario
     */
    private fun setupBottomNavigation() {
        bottomNav.menu.clear() // Limpiar menú actual
        if (isAdmin) {
            bottomNav.inflateMenu(R.menu.menu_nav_admin) // Cargar menú de administrador
        } else {
            bottomNav.inflateMenu(R.menu.menu_nav) // Cargar menú de encargado
        }

        // Fragmentos generales
        val panelFragment = PanelFragment()
        val reportesFragment = ReportesFragment()
        val verFragment = VerFragment()
        val ajustesFragment = AjustesFragment()
        val registroFragment = RegistroFragment() // Solo para administradores
        val usuariosFragment = UsuariosFragment()

        makeCurrentFragment(panelFragment)
        if (isAdmin) makeCurrentFragment(registroFragment)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_lista -> makeCurrentFragment(verFragment)
                R.id.ic_lista_admin -> if (isAdmin) makeCurrentFragment(verFragment)
                R.id.ic_crear -> makeCurrentFragment(reportesFragment)
                R.id.ic_usuario -> makeCurrentFragment(panelFragment)
                R.id.ic_ajustes -> makeCurrentFragment(ajustesFragment)
                R.id.ic_ajustes_admin -> if (isAdmin) makeCurrentFragment(ajustesFragment)
                R.id.ic_gestionar_usuarios -> if (isAdmin) makeCurrentFragment(usuariosFragment)
            }
            true
        }

        bottomNav.selectedItemId = R.id.ic_usuario
    }

    /**
     * 🔹 Cambia el fragmento actual de la pantalla
     */
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmento_contenedor, fragment)
            commit()
        }

    /**
     * 🔹 Configura la barra de estado (StatusBar) con colores claros
     */
    private fun setupStatusBar() {
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

