package com.ortiz.controlgastos.data


import kotlinx.coroutines.flow.Flow

class GastosRepository(private val dao: GastosDao) {

    fun movimientos(desde: Long, hasta: Long): Flow<List<MovimientoDetalle>> =
        dao.movimientosDelMes(desde, hasta)

    fun total(tipo: String, desde: Long, hasta: Long): Flow<Double> =
        dao.total(tipo, desde, hasta)

    fun gastosPorCategoria(desde: Long, hasta: Long): Flow<List<TotalCategoria>> =
        dao.gastosPorCategoria(desde, hasta)

    suspend fun categorias(): List<Categoria> = dao.listaCategorias()

    suspend fun obtener(id: Int): Movimiento? = dao.obtener(id)

    suspend fun guardar(m: Movimiento) {
        if (m.id == 0) dao.insertar(m) else dao.actualizar(m)
    }

    suspend fun eliminar(id: Int) = dao.eliminarPorId(id)
}