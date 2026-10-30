package com.ortiz.controlgastos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GastosDao {

    @Insert
    suspend fun insertar(movimiento: Movimiento)

    @Update
    suspend fun actualizar(movimiento: Movimiento)

    @Query("DELETE FROM movimientos WHERE id = :id")
    suspend fun eliminarPorId(id: Int)

    @Query("SELECT * FROM movimientos WHERE id = :id")
    suspend fun obtener(id: Int): Movimiento?

    @Query("SELECT * FROM categorias ORDER BY nombre")
    suspend fun listaCategorias(): List<Categoria>

    @Query(
        """
        SELECT m.id, m.tipo, m.monto, m.fecha, m.nota, m.categoriaId,
               c.nombre AS categoriaNombre
        FROM movimientos m
        INNER JOIN categorias c ON c.id = m.categoriaId
        WHERE m.fecha BETWEEN :desde AND :hasta
        ORDER BY m.fecha DESC, m.id DESC
        """
    )
    fun movimientosDelMes(desde: Long, hasta: Long): Flow<List<MovimientoDetalle>>

    @Query(
        """
        SELECT COALESCE(SUM(monto), 0.0) FROM movimientos
        WHERE tipo = :tipo AND fecha BETWEEN :desde AND :hasta
        """
    )
    fun total(tipo: String, desde: Long, hasta: Long): Flow<Double>

    @Query(
        """
        SELECT c.nombre AS categoria, SUM(m.monto) AS total
        FROM movimientos m
        INNER JOIN categorias c ON c.id = m.categoriaId
        WHERE m.tipo = 'GASTO' AND m.fecha BETWEEN :desde AND :hasta
        GROUP BY c.id
        ORDER BY total DESC
        """
    )
    fun gastosPorCategoria(desde: Long, hasta: Long): Flow<List<TotalCategoria>>
}