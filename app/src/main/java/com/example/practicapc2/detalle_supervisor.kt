package com.example.practicapc2

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practicapc2.dao.supervisorDAO
import java.io.File
import android.os.Environment
import com.google.android.material.button.MaterialButton
import android.content.Intent

class detalle_supervisor : AppCompatActivity() {

    private lateinit var tvNombre: TextView
    private lateinit var tvApellido: TextView
    private lateinit var tvSueldo: TextView
    private lateinit var tvTurno: TextView
    private lateinit var tvDni: TextView
    private lateinit var imgFoto: ImageView
    private lateinit var btnEliminar: MaterialButton
    private lateinit var btnEditar: MaterialButton
    private lateinit var btnVolver: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_supervisor)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnEliminar = findViewById(R.id.btnEliminardetalle)
        btnEditar = findViewById(R.id.btnEditardetalle)
        btnVolver = findViewById(R.id.btnRegresardetalle)
        tvNombre = findViewById(R.id.tvNombre3)
        tvApellido = findViewById(R.id.tvApellido3)
        tvSueldo = findViewById(R.id.tvSueldo3)
        tvTurno = findViewById(R.id.tvTurno3)
        tvDni = findViewById(R.id.tvDNI3)
        imgFoto = findViewById(R.id.imgSuper4) // Asegúrate de tener esta imagen en tu XML

        // Botón para regresar a la lista de supervisores
        btnVolver.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve a la anterior
        }


        val idSupervisor = intent.getIntExtra("idSupervisor", -1)

        if (idSupervisor != -1) {
            val dao = supervisorDAO(this)
            val supervisor = dao.obtenerSupervisorPorId(idSupervisor)

            if (supervisor != null) {
                // Mostras los detalles del supervisor
                // Mostrar los detalles del supervisor
                tvNombre.text = supervisor.nombreE
                tvApellido.text = supervisor.apellidoE
                tvSueldo.text = supervisor.sueldoE.toString()
                tvTurno.text = supervisor.turnoE
                tvDni.text = supervisor.dniE

                val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val archivoImagen = File(storageDir, supervisor.imagenE)
                if (archivoImagen.exists()) {
                    val bitmap = BitmapFactory.decodeFile(archivoImagen.absolutePath)
                    imgFoto.setImageBitmap(bitmap)
                } else {
                    imgFoto.setImageResource(R.drawable.imagen_default)
                }

                // Botón para eliminar supervisor
                btnEliminar.setOnClickListener{
                    val confirmacion = android.app.AlertDialog.Builder(this)
                        .setTitle("Eliminar Supervisor")
                        .setMessage("¿Estás seguro de que deseas eliminar a ${supervisor.nombreE} ${supervisor.apellidoE}?")
                        .setPositiveButton("Sí") { _, _ ->
                            dao.obtenerSupervisorPorId(idSupervisor)
                            // Eliminar imagen del supervisor
                            val archivoImagen = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), supervisor.imagenE)
                            if (archivoImagen.exists()) {
                                archivoImagen.delete() // Elimina la imagen del almacenamiento
                            }
                            // Eliminar supervisor de la base de datos
                            dao.eliminarSupervisorPorId(idSupervisor)
                            Toast.makeText(this, "Supervisor eliminado", Toast.LENGTH_SHORT).show()
                            finish() // Regresa a la lista de supervisores
                        }
                        .setNegativeButton("No", null)
                        .create()
                    confirmacion.show()
                }

                // Botón para editar supervisor
                btnEditar.setOnClickListener{
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("editarSupervisor", true)
                    intent.putExtra("IDsuper", idSupervisor)
                    this.startActivity(intent)
                }



            } else {
                Toast.makeText(this, "No se encontró el supervisor", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "ID no válido", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}