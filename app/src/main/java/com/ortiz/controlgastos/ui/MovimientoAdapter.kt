package com.ortiz.controlgastos.ui

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ortiz.controlgastos.R
import com.ortiz.controlgastos.data.MovimientoDetalle
import com.ortiz.controlgastos.data.Tipo
import com.ortiz.controlgastos.databinding.ItemMovimientoBinding

private object MovimientoDiff : DiffUtil.ItemCallback<MovimientoDetalle>() {
    override fun areItemsTheSame(a: MovimientoDetalle, b: MovimientoDetalle) = a.id == b.id
    override fun areContentsTheSame(a: MovimientoDetalle, b: MovimientoDetalle) = a == b
}

class MovimientoAdapter(
    private val onClick: (MovimientoDetalle) -> Unit,
    private val onLongClick: (MovimientoDetalle) -> Unit
) : ListAdapter<MovimientoDetalle, MovimientoAdapter.VH>(MovimientoDiff) {

    class VH(val b: ItemMovimientoBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemMovimientoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val m = getItem(position)
        val esGasto = m.tipo == Tipo.GASTO
        val b = holder.b
        val ctx = b.root.context

        val colorFuerte = ContextCompat.getColor(ctx, if (esGasto) R.color.gasto else R.color.ingreso)
        val colorSuave = ContextCompat.getColor(ctx, if (esGasto) R.color.gasto_suave else R.color.ingreso_suave)

        b.tvInicial.text = m.categoriaNombre.take(1).uppercase()
        b.tvInicial.setTextColor(colorFuerte)
        b.tvInicial.backgroundTintList = ColorStateList.valueOf(colorSuave)

        b.tvCategoria.text = m.categoriaNombre
        b.tvDetalle.text = listOf(Formato.fecha(m.fecha), m.nota)
            .filter { it.isNotBlank() }
            .joinToString(" · ")
        b.tvMonto.text = (if (esGasto) "-" else "+") + Formato.dinero(m.monto)
        b.tvMonto.setTextColor(colorFuerte)

        b.root.setOnClickListener { onClick(m) }
        b.root.setOnLongClickListener { onLongClick(m); true }
    }
}