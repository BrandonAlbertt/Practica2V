Sistema de Gestión de Supervisores - Android

Un sistema completo para la gestión de supervisores desarrollado en Android con Kotlin, que permite registrar, editar, buscar y eliminar supervisores con fotografía integrada.

📌 Características Principales
Registro completo de supervisores con:

Datos personales (nombre, apellido, DNI)

Información laboral (sueldo, turno)

Fotografía almacenada localmente

Validación de DNI único para evitar duplicados

Búsqueda en tiempo real por nombre

Eliminación con confirmación y swipe to delete

Edición completa de registros existentes

Almacenamiento seguro de imágenes en directorio privado

🛠 Tecnologías Utilizadas
Lenguaje: Kotlin 100%

Arquitectura: Clean Architecture (en progreso)

Persistencia: SQLite con Room

Permisos: Android Runtime Permissions

Cámara: Activity Result API

UI: Material Design 3

📚 Documentación Técnica
Ver documentación detallada de funciones 
https://docs.google.com/document/d/10VOnTZ5eOFJoMpVkdaeUKj1stLlXcWasBmhXELEfsao/edit?usp=sharing

Explicación completa de:

Sistema de captura de fotos

Validación de DNI único

Implementación del RecyclerView

Funciones de búsqueda y filtrado

Proceso de eliminación con confirmación

⚙️ Configuración
Clonar repositorio:

bash
git clone https://github.com/tu-usuario/tu-repositorio.git
Abrir en Android Studio (Arctic Fox o superior)

Configurar FileProvider en AndroidManifest.xml:

xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="com.tudominio.tuapp.provider"
    ... />

📌 Próximas Mejoras
Implementar autenticación de usuarios

Sincronización con Firebase

Exportación a PDF/Excel

Dashboard con estadísticas

🤝 Contribución
¡Las contribuciones son bienvenidas! Por favor lee el CONTRIBUTING.md antes de enviar un PR.

📄 Licencia
MIT © BrandonAlbertt 2025
