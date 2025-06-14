package com.example.practicapc2

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.IOException
import android.widget.Toast
import androidx.core.content.FileProvider
import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import com.example.practicapc2.dao.supervisorDAO
import com.example.practicapc2.entidad.Supervisores

class MainActivity : AppCompatActivity() {
    // Declaración de variables para los elementos de la interfaz
    private lateinit var edtNombre: TextInputEditText
    private lateinit var edtApellido: TextInputEditText
    private lateinit var edtDNI: TextInputEditText
    private lateinit var edtSueldo: TextInputEditText
    private lateinit var rdHorario: RadioGroup
    private lateinit var btnGuardar: MaterialButton
    private lateinit var btnFoto: MaterialButton
    private lateinit var btnVerLista: MaterialButton

    // Variables para manejar la foto
    private lateinit var photoURI: Uri
    private lateinit var currentPhotoPath: String
    private var nombreFotoActual: String = ""
    // Lanzador para tomar la foto
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            Toast.makeText(this, "Foto tomada correctamente:\n$nombreFotoActual", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Error al tomar foto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de los elementos de la interfaz
        edtNombre = findViewById(R.id.edtNombre)
        edtApellido = findViewById(R.id.edtApellido)
        edtDNI = findViewById(R.id.edtDNI)
        edtSueldo = findViewById(R.id.edtSueldo)
        rdHorario = findViewById(R.id.rdHorario)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnFoto = findViewById(R.id.btnFoto)
        btnVerLista = findViewById(R.id.btnVerLista)



        //! Llamada al método para configurar el botón Guardar
        funcionGuardarDocente()

        //! llamda para tomar foto y guardar en almacenamiento externo
        btnFoto.setOnClickListener {
            checkCameraPermissionAndTakePhoto()
        }

        //! Configuración del botón para ver la lista de supervisores
        btnVerLista.setOnClickListener {
            val intent = Intent(this, lista_supervisores::class.java)
            startActivity(intent)
            finish()
        }



    } //? fin onCreate

    //!--- Métodos de configuración de cámara y permisos ---
    //!--- Métodos de configuración de cámara y permisos ---
    //!--- Métodos de configuración de cámara y permisos ---
    //!--- Métodos de configuración de cámara y permisos ---
    //! Método para verificar permisos de cámara y tomar foto
    private fun checkCameraPermissionAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            tomarFoto()
        }
    }

    //! Método para manejar el resultado de la solicitud de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            tomarFoto()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    //! Método para tomar una foto y guardarla en el almacenamiento externo
    private fun tomarFoto() {
        val photoFile: File? = try {
            crearArchivoImagen()
        } catch (ex: IOException) {
            Toast.makeText(this, "Error al crear archivo", Toast.LENGTH_SHORT).show()
            null
        }
        // Si se creó el archivo correctamente, se obtiene su URI y se lanza el intent para tomar la foto
        photoFile?.also {
            photoURI = FileProvider.getUriForFile(
                this,
                "com.example.practicapc2.provider",
                it
            )
            takePictureLauncher.launch(photoURI)
        }
    }

    //! Método para crear un archivo de imagen en el almacenamiento externo
    private fun crearArchivoImagen(): File {
        val nombreArchivo = obtenerSiguienteNombre()
        nombreFotoActual = "$nombreArchivo.jpg"
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(storageDir, nombreFotoActual)
        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    //! Método para obtener el siguiente nombre de archivo basado en un contador almacenado en SharedPreferences
    //! Este nombre se formatea como "JPEG_DOCENTE_000000001.jpg", "JPEG_DOCENTE_000000002.jpg", etc.
    private fun obtenerSiguienteNombre(): String {
        val prefs = getSharedPreferences("prefs_fotos", MODE_PRIVATE)
        val contador = prefs.getInt("contador", 1)
        val nombre = String.format("JPEG_DOCENTE_%09d", contador)
        prefs.edit().putInt("contador", contador + 1).apply()
        return nombre
    }





    //! --- Configuración del botón Guardar y lógica de guardado/edición ---
    //! --- Configuración del botón Guardar y lógica de guardado/edición ---
    //! --- Configuración del botón Guardar y lógica de guardado/edición ---
    //! --- Configuración del botón Guardar y lógica de guardado/edición ---
    //! --- Configuración del botón Guardar y lógica de guardado/edición ---

    //! Método para limpiar los campos del formulario
    private fun limpiarCampos() {
        edtNombre.text?.clear()
        edtApellido.text?.clear()
        edtDNI.text?.clear()
        edtSueldo.text?.clear()
        rdHorario.clearCheck()
        nombreFotoActual = ""
    }

    //! Método para mostrar un diálogo de confirmación antes de guardar o editar un docente
    private fun mostrarDialogoConfirmacion(modoEditar: Boolean, onConfirmar: () -> Unit) {
        val mensaje = if (modoEditar) {
            "¿Estás seguro que deseas editar este docente?"
        } else {
            "¿Estás seguro que deseas agregar este docente?"
        }
        AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { _, _ ->
                onConfirmar()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    //! Método para guardar o editar un docente
    private fun funcionGuardarDocente() {
        val modoEditar = intent.getBooleanExtra("editarSupervisor", false)
        val idSupervisor = intent.getIntExtra("IDsuper", -1)

        if (modoEditar && idSupervisor != -1) {
            val dao = supervisorDAO(this)
            val supervisor = dao.obtenerSupervisorPorId(idSupervisor)

            if (supervisor != null) {
                edtNombre.setText(supervisor.nombreE)
                edtApellido.setText(supervisor.apellidoE)
                edtSueldo.setText(supervisor.sueldoE.toString())
                edtDNI.setText(supervisor.dniE)
                when (supervisor.turnoE) {
                    "Mañana" -> rdHorario.check(R.id.rdMañana)
                    "Tarde" -> rdHorario.check(R.id.rdTarde)
                    "Noche" -> rdHorario.check(R.id.rdNoche)
                }
                nombreFotoActual = supervisor.imagenE
            }
        }

        // Validación de DNI al perder foco
        edtDNI.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val dniIngresado = edtDNI.text.toString()
                if (dniIngresado.isNotEmpty()) {
                    val dao = supervisorDAO(this)
                    val dniExiste = dao.existeDni(dniIngresado)

                    val dniOriginal = if (modoEditar) {
                        dao.obtenerSupervisorPorId(idSupervisor)?.dniE
                    } else null

                    if (dniExiste && dniIngresado != dniOriginal) {
                        Toast.makeText(this, "El DNI ya existe", Toast.LENGTH_SHORT).show()
                        btnGuardar.isEnabled = false
                    } else {
                        btnGuardar.isEnabled = true
                    }
                }
            }
        }

        btnGuardar.setOnClickListener {
            val nombre = edtNombre.text.toString()
            val apellido = edtApellido.text.toString()
            val dni = edtDNI.text.toString()
            val sueldo = edtSueldo.text.toString().toDoubleOrNull() ?: 0.0
            val turno = when (rdHorario.checkedRadioButtonId) {
                R.id.rdMañana -> "Mañana"
                R.id.rdTarde -> "Tarde"
                R.id.rdNoche -> "Noche"
                else -> ""
            }

            val foto = if (nombreFotoActual.isNotEmpty()) nombreFotoActual else "imagen_default.jpg"

            if (
                nombre.isNotEmpty() &&
                apellido.isNotEmpty() &&
                dni.isNotEmpty() &&
                edtSueldo.text.toString().isNotEmpty() &&
                turno.isNotEmpty() &&
                nombreFotoActual.isNotEmpty()
            ) {
                mostrarDialogoConfirmacion(modoEditar) {
                    val supervisor = Supervisores(
                        idE = if (modoEditar) idSupervisor else 0,
                        nombreE = nombre,
                        apellidoE = apellido,
                        sueldoE = sueldo,
                        dniE = dni,
                        turnoE = turno,
                        imagenE = foto
                    )

                    val dao = supervisorDAO(this)
                    val resultado = if (modoEditar) dao.editarSupervisorPorId(supervisor) else dao.insertarSupervisor(supervisor)

                    if (resultado) {
                        val mensaje = if (modoEditar) "Datos actualizados correctamente" else "Datos guardados correctamente"
                        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                        if (!modoEditar) limpiarCampos()

                        val intent = Intent(this, lista_supervisores::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos y toma una foto", Toast.LENGTH_SHORT).show()
            }
        }
    }
}