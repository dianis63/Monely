package com.ortiz.controlgastos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Categoria::class, Movimiento::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gastosDao(): GastosDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gastos.db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        listOf(
                            "Comida", "Transporte", "Servicios", "Entretenimiento",
                            "Salud", "Educación", "Sueldo", "Otros"
                        ).forEach { db.execSQL("INSERT INTO categorias (nombre) VALUES ('$it')") }
                    }
                }).build().also { INSTANCE = it }
            }
    }
}