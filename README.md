# $ Monely

**Aplicación Android para la gestión de finanzas personales.**

Monely es una aplicación móvil nativa que permite registrar y consultar ingresos y gastos personales, llevar un control del balance mensual y organizar los movimientos por categorías. Los datos se almacenan localmente en el dispositivo, sin necesidad de conexión a internet ni creación de cuentas.

## ✨ Funcionalidades

- Registro de ingresos y gastos con monto, categoría, fecha y nota opcional.
- Resumen mensual con total de ingresos, gastos y balance.
- Navegación entre diferentes meses.
- Identificación de la categoría con mayor gasto.
- Edición y eliminación de movimientos.
- Categorías predefinidas: Comida, Transporte, Servicios, Entretenimiento, Salud, Educación, Sueldo y Otros.
- Personalización del saludo mediante el nombre del usuario.
- Validaciones de datos en los formularios.
- Almacenamiento local y persistencia de información.

## 🛠️ Tecnologías

| Área | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| Base de datos | Room (SQLite) |
| Arquitectura | MVVM + Repository |
| Reactividad | Kotlin Coroutines, Flow y StateFlow |
| Interfaz | XML, ViewBinding y Material Design 3 |
| Listas | RecyclerView, ListAdapter y DiffUtil |
| Preferencias | SharedPreferences |
| Procesamiento | KSP |
| Build | Gradle, AGP 9 |

## 🧠 Conceptos aplicados

- Relaciones entre entidades mediante claves foráneas.
- Consultas SQL con INNER JOIN, SUM y GROUP BY.
- Gestión reactiva de datos mediante Flow y StateFlow.
- Uso de flatMapLatest para actualizar la información según el mes seleccionado.
- Gestión del ciclo de vida con repeatOnLifecycle.
- Separación de responsabilidades mediante MVVM y Repository.


## 🚀 Instalación y ejecución

1. Clona el repositorio:

   ```bash
   git clone https://github.com/dianis63/Monely.git
   ```

2. Abre el proyecto en Android Studio.
3. Espera a que Gradle sincronice las dependencias.
4. Ejecuta la aplicación en un emulador o dispositivo Android.

**Requisitos:**
- Android Studio compatible con la configuración del proyecto.
- Android 7.0 (API 24) o superior.

## 🔮 Mejoras futuras

- Gráficas de gastos por categoría.
- Presupuestos mensuales y alertas.
- Exportación de movimientos a CSV.
- Creación y edición de categorías personalizadas.
- Implementación de tema oscuro.

## 👩‍💻 Autora

**Diana Avilés**

Proyecto personal desarrollado con Kotlin y Android Studio.
