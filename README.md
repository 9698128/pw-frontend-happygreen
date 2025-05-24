# 📱 HappyGreen Frontend

App Android per uno stile di vita più sostenibile ed eco-friendly.

## 🚀 Funzionalità

### ✅ Implementate
- **Schermata di Login**
  - Design moderno con Material Design 3
  - Validazione input
  - Integrazione con API backend
  - Gestione stati di caricamento

- **Autenticazione**
  - Login con email e password
  - Gestione token JWT
  - Interceptor per richieste HTTP
  - Auto-refresh token

- **UI/UX**
  - Theme HappyGreen personalizzato
  - Colori eco-friendly (verdi)
  - Componenti Material Design 3
  - Animazioni fluide

### 🔧 Tecnologie Utilizzate
- **Framework**: Android Jetpack Compose
- **Linguaggio**: Kotlin
- **Architettura**: MVVM + Hilt DI
- **Networking**: Retrofit + OkHttp
- **Navigation**: Navigation Compose
- **UI**: Material Design 3

## 📦 Installazione

### Prerequisiti
- Android Studio Hedgehog o superiore
- JDK 17
- Android SDK 34
- Device/Emulatore Android (minSdk 24)

### Setup
```bash
# Clona il repository
git clone https://github.com/tuo-username/happygreen-frontend.git

# Apri il progetto in Android Studio
# Sync dependencies
# Build & Run
```

## 🔧 Configurazione

### Network Configuration
L'app è configurata per connettersi al backend:

```kotlin
// Constants.kt
const val BASE_URL = "http://172.20.10.3:8000/api/"
```

### Permessi Android
```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### Network Security Config
```xml
<!-- network_security_config.xml -->
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="false">172.20.10.3</domain>
        <domain includeSubdomains="false">localhost</domain>
    </domain-config>
</network-security-config>
```

## 🏗️ Architettura

### Struttura del Progetto
```
app/src/main/java/com/happygreen/frontend/
├── data/
│   ├── api/          # API interfaces
│   ├── models/       # Data models
│   └── repository/   # Repository pattern
├── di/               # Dependency Injection
├── ui/
│   ├── screens/      # Compose screens
│   ├── components/   # Reusable components
│   └── theme/        # App theme
├── utils/            # Utility classes
└── MainActivity.kt   # Main activity
```

### Dependency Injection (Hilt)
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideAuthApiService(): AuthApiService
}
```

## 🎨 Design System

### Colori Principali
- **Primary**: Verde eco (#4CAF50)
- **Secondary**: Verde scuro (#2E7D32) 
- **Background**: Bianco puro
- **Surface**: Grigio chiaro

### Typography
- Material Design 3 Typography
- Font: Default system font

## 📱 Schermate

### 1. Login Screen
- **Funzionalità**:
  - Input email e password
  - Validazione real-time
  - Loading state durante login
  - Gestione errori
  - Link "Password dimenticata" (placeholder)

- **Navigazione**: 
  - Login riuscito → Dashboard (da implementare)

## 🔄 Gestione Stati

### Loading States
```kotlin
data class LoginUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)
```

### Error Handling
- Errori di rete
- Errori di validazione
- Messaggi user-friendly

## 🌐 API Integration

### Configurazione Retrofit
```kotlin
@POST("auth/login")
suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
```

### Modelli Dati
```kotlin
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: UserData?
)
```

## 🚧 In Sviluppo

Le seguenti funzionalità sono pianificate:
- Dashboard principale
- Schermata di registrazione
- Profilo utente
- Sistema di navigazione completo

## 🐛 Troubleshooting

**Problema**: Errore "CLEARTEXT communication not permitted"
**Soluzione**: Verifica `network_security_config.xml` e IP backend

**Problema**: App crasha al login
**Soluzione**: Verifica che il backend sia in esecuzione

**Problema**: Errore di connessione
**Soluzione**: Controlla IP del backend e connessione di rete

## 📝 Build Variants

- **Debug**: Logging abilitato, networking permissivo
- **Release**: Ottimizzazioni, offuscamento codice

## 🧪 Testing

### Come testare:
1. Avvia il backend Laravel
2. Installa l'app su device/emulatore
3. Testa login con credenziali valide
4. Verifica navigazione e stati dell'app
