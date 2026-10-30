package com.ortiz.controlgastos
import android.graphics.Color
import androidx.activity.SystemBarStyle
import androidx.core.view.updatePadding
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ortiz.controlgastos.data.MovimientoDetalle
import com.ortiz.controlgastos.data.Prefs
import com.ortiz.controlgastos.databinding.ActivityMainBinding
import com.ortiz.controlgastos.ui.Formato
import com.ortiz.controlgastos.ui.GastosViewModel
import com.ortiz.controlgastos.ui.MovimientoAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: Prefs
    private val vm: GastosViewModel by viewModels()

    private val adapter = MovimientoAdapter(
        onClick = { abrirFormulario(it.id) },
        onLongClick = { confirmarEliminar(it) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.header.updatePadding(top = bars.top + 16.dp())
            binding.rvMovimientos.updatePadding(bottom = bars.bottom + 96.dp())
            insets
        }

        prefs = Prefs(this)
        binding.rvMovimientos.layoutManager = LinearLayoutManager(this)
        binding.rvMovimientos.adapter = adapter

        binding.fabAgregar.setOnClickListener { abrirFormulario(-1) }
        binding.btnMesAnterior.setOnClickListener { vm.cambiarMes(-1) }
        binding.btnMesSiguiente.setOnClickListener { vm.cambiarMes(1) }
        binding.tvSaludo.setOnClickListener { pedirNombre() }

        mostrarSaludo()
        if (prefs.nombre.isEmpty()) pedirNombre()
        observar()
    }

    private fun observar() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.mes.collect { binding.tvMes.text = Formato.mes(it) }
                }
                launch {
                    vm.movimientos.collect {
                        adapter.submitList(it)
                        binding.tvVacio.isVisible = it.isEmpty()
                    }
                }
                launch {
                    combine(vm.ingresos, vm.gastos) { i, g -> i to g }.collect { (i, g) ->
                        binding.tvIngresos.text = "Ingresos\n${Formato.dinero(i)}"
                        binding.tvGastos.text = "Gastos\n${Formato.dinero(g)}"
                        binding.tvBalance.text = Formato.dinero(i - g)
                    }
                }
                launch {
                    vm.gastosPorCategoria.collect { lista ->
                        binding.tvTopCategoria.text = lista.firstOrNull()?.let {
                            "Mayor gasto: ${it.categoria} (${Formato.dinero(it.total)})"
                        } ?: "Sin gastos este mes"
                    }
                }
            }
        }
    }

    private fun abrirFormulario(id: Int) {
        startActivity(
            Intent(this, AddEditActivity::class.java)
                .putExtra(AddEditActivity.EXTRA_ID, id)
                .putExtra(AddEditActivity.EXTRA_MES, vm.mes.value)
        )
    }

    private fun confirmarEliminar(m: MovimientoDetalle) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Eliminar movimiento")
            .setMessage("¿Eliminar ${m.categoriaNombre} por ${Formato.dinero(m.monto)}?")
            .setPositiveButton("Eliminar") { _, _ -> vm.eliminar(m.id) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarSaludo() {
        binding.tvSaludo.text = if (prefs.nombre.isEmpty()) "Hola" else "Hola, ${prefs.nombre}"
    }

    private fun pedirNombre() {
        val input = EditText(this).apply {
            hint = "Tu nombre"
            setText(prefs.nombre)
        }
        MaterialAlertDialogBuilder(this)
            .setTitle("¿Cómo te llamas?")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                prefs.nombre = input.text.toString().trim()
                mostrarSaludo()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    private fun Int.dp(): Int = (this * resources.displayMetrics.density).toInt()
}
