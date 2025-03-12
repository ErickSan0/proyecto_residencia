package com.example.combuapp.models


data class Usuario(
    var id: String = "", // 🔹 Agregado id
    var nombre: String = "",
    var apellido: String = "",
    var email: String = "",
    var telefono: String = "",
    var rol: String = "Encargado" // Por defecto "Encargado"
)

