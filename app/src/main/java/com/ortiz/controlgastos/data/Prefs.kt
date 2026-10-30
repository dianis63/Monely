package com.ortiz.controlgastos.data


import android.content.Context

class Prefs(context: Context) {
    private val sp = context.getSharedPreferences("ajustes", Context.MODE_PRIVATE)

    var nombre: String
        get() = sp.getString("nombre", "") ?: ""
        set(value) = sp.edit().putString("nombre", value).apply()
}