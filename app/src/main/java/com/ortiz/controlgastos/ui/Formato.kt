package com.ortiz.controlgastos.ui

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Formato {

    fun inicioMes(millis: Long): Long = Calendar.getInstance().apply {
        timeInMillis = millis
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    fun finMes(inicio: Long): Long = Calendar.getInstance().apply {
        timeInMillis = inicio
        add(Calendar.MONTH, 1)
    }.timeInMillis - 1

    fun sumarMeses(inicio: Long, n: Int): Long = Calendar.getInstance().apply {
        timeInMillis = inicio
        add(Calendar.MONTH, n)
    }.timeInMillis

    fun mes(millis: Long): String =
        SimpleDateFormat("MMMM yyyy", Locale("es", "SV")).format(Date(millis))
            .replaceFirstChar { it.uppercase() }

    fun fecha(millis: Long): String =
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(millis))

    fun dinero(valor: Double): String = String.format(Locale.US, "\$%,.2f", valor)
}