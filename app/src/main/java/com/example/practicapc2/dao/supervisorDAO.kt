package com.example.practicapc2.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.practicapc2.data.BaseDatosHelper
import com.example.practicapc2.entidad.Supervisores

class supervisorDAO(contexto: Context) {

    private val dbHelper = BaseDatosHelper(contexto)

    fun insertarSupervisor(sup: Supervisores): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("nombreB", sup.nombreE)
                put("apellidoB", sup.apellidoE)
                put("dniB", sup.dniE)
                put("sueldoB", sup.sueldoE)
                put("turnoB", sup.turnoE)
                put("imagenB", sup.imagenE)
            }
            val resultado = db.insert("supervisores", null, values)
            resultado != -1L
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

    fun listarSupervisores(): List<Supervisores> {
        val lista = mutableListOf<Supervisores>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM supervisores", null)

        if (cursor.moveToFirst()) {
            do {
                val supervisor = Supervisores(
                    idE = cursor.getInt(cursor.getColumnIndexOrThrow("idB")),
                    nombreE = cursor.getString(cursor.getColumnIndexOrThrow("nombreB")),
                    apellidoE = cursor.getString(cursor.getColumnIndexOrThrow("apellidoB")),
                    dniE = cursor.getString(cursor.getColumnIndexOrThrow("dniB")),
                    sueldoE = cursor.getDouble(cursor.getColumnIndexOrThrow("sueldoB")),
                    turnoE = cursor.getString(cursor.getColumnIndexOrThrow("turnoB")),
                    imagenE = cursor.getString(cursor.getColumnIndexOrThrow("imagenB"))
                )
                lista.add(supervisor)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun editarSupervisorPorId(supervisor: Supervisores): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("nombreB", supervisor.nombreE)
                put("apellidoB", supervisor.apellidoE)
                put("dniB", supervisor.dniE)
                put("sueldoB", supervisor.sueldoE)
                put("turnoB", supervisor.turnoE)
                put("imagenB", supervisor.imagenE)
            }
            val resultado = db.update(
                "supervisores",
                values,
                "idB = ?",
                arrayOf(supervisor.idE.toString())
            )
            resultado > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

    fun obtenerSupervisorPorId(id: Int): Supervisores? {
        val db = dbHelper.readableDatabase
        var supervisor: Supervisores? = null

        val cursor = db.rawQuery("SELECT * FROM supervisores WHERE idB = ?", arrayOf(id.toString()))
        if (cursor.moveToFirst()) {
            supervisor = Supervisores(
                idE = cursor.getInt(cursor.getColumnIndexOrThrow("idB")),
                nombreE = cursor.getString(cursor.getColumnIndexOrThrow("nombreB")),
                apellidoE = cursor.getString(cursor.getColumnIndexOrThrow("apellidoB")),
                dniE = cursor.getString(cursor.getColumnIndexOrThrow("dniB")),
                sueldoE = cursor.getDouble(cursor.getColumnIndexOrThrow("sueldoB")),
                turnoE = cursor.getString(cursor.getColumnIndexOrThrow("turnoB")),
                imagenE = cursor.getString(cursor.getColumnIndexOrThrow("imagenB"))
            )
        }

        cursor.close()
        db.close()
        return supervisor
    }

    fun eliminarSupervisorPorId(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val resultado = db.delete("supervisores", "idB = ?", arrayOf(id.toString()))
            resultado > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

    /// Verifica si un DNI ya existe en la base de datos
    fun existeDni(dni: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT dniB FROM supervisores WHERE dniB = ?", arrayOf(dni))
        val existe = cursor.moveToFirst() // si encontró al menos un registro, existe es true
        cursor.close()
        db.close()
        return existe
    }

}