package com.example.vamos1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class formulario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formulario)
        navegar()
        layoutfrom()
    }

    fun navegar() {
        var btn1 = findViewById<Button>(R.id.regresar)
        btn1.setOnClickListener() {
            val saltarV: Intent = Intent(this, MainActivity::class.java)
            startActivity(saltarV)
        }
    }


    fun layoutfrom(){
        val btn_gps = findViewById<Button>(R.id.btn_aceptar)
        val tv_nombre = findViewById<TextView>(R.id.etNombre)
        val tv_apellido = findViewById<TextView>(R.id.etApellido)
        val tv_email = findViewById<TextView>(R.id.etCorreo)
        val tv_telefono1 = findViewById<TextView>(R.id.editTextPhone)
        val tv_telefono2 = findViewById<TextView>(R.id.editTextPhone2)


        btn_gps.setOnClickListener(){
            //val mensage="Este es un mensaje"
            val mensage1= "Nombre: ${tv_nombre.text}, " +
                    "Apellido: ${tv_apellido.text}, " +
                    "Correo: ${tv_email.text}, " +
                    "Telefono1: ${tv_telefono1.text}," +
                    "Telefono2: ${tv_telefono2.text}, Es estudiante"
            Toast.makeText(this,mensage1, Toast.LENGTH_SHORT).show()
        }
    }
}