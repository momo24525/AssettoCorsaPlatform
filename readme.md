# 🏁 RaceHub Community

> **Community platform for Assetto Corsa racing in Italy.**

RaceHub Community is a web platform designed for the Italian **Assetto Corsa** community, focused on organizing racing events, managing registrations and handling race results.

The project includes **Steam login**, event management, waiting lists, an administration area and automatic reception of race results.

---

## 🇮🇹 Italiano

### 📖 Descrizione

**RaceHub Community** è una piattaforma web pensata per la community italiana di **Assetto Corsa**.

L'obiettivo è centralizzare la gestione delle gare e della community, offrendo:

* 📅 Calendario degli eventi
* 🏎️ Iscrizione alle gare
* 👥 Lista d'attesa
* 🔐 Login tramite Steam
* 🏆 Gestione dei risultati
* ⚙️ Area amministrativa
* 📡 Ricezione automatica dei risultati delle gare

### 🛠️ Stack tecnologico

| Tecnologia          | Utilizzo                   |
| ------------------- | -------------------------- |
| **Java 17**         | Linguaggio principale      |
| **Spring Boot 4.1** | Framework backend          |
| **Spring MVC**      | Web layer                  |
| **Thymeleaf**       | Rendering delle pagine     |
| **Spring Security** | Autenticazione e sicurezza |
| **Spring Data JPA** | Persistenza dei dati       |
| **H2**              | Database per sviluppo      |
| **PostgreSQL**      | Database per produzione    |

### 📋 Requisiti

* **JDK 17**
* Non è necessario installare Maven: il progetto utilizza il **Maven Wrapper** incluso.

### ⚙️ Configurazione

L'applicazione utilizza la seguente variabile d'ambiente:

| Variabile     | Descrizione                                                                                |
| ------------- | ------------------------------------------------------------------------------------------ |
| `AC_API_KEY`  | Chiave utilizzata dallo script dei risultati per autenticarsi tramite l'header `X-API-KEY` |
| `DB_USERNAME` | Username del database PostgreSQL in produzione                                             |
| `DB_PASSWORD` | Password del database PostgreSQL in produzione                                             |

> ⚠️ **Non inserire mai chiavi API, password o altre credenziali direttamente nei file del repository.**

### 🚀 Avvio in sviluppo

Il profilo `dev` è attivo di default e utilizza un database **H2 su file**, salvato nella directory `./data`.

#### Windows

```powershell
$env:AC_API_KEY="una-chiave-di-test"
.\mvnw.cmd spring-boot:run
```

#### Linux / macOS

```bash
export AC_API_KEY=una-chiave-di-test
./mvnw spring-boot:run
```


### 🔧 Profili

#### `dev`

* H2 su file
* `ddl-auto: update`
* Log SQL attivi
* Console H2 abilitata

#### `prod`

* PostgreSQL
* `ddl-auto: validate`
* Console H2 disabilitata

### ✨ Funzionalità

* 🔐 Login con Steam tramite OpenID
* 📅 Calendario degli eventi
* 📝 Iscrizione agli eventi
* ⏳ Gestione della lista d'attesa
* 👨‍💼 Gestione del database per admin (`/admin/**`)
* 📡 Ricezione dei risultati tramite API

Endpoint utilizzato per la ricezione dei risultati:

```http
POST /admin/results/incoming
```

L'autenticazione per lo script python che invia i risultati avviene tramite l'header:

```http
X-API-KEY: <api-key>
```



## 🇬🇧 English

### 📖 Description

**RaceHub Community** is a web platform built for the Italian **Assetto Corsa** community.

Its goal is to centralize race and community management, providing:

* 📅 Event calendar
* 🏎️ Race registration
* 👥 Waiting list management
* 🔐 Steam login
* 🏆 Race result management
* ⚙️ Administration area
* 📡 Automatic race result reception

### 🛠️ Tech Stack

| Technology          | Purpose                     |
| ------------------- | --------------------------- |
| **Java 17**         | Main programming language   |
| **Spring Boot 4.1** | Backend framework           |
| **Spring MVC**      | Web layer                   |
| **Thymeleaf**       | Server-side page rendering  |
| **Spring Security** | Authentication and security |
| **Spring Data JPA** | Data persistence            |
| **H2**              | Development database        |
| **PostgreSQL**      | Production database         |

### 📋 Requirements

* **JDK 17**
* Maven does not need to be installed separately: the project uses the included **Maven Wrapper**.

### ⚙️ Configuration

The application uses the following environment variables:

| Variable      | Description                                                                        |
| ------------- | ---------------------------------------------------------------------------------- |
| `AC_API_KEY`  | Key used by the race-results script to authenticate through the `X-API-KEY` header |
| `DB_USERNAME` | PostgreSQL database username in production                                         |
| `DB_PASSWORD` | PostgreSQL database password in production                                         |

> ⚠️ **Never commit API keys, passwords or other credentials directly to the repository.**

### 🚀 Running in Development

The `dev` profile is enabled by default and uses a **file-based H2 database**, stored in the `./data` directory.

#### Windows

```powershell
$env:AC_API_KEY="test-key"
.\mvnw.cmd spring-boot:run
```

#### Linux / macOS

```bash
export AC_API_KEY=test-key
./mvnw spring-boot:run
```

### 🔧 Profiles

#### `dev`

* File-based H2 database
* `ddl-auto: update`
* SQL logging enabled
* H2 console enabled

#### `prod`

* PostgreSQL
* `ddl-auto: validate`
* H2 console disabled

### ✨ Features

* 🔐 Steam login using OpenID
* 📅 Event calendar
* 📝 Event registration
* ⏳ Waiting list management
* 👨‍💼 Database management through the admin area (`/admin/**`)
* 📡 Race result reception through an API

The endpoint used to receive race results is:

```http
POST /admin/results/incoming
```

The Python script that sends the race results authenticates using the following header:

```http
X-API-KEY: <api-key>
```

