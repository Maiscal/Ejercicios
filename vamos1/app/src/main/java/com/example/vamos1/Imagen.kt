package com.example.vamos1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Imagen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_imagen)

        navegar()

    }


    fun navegar(){
        var btn1 = findViewById<Button>(R.id.btn1)
        btn1.setOnClickListener(){
            val saltarV: Intent = Intent(this,MainActivity::class.java)
            startActivity(saltarV)
        }
    }
}