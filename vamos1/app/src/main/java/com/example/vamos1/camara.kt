package com.example.vamos1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*


class camara : AppCompatActivity() {
    // Constantes para manejar las solicitudes de permisos y la captura de imagen
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CODE_PERMISSIONS = 100

    // Variable para almacenar la ruta del archivo de la foto tomada
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camara)

        // Referencia al botón de tomar foto
        val btnTakePhoto: Button = findViewById(R.id.btn_foto)

        // Solicitar permisos para la cámara si aún no están concedidos
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE_PERMISSIONS
            )
        }

        // Configurar el botón de tomar foto
        btnTakePhoto.setOnClickListener {
            if (hasPermissions()) {
                // Si tiene permisos, dispara la cámara para tomar la foto
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "Por favor, concede los permisos necesarios", Toast.LENGTH_SHORT).show()
            }
        }

        // Configura el botón de navegación hacia la actividad principal
        navegar()
    }

    // Método para navegar hacia la actividad principal
    private fun navegar() {
        val btn1 = findViewById<Button>(R.id.btn_atras)
        btn1.setOnClickListener {
            val saltarV1: Intent = Intent(this, MainActivity::class.java)
            startActivity(saltarV1)
        }
    }

    // Método para verificar si el permiso de cámara ha sido concedido
    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    // Método para iniciar la actividad de captura de imagen
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Verifica que la cámara esté disponible
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Crea un archivo donde se almacenará la imagen
            val photoFile: File? = createImageFile()

            photoFile?.also {
                // Prepara los valores para guardar la foto en la galería
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, it.name) // Nombre del archivo
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // Tipo MIME
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${getString(R.string.app_name)}") // Carpeta en la galería
                    put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis()) // Fecha de creación
                    put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis()) // Fecha en que se tomó la foto
                }

                // Inserta la imagen en la galería y obtiene su URI
                val photoURI: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // Si la URI es válida, pasa la URI a la cámara
                photoURI?.let { uri ->
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

                    // Notifica a la galería que se ha agregado una nueva imagen
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
                }
            }
        }
    }

    // Método para crear un archivo temporal donde se almacenará la foto
    private fun createImageFile(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "imagenPrueba${timestamp}_", // Prefijo del nombre del archivo
            ".jpg", // Sufijo
            storageDir // Directorio de almacenamiento
        ).apply {
            currentPhotoPath = absolutePath // Almacena la ruta completa del archivo
        }
    }

    // Manejo del resultado de la actividad de captura de la foto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Muestra la imagen capturada en un ImageView
            val imgPhoto: ImageView = findViewById(R.id.img_foto)

            imgPhoto.setImageURI(Uri.fromFile(File(currentPhotoPath))) // Establece la imagen en el ImageView
            Toast.makeText(this, "Foto guardada en: $currentPhotoPath", Toast.LENGTH_SHORT).show() // Muestra la ruta de la foto
        }
    }

    // Manejo de permisos en tiempo de ejecución
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show()
                finish() // Si no se conceden permisos, cierra la actividad
            }
        }
    }
}
