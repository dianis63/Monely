package com.ortiz.controlgastos.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

object Tipo {
    const val GASTO = "GASTO"
    const val INGRESO = "INGRESO"
}

@Entity(
    tableName = "movimientos",
    foreignKeys = [
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("categoriaId")]
)
data class Movimiento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipo: String,
    val monto: Double,
    val categoriaId: Int,
    val fecha: Long,
    val nota: String = ""
)