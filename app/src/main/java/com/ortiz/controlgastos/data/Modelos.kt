package com.ortiz.controlgastos.data

data class MovimientoDetalle(
    val id: Int,
    val tipo: String,
    val monto: Double,
    val fecha: Long,
    val nota: String,
    val categoriaId: Int,
    val categoriaNombre: String
)

data class TotalCategoria(
    val categoria: String,
    val total: Double
)