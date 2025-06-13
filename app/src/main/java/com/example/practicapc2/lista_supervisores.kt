package com.example.practicapc2

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.practicapc2.adaptador.SupervisorAdapter
import com.example.practicapc2.entidad.Supervisores
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.example.practicapc2.dao.supervisorDAO
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import java.io.File

class lista_supervisores : AppCompatActivity() {

    private lateinit var btnNuevoSupervisor: MaterialButton
    private lateinit var edtBarraBusqueda: TextInputEditText
    private lateinit var btnBuscar: MaterialButton
    private lateinit var recyclerView: RecyclerView

    private lateinit var superAdapter: SupervisorAdapter
    private lateinit var listaSupervisores: MutableList<Supervisores>
    private var listaActualMostrada: MutableList<Supervisores> = mutableListOf() // NUEVO: mantiene la lista que se muestra (filtrada o no)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_supervisores)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnNuevoSupervisor = findViewById(R.id.btnNuevoSupervisor2)
        edtBarraBusqueda = findViewById(R.id.edtBarraBusqueda2)
        btnBuscar = findViewById(R.id.btnBuscar2)
        recyclerView = findViewById(R.id.reciclerviewSupervisores)

        recyclerView.layoutManager = LinearLayoutManager(this)

        btnBuscar.setOnClickListener {
            val nombreBuscado = edtBarraBusqueda.text.toString().trim()
            if (nombreBuscado.isNotEmpty()) {
                buscarPorNombre(nombreBuscado)
            } else {
                cargarSupervisores() // Si no hay texto, recargar la lista completa
            }
        }

        btnNuevoSupervisor.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



    } // fin onCreate



    override fun onResume() {
        super.onResume()
        cargarSupervisores()
    }

    private fun cargarSupervisores() {
        val dao = supervisorDAO(this)
        listaSupervisores = dao.listarSupervisores().toMutableList()
        listaActualMostrada = listaSupervisores.toMutableList() // ✅ Reflejamos que la lista mostrada es la completa

        superAdapter = SupervisorAdapter(this, listaActualMostrada) { posicion, idSupervisor ->
            eliminarSupervisorConConfirmacion(posicion, idSupervisor)
        }

        recyclerView.adapter = superAdapter
        configurarSwipe()
    }

    private fun buscarPorNombre(nombre: String) {
        val listaFiltrada = listaSupervisores.filter {
            it.nombreE.contains(nombre, ignoreCase = true)
        }.toMutableList()
        listaActualMostrada = listaFiltrada //Reflejamos que ahora la lista mostrada es la filtrada

        superAdapter = SupervisorAdapter(this, listaActualMostrada) { posicion, idSupervisor ->
            eliminarSupervisorConConfirmacion(posicion, idSupervisor)
        }

        recyclerView.adapter = superAdapter
        configurarSwipe()
    }

    //! Este método centraliza la eliminación desde swipe o botón, en lista normal o filtrada
    private fun eliminarSupervisorConConfirmacion(posicion: Int, idSupervisor: Int) {
        mostrarDialogoEliminarSupervisor {
            val dao = supervisorDAO(this)
            val supervisor = dao.obtenerSupervisorPorId(idSupervisor)
            if (supervisor != null) {
                val nombreImagen = supervisor.imagenE
                val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val archivoImagen = File(storageDir, nombreImagen)

                if (archivoImagen.exists()) {
                    archivoImagen.delete()
                }

                // 🔴 Eliminar de la base de datos
                dao.eliminarSupervisorPorId(idSupervisor)

                // ✅ También elimínalo de la lista mostrada y notifica al adaptador
                listaActualMostrada.removeAt(posicion)
                superAdapter.notifyItemRemoved(posicion)

                Toast.makeText(this, "Supervisor eliminado", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun configurarSwipe() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val supervisor = listaActualMostrada[posicion] // Usamos lista actual mostrada

                eliminarSupervisorConConfirmacion(posicion, supervisor.idE)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun mostrarDialogoEliminarSupervisor(onConfirmar: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage("¿Quieres eliminar este supervisor?")
            .setPositiveButton("Aceptar") { _, _ -> onConfirmar() }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}