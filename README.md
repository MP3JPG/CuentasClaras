# CuentasClaras — Gestión de Gastos Compartidos

Aplicación móvil nativa para Android desarrollada en **Kotlin** y **Jetpack Compose** bajo **Clean Architecture + MVVM**, diseñada para dividir gastos cotidianos entre amigos, compañeros de departamento (*roomies*) o viajes en grupo.

## Arquitectura y Stack Tecnológico

- **Lenguaje:** 100% Kotlin (Coroutines, Flow y StateFlow).
- **Interfaz de Usuario:** Jetpack Compose + Material Design 3.
- **Arquitectura:** Clean Architecture estricta dividida en capas `domain`, `data` y `presentation` con patrón MVVM.
- **Inyección de Dependencias:** Dagger Hilt (`@HiltAndroidApp`, `@Module`, `@HiltViewModel`).
- **Consumo de API REST:** Retrofit 2 + OkHttp 4 con `AuthInterceptor` para inyección automática de cabecera `Authorization: Bearer <token>`.
- **Persistencia Offline-First:** Room Database como única fuente de verdad (*Single Source of Truth*) para consulta sin conexión a internet.
- **Seguridad de Sesión:** Jetpack Preferences DataStore para almacenamiento persistente del token JWT y datos de sesión.

## Estructura del Proyecto

```text
com.cuentasclaras.app
├── di/                  # Módulos Hilt (NetworkModule, DatabaseModule, RepositoryModule)
├── data/
│   ├── local/           # Room Database (AppDatabase, DAOs, Entities) y DataStore
│   ├── remote/          # API Service (Retrofit), DTOs y OkHttp AuthInterceptor
│   └── repository/      # Implementaciones Offline-First de repositorios
├── domain/
│   ├── model/           # Modelos puros de negocio (UserSession, ExpenseGroup, Expense)
│   ├── repository/      # Contratos e interfaces de repositorios
│   ├── usecase/         # Casos de uso de responsabilidad única
│   └── util/            # Clase sellada Resource para manejo de estados y errores
└── presentation/
    ├── login/           # LoginScreen, LoginViewModel y LoginUiState
    ├── groups/          # GroupsScreen, GroupsViewModel y GroupsUiState
    ├── detail/          # GroupDetailScreen, GroupDetailViewModel y GroupDetailUiState
    └── navigation/      # AppNavGraph, Screen y MainViewModel