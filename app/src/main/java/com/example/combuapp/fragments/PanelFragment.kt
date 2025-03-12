package com.example.combuapp.fragments
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.combuapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PanelFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var txtNombre: TextView
    private lateinit var txtApellido: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtTelefono: TextView
    private lateinit var txtRol: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var imgUser: ImageView
    private lateinit var cardView: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_panel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("usuarios")
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Asignar vistas
        txtNombre = view.findViewById(R.id.txtNombre)
        txtApellido = view.findViewById(R.id.txtApellido)
        txtEmail = view.findViewById(R.id.txtEmail)
        txtTelefono = view.findViewById(R.id.txtTelefono)
        txtRol = view.findViewById(R.id.txtRol)
        progressBar = view.findViewById(R.id.progressBar)
        imgUser = view.findViewById(R.id.imgUser)
        cardView = view.findViewById(R.id.cardInfo)

        // Intentar cargar datos desde SharedPreferences primero
        if (cargarDatosDesdeCache()) {
            Log.d("PanelFragment", "Datos cargados desde SharedPreferences")
        } else {
            Log.d("PanelFragment", "SharedPreferences vacío, obteniendo datos de Firebase")
            cargarDatosUsuario()
        }
    }

    override fun onResume() {
        super.onResume()
        // Cada vez que el usuario regresa al fragmento, forzamos actualización desde Firebase
        cargarDatosUsuario()
    }

    /**
     * 🔹 Carga los datos del usuario desde Realtime Database y los almacena en caché
     */
    private fun cargarDatosUsuario() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            mostrarError("Error: No estás autenticado")
            return
        }

        progressBar.visibility = View.VISIBLE
        cardView.visibility = View.GONE

        database.child(currentUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nombre = snapshot.child("nombre").getValue(String::class.java) ?: "N/A"
                    val apellido = snapshot.child("apellido").getValue(String::class.java) ?: "N/A"
                    val email = snapshot.child("email").getValue(String::class.java) ?: "N/A"
                    val telefono = snapshot.child("telefono").getValue(String::class.java) ?: "N/A"
                    val rol = snapshot.child("rol").getValue(String::class.java) ?: "Encargado"

                    Log.d("PanelFragment", "Datos obtenidos de Firebase: $nombre, $apellido, $email, $telefono, $rol")

                    // Guardar en SharedPreferences
                    guardarDatosEnCache(nombre, apellido, email, telefono, rol)

                    // Mostrar datos en la UI
                    actualizarUI(nombre, apellido, email, telefono, rol)
                } else {
                    mostrarError("No se encontraron datos del usuario")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                mostrarError("Error al cargar datos: ${error.message}")
            }
        })
    }

    /**
     * 🔹 Guarda los datos en SharedPreferences para evitar recargas innecesarias
     */
    private fun guardarDatosEnCache(nombre: String, apellido: String, email: String, telefono: String, rol: String) {
        val editor = sharedPreferences.edit()
        editor.putString("nombre", nombre)
        editor.putString("apellido", apellido)
        editor.putString("email", email)
        editor.putString("telefono", telefono)
        editor.putString("rol", rol)
        editor.apply()
    }

    /**
     * 🔹 Intenta cargar los datos desde SharedPreferences y los coloca en la UI
     */
    private fun cargarDatosDesdeCache(): Boolean {
        val nombre = sharedPreferences.getString("nombre", null)
        val apellido = sharedPreferences.getString("apellido", null)
        val email = sharedPreferences.getString("email", null)
        val telefono = sharedPreferences.getString("telefono", null)
        val rol = sharedPreferences.getString("rol", null)

        return if (nombre != null && apellido != null && email != null && telefono != null && rol != null) {
            actualizarUI(nombre, apellido, email, telefono, rol)
            true
        } else {
            false
        }
    }

    /**
     * 🔹 Actualiza la interfaz de usuario con los datos obtenidos
     */
    private fun actualizarUI(nombre: String, apellido: String, email: String, telefono: String, rol: String) {
        txtNombre.text = "Nombre: $nombre"
        txtApellido.text = "Apellido: $apellido"
        txtEmail.text = "Correo: $email"
        txtTelefono.text = "Teléfono: $telefono"
        txtRol.text = "Rol: $rol"

        progressBar.visibility = View.GONE
        cardView.visibility = View.VISIBLE
    }

    /**
     * 🔹 Muestra un mensaje de error en la pantalla
     */
    private fun mostrarError(mensaje: String) {
        txtNombre.text = mensaje
        progressBar.visibility = View.GONE
    }
}

