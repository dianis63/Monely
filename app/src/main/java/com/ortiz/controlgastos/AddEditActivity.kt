package com.ortiz.controlgastos

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.ortiz.controlgastos.data.Categoria
import com.ortiz.controlgastos.data.Movimiento
import com.ortiz.controlgastos.data.Tipo
import com.ortiz.controlgastos.databinding.ActivityAddEditBinding
import com.ortiz.controlgastos.ui.Formato
import com.ortiz.controlgastos.ui.GastosViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class AddEditActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_MES = "extra_mes"
    }

    private lateinit var binding: ActivityAddEditBinding
    private val vm: GastosViewModel by viewModels()

    private var idEdicion = -1
    private var fecha = System.currentTimeMillis()
    private var categorias: List<Categoria> = emptyList()
    private var categoriaPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.header.updatePadding(top = bars.top + 16.dp())
            binding.scroll.updatePadding(bottom = bars.bottom + 16.dp())
            insets
        }

        idEdicion = intent.getIntExtra(EXTRA_ID, -1)
        val mesActual = Formato.inicioMes(System.currentTimeMillis())
        val mesVisto = intent.getLongExtra(EXTRA_MES, mesActual)
        if (idEdicion == -1 && mesVisto != mesActual) {
            fecha = mesVisto
        }
        binding.tvTitulo.text = if (idEdicion == -1) "Nuevo movimiento" else "Editar movimiento"
        binding.toggleTipo.check(R.id.btnGasto)

        binding.btnFecha.setOnClickListener { mostrarCalendario() }
        binding.btnGuardar.setOnClickListener { guardar() }

        lifecycleScope.launch {
            categorias = vm.listaCategorias()
            val nombres = categorias.map { it.nombre }
            binding.actCategoria.setAdapter(
                ArrayAdapter(this@AddEditActivity, android.R.layout.simple_list_item_1, nombres)
            )
            binding.actCategoria.setOnItemClickListener { _, _, pos, _ -> categoriaPos = pos }
            binding.actCategoria.setText(nombres.firstOrNull() ?: "", false)

            if (idEdicion != -1) cargar()
            actualizarFecha()
        }
    }

    private suspend fun cargar() {
        val m = vm.obtener(idEdicion) ?: return
        binding.toggleTipo.check(if (m.tipo == Tipo.GASTO) R.id.btnGasto else R.id.btnIngreso)
        binding.etMonto.setText(m.monto.toString())
        binding.etNota.setText(m.nota)
        fecha = m.fecha
        val pos = categorias.indexOfFirst { it.id == m.categoriaId }
        if (pos >= 0) {
            categoriaPos = pos
            binding.actCategoria.setText(categorias[pos].nombre, false)
        }
    }

    private fun actualizarFecha() {
        binding.btnFecha.text = "Fecha: ${Formato.fecha(fecha)}"
    }

    private fun mostrarCalendario() {
        val cal = Calendar.getInstance().apply { timeInMillis = fecha }
        DatePickerDialog(
            this,
            { _, y, m, d ->
                cal.set(y, m, d)
                fecha = cal.timeInMillis
                actualizarFecha()
            },
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun guardar() {
        val monto = binding.etMonto.text.toString().toDoubleOrNull()
        if (monto == null || monto <= 0) {
            binding.tilMonto.error = "Ingresa un monto mayor a 0"
            return
        }
        binding.tilMonto.error = null
        if (categorias.isEmpty()) return

        val tipo = if (binding.toggleTipo.checkedButtonId == R.id.btnGasto) Tipo.GASTO else Tipo.INGRESO
        val movimiento = Movimiento(
            id = if (idEdicion == -1) 0 else idEdicion,
            tipo = tipo,
            monto = monto,
            categoriaId = categorias[categoriaPos].id,
            fecha = fecha,
            nota = binding.etNota.text.toString().trim()
        )
        lifecycleScope.launch {
            vm.guardar(movimiento)
            finish()
        }
    }

    private fun Int.dp(): Int = (this * resources.displayMetrics.density).toInt()
}