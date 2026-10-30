package com.ortiz.controlgastos.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ortiz.controlgastos.data.AppDatabase
import com.ortiz.controlgastos.data.Categoria
import com.ortiz.controlgastos.data.GastosRepository
import com.ortiz.controlgastos.data.Movimiento
import com.ortiz.controlgastos.data.MovimientoDetalle
import com.ortiz.controlgastos.data.Tipo
import com.ortiz.controlgastos.data.TotalCategoria
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class GastosViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = GastosRepository(AppDatabase.get(app).gastosDao())

    private val _mes = MutableStateFlow(Formato.inicioMes(System.currentTimeMillis()))
    val mes: StateFlow<Long> = _mes

    val movimientos: StateFlow<List<MovimientoDetalle>> = _mes
        .flatMapLatest { repo.movimientos(it, Formato.finMes(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val ingresos: StateFlow<Double> = _mes
        .flatMapLatest { repo.total(Tipo.INGRESO, it, Formato.finMes(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val gastos: StateFlow<Double> = _mes
        .flatMapLatest { repo.total(Tipo.GASTO, it, Formato.finMes(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val gastosPorCategoria: StateFlow<List<TotalCategoria>> = _mes
        .flatMapLatest { repo.gastosPorCategoria(it, Formato.finMes(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun cambiarMes(delta: Int) {
        _mes.value = Formato.sumarMeses(_mes.value, delta)
    }

    fun eliminar(id: Int) {
        viewModelScope.launch { repo.eliminar(id) }
    }

    suspend fun guardar(m: Movimiento) = repo.guardar(m)
    suspend fun obtener(id: Int): Movimiento? = repo.obtener(id)
    suspend fun listaCategorias(): List<Categoria> = repo.categorias()
}