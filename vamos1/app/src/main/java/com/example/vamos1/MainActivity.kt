package com.example.vamos1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_Vamos1)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        navegar()

        }


    fun navegar(){
        var btn1 = findViewById<Button>(R.id.btn1)
        var btn2 = findViewById<Button>(R.id.btn2)
        var btn3 = findViewById<Button>(R.id.btn3)
        var btn4 = findViewById<Button>(R.id.btn4)
        var btn5 = findViewById<Button>(R.id.btn5)

        btn1.setOnClickListener(){
            val saltarV1:Intent=Intent(this,formulario::class.java)
            startActivity(saltarV1)
        }

        btn2.setOnClickListener(){
            val saltarV2:Intent=Intent(this,Imagen::class.java)
            startActivity(saltarV2)
        }

        btn3.setOnClickListener(){
            val saltarV3:Intent=Intent(this,locaGPS::class.java)
            startActivity(saltarV3)
        }
        btn4.setOnClickListener(){
            val saltarV4:Intent=Intent(this,pasosLocos::class.java)
            startActivity(saltarV4)
        }
        btn5.setOnClickListener(){
            val saltarV5:Intent=Intent(this,camara::class.java)
            startActivity(saltarV5)
        }
    }


}
