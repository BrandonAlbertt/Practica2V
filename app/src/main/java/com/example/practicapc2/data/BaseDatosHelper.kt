package com.example.practicapc2.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatosHelper(contexto: Context) : SQLiteOpenHelper(contexto, "dbbrandon", null, 1) {

    override fun onCreate(baseDatos: SQLiteDatabase) {
        baseDatos.execSQL(
            """
            CREATE TABLE supervisores (
                idB INTEGER PRIMARY KEY AUTOINCREMENT,
                nombreB TEXT NOT NULL,
                apellidoB TEXT NOT NULL,
                dniB TEXT NOT NULL,
                sueldoB REAL NOT NULL,
                turnoB TEXT NOT NULL,
                imagenB TEXT NOT NULL
            );
            """.trimIndent()
        )
    }

    override fun onUpgrade(baseDatos: SQLiteDatabase, versionAnterior: Int, versionNueva: Int) {
        baseDatos.execSQL("DROP TABLE IF EXISTS supervisores")
        onCreate(baseDatos)
    }
}
