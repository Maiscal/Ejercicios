package com.example.vamos1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class pasosLocos : AppCompatActivity(), SensorEventListener {

    // Instancia del SensorManager para gestionar los sensores del dispositivo.
    private lateinit var sensorManager: SensorManager
    // Sensores para contar los pasos (total y por cada detección).
    private var stepCounter: Sensor? = null
    private var stepDetector: Sensor? = null
    // Bandera para saber si el contador está activo o no.
    private var isCounterActive = false
    // Contador de pasos.
    private var steps = 0
    // Almacena el número inicial de pasos cuando se empieza a contar.
    private var initialSteps = -1


        private lateinit var stepsTextView: TextView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_pasos_locos)


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 1)
            }

            // Inicializa el SensorManager y obtiene los sensores de pasos (contador y detector).
            sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

            // Verifica si los sensores están disponibles en el dispositivo.
            if (stepCounter == null && stepDetector == null) {
                Toast.makeText(this, "No se encontró un sensor de pasos compatible.", Toast.LENGTH_SHORT).show()
                return
            }

            // Inicializa el TextView para mostrar los pasos.
            stepsTextView = findViewById(R.id.textView_pasos)

            // Configura el botón para iniciar el contador.
            val startButton = findViewById<Button>(R.id.btn_Start)
            val stopButton = findViewById<Button>(R.id.btn_Stop)

            // Inicia el contador de pasos al hacer clic en "Start".
            startButton.setOnClickListener {
                if (!isCounterActive) {
                    isCounterActive = true
                    steps = 0
                    initialSteps = -1
                    // Registra los sensores para empezar a recibir eventos.
                    sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_UI)
                    sensorManager.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_UI)
                    Toast.makeText(this, "Contador iniciado", Toast.LENGTH_SHORT).show()
                }
            }

            stopButton.setOnClickListener {
                if (isCounterActive) {
                    isCounterActive = false
                    // Desregistra los sensores para detener los eventos.
                    sensorManager.unregisterListener(this)
                    Toast.makeText(this, "Contador detenido", Toast.LENGTH_SHORT).show()
                }
            }


            navegar()
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                // Si el sensor es el contador de pasos, calcula el número total de pasos desde que se encendió el dispositivo.
                if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                    val currentSteps = event.values[0].toInt()

                    // Si no se ha configurado el valor inicial, lo hace con el primer valor recibido.
                    if (initialSteps == -1) {
                        initialSteps = currentSteps
                    }

                    // Calcula los pasos actuales a partir de la diferencia entre los pasos actuales y el valor inicial.
                    steps = currentSteps - initialSteps
                    // Actualiza el TextView para mostrar los pasos.
                    stepsTextView.text = "Pasos: $steps"
                }
                // Si el sensor es el detector de pasos, incrementa el contador cada vez que se detecta un paso.
                else if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                    steps++
                    stepsTextView.text = "Pasos: $steps"
                }
            }
        }


        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}


        override fun onPause() {
            super.onPause()
            if (isCounterActive) {
                sensorManager.unregisterListener(this)
            }
        }

        private fun navegar() {
            val btn1 = findViewById<Button>(R.id.btn_atras)
            btn1.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == 1) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
