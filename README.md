# 📋 DECLARANET

> Sistema de Declaración Patrimonial y de Intereses — Plataforma completa basada en microservicios Docker.

[![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker)](https://www.docker.com/)
[![Java](https://img.shields.io/badge/Java-8-orange?logo=java)](https://www.java.com/)
[![Angular](https://img.shields.io/badge/Angular-9-red?logo=angular)](https://angular.io/)
[![MongoDB](https://img.shields.io/badge/MongoDB-4.x-green?logo=mongodb)](https://www.mongodb.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-11-blue?logo=postgresql)](https://www.postgresql.org/)

---

## 📌 Descripción

**DECLARANET** es un sistema para la gestión y presentación de declaraciones patrimoniales y de intereses de servidores públicos. Está construido como una arquitectura de microservicios orquestada con Docker Compose, pensada para ser desplegada fácilmente en cualquier servidor con Docker instalado.

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────┐
│                  DECLARANET ECOSYSTEM                   │
├──────────────┬──────────────┬──────────────┬────────────┤
│   dnetfront  │  operfront   │   csp front  │  ip-front  │
│  (Puerto 5000)│ (Puerto 5002)│ (Puerto 5003)│(Puerto 4000)│
├──────────────┴──────────────┴──────────────┴────────────┤
│          Kong API Gateway (Puerto 5120 / 5121)          │
├─────────────────────────────────────────────────────────┤
│              Microservicios Java (Vert.x)                │
│  iniciodeclaracion │ guardadeclaracion │ consultadecla  │
│  validaciondecla   │ servicioimpresion │ recepcionfirma │
│  validadores (x15) │ catalogos (x20)  │ cumplimiento   │
├────────────────────┬────────────────────────────────────┤
│   MongoDB (x4 DBs) │     PostgreSQL (x2 DBs)            │
│  mondecla :27017   │  catalogos :5433                   │
│  amaccesos :27018  │  identidad :5434                   │
│  dependencias:27019│                                    │
│  omext :27020      │                                    │
└────────────────────┴────────────────────────────────────┘
```

---

## 🧩 Módulos Principales

| Módulo | Descripción | Puerto |
|--------|-------------|--------|
| `dnetfront` | Portal principal para declarantes | 5000 |
| `ip-front` | Proveedor de Identidad (Login) | 4000 |
| `operfront` | Panel de Operación para administradores | 5002 |
| `servidorespublicosfront` | Consulta Pública de declaraciones | 5003 |
| `kong-api-gateway` | API Gateway (Kong) | 5120 |
| `admin-panel` | Panel de administración personalizado | 8080 |
| `entes` | Catálogo de entidades (GraphQL) | 5001 |
| `registrocumplimiento` | Registro de cumplimiento | 5005 |

---

## 🗄️ Bases de Datos

### MongoDB
| Contenedor | Base de Datos | Puerto |
|-----------|--------------|--------|
| `mondecla` | `declaranet` — Declaraciones patrimoniales | 27017 |
| `amaccesos` | `amaccesos` — Gestión de accesos | 27018 |
| `dependencias` | `dependencia` — Catálogo de entes/dependencias | 27019 |
| `omext` | `omext` — Omisos y extemporáneos | 27020 |

### PostgreSQL
| Contenedor | Base de Datos | Puerto |
|-----------|--------------|--------|
| `catalogos` | Catálogos del sistema | 5433 |
| `identidad` | Usuarios y proveedor de identidad | 5434 |

---

## ⚙️ Requisitos Previos

- **Docker** ≥ 20.x
- **Docker Compose** ≥ 1.29
- **Git**
- Servidor con mínimo **8 GB RAM** y **20 GB disco**

---

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/gariarellano/DECLARANET.git
cd DECLARANET/installation-manifest
```

### 2. Configurar el archivo `.env`

Editar el archivo `.env` con los datos de tu servidor:

```env
# IP pública del servidor
IP_HOST_DOCKER=TU_IP_PUBLICA

# Dominios de acceso
DOMINIO_FRONT_DECLARANET=${IP_HOST_DOCKER}:5000
DOMINIO_IDENTIDAD=${IP_HOST_DOCKER}:4000
DOMINIO_FRONT_OPER=${IP_HOST_DOCKER}:5002
```

### 3. Crear las bases de datos

```bash
# MongoDB — Declaraciones
docker run -d -p 27017:27017 --name mondecla \
  -e MONGODB_ROOT_PASSWORD=1X2Y3Z \
  -e MONGODB_USERNAME=declausr \
  -e MONGODB_PASSWORD=usrpass123 \
  -e MONGODB_DATABASE=declaranet \
  -v /mongodb/persistence:/bitnami/mongodb \
  bitnami/mongodb:4.0

# MongoDB — Accesos
docker run -d -p 27018:27017 --name amaccesos \
  -e MONGODB_ROOT_PASSWORD=1X2Y3Z \
  -e MONGODB_USERNAME=amaccesosusr \
  -e MONGODB_PASSWORD=usrpass123 \
  -e MONGODB_DATABASE=amaccesos \
  bitnami/mongodb:4.0

# MongoDB — Dependencias
docker run -d -p 27019:27017 --name dependencias \
  -e MONGODB_ROOT_PASSWORD=1X2Y3Z \
  -e MONGODB_USERNAME=dependenciausr \
  -e MONGODB_PASSWORD=usrpass123 \
  -e MONGODB_DATABASE=dependencia \
  bitnami/mongodb:4.0

# MongoDB — Omext
docker run -d -p 27020:27017 --name omext \
  -e MONGODB_ROOT_PASSWORD=1X2Y3Z \
  -e MONGODB_USERNAME=omextusr \
  -e MONGODB_PASSWORD=usrpass123 \
  -e MONGODB_DATABASE=omext \
  bitnami/mongodb:4.2

# PostgreSQL — Catálogos
docker run -dp 5433:5432 --name catalogos \
  -e POSTGRESQL_POSTGRES_PASSWORD=1X2Y3Z \
  -e POSTGRESQL_USERNAME=catalogousr \
  -e POSTGRESQL_PASSWORD=userpass123 \
  -e POSTGRESQL_DATABASE=catalogos \
  -v /path/to/postgresql-persistence:/bitnami/postgresql \
  bitnami/postgresql:11

# PostgreSQL — Identidad
docker run -dp 5434:5432 --name identidad \
  -e POSTGRESQL_POSTGRES_PASSWORD=1X2Y3Z \
  -e POSTGRESQL_USERNAME=identidadsfpusr \
  -e POSTGRESQL_PASSWORD=userpasss123 \
  -e POSTGRESQL_DATABASE=identidad \
  -v /path/to/postgresql-persistence:/bitnami/postgresql \
  bitnami/postgresql:11
```

### 4. Ejecutar scripts de base de datos

Cargar las estructuras iniciales de PostgreSQL desde `scripts-bd/`:

```bash
# Estructura de catálogos
docker exec -i catalogos psql -U catalogousr -d catalogos < scripts-bd/bd_service_catalogos.sql

# Estructura de identidad
docker exec -i identidad psql -U identidadsfpusr -d identidad < scripts-bd/bd_service_identidad.sql
```

### 5. Levantar el sistema

```bash
# Inicialización + sistema principal
docker-compose -f docker-compose-init.yml up -d && docker-compose up -d && docker image prune

# Módulo operativo y consulta pública (opcional)
docker-compose -f docker-compose-oper-csp.yml up -d
```

---

## 📦 Registros Iniciales

Después de instalar, se deben insertar los registros mínimos para que el sistema funcione:

### PostgreSQL — Identidad

```sql
-- Registrar la aplicación DECLARANET
INSERT INTO ip_aplicacion VALUES (
  nextval('ip_aplicacion_id_aplicacion_seq'),
  'DECLARANET-CUSTER',
  '${SECRET_KEY}',
  'http://${DOMINIO_FRONT_DECLARANET}/system-log-in',
  '${DOMINIO_FRONT_DECLARANET}',
  0, 0, 'jwtdnet', 15
);
```

### MongoDB — Dependencias

Registrar el ente receptor mediante la API GraphQL en:  
`http://${IP_HOST_DOCKER}:5001/graphiql/`

---

## 🌐 URLs de Acceso

Una vez levantado el sistema, los servicios estarán disponibles en:

| Servicio | URL |
|---------|-----|
| 🏠 Portal Declaranet | `http://IP:5000` |
| 🔑 Proveedor Identidad | `http://IP:4000` |
| 🛠️ Panel Operativo | `http://IP:5002` |
| 👁️ Consulta Pública | `http://IP:5003` |
| 📊 Admin Panel | `http://IP:8080` |
| 🔗 API Gateway | `http://IP:5120` |
| 📚 Catálogo Entes (GraphQL) | `http://IP:5001/graphiql/` |

---

## 🖼️ Personalización del Login

### Logo Institucional
La imagen del logo en el login se almacena como **Base64** en MongoDB:

```javascript
db.enteReceptorDeclaracion.updateOne(
  { path: "VACIO" },
  { $set: { imagenB64: "data:image/png;base64,..." } }
)
```

> Convierte tu imagen en Base64 en: https://www.base64-image.de/

### Imagen de Fondo del Login
Reemplaza el archivo: `dnetfront/src/assets/image/loginRight.png`  
Luego reconstruye el contenedor: `docker-compose up --build -d dnetfront`

---

## 🔄 Actualización del Sistema

```bash
# En el servidor VPS
git pull

# Reconstruir y reiniciar servicios
docker-compose up --build -d
```

---

## 💾 Respaldo de Bases de Datos

```bash
# Crear directorio de respaldos
mkdir -p /root/backups

# PostgreSQL
docker exec catalogos bash -c "PGPASSWORD=userpass123 pg_dump -U catalogousr catalogos" > /root/backups/backup_catalogos_$(date +%Y%m%d).sql
docker exec identidad bash -c "PGPASSWORD=userpasss123 pg_dump -U identidadsfpusr identidad" > /root/backups/backup_identidad_$(date +%Y%m%d).sql

# MongoDB
docker exec mondecla mongodump -u declausr -p usrpass123 --authenticationDatabase declaranet --archive > /root/backups/backup_mondecla_$(date +%Y%m%d).archive

# Comprimir todo
tar -czf /root/backups_declaranet_$(date +%Y%m%d).tar.gz /root/backups/
```

---

## 🗂️ Estructura del Proyecto

```
DECLARANET/
└── installation-manifest/
    ├── .env                          # ⚙️ Configuración principal
    ├── docker-compose.yml            # 🐳 Sistema principal
    ├── docker-compose-init.yml       # 🐳 Inicialización
    ├── docker-compose-oper-csp.yml   # 🐳 Módulo operativo
    ├── scripts-bd/                   # 🗄️ Scripts SQL
    ├── dnetfront/                    # 🌐 Portal declarantes (Angular)
    ├── provedorIdentidad/            # 🔐 IP back/front (Java + Angular)
    ├── oper/                         # 🛠️ Panel operativo (Java + Angular)
    ├── csp/                          # 👁️ Consulta pública
    ├── admin-panel/                  # 📊 Panel de administración
    ├── validadores/                  # ✅ Microservicios validadores
    ├── catalogos/                    # 📚 Microservicios catálogos
    ├── kong-api-gateway/             # 🔗 API Gateway
    ├── ServicioImpresion/            # 🖨️ Generación de PDFs
    ├── MailSenderSFP/                # 📧 Servicio de correos
    ├── tokens/                       # 🔑 Generador de tokens JWT
    └── librerias/                    # 📦 Librerías internas
```

---

## 🛠️ Stack Tecnológico

| Tecnología | Uso |
|-----------|-----|
| **Java 8 + Vert.x** | Microservicios backend |
| **Angular 9** | Frontends (declarantes, operativo, CSP) |
| **Node.js** | Admin Panel |
| **MongoDB 4.x** | Base de datos de declaraciones |
| **PostgreSQL 11** | Base de datos de catálogos e identidad |
| **Kong** | API Gateway |
| **Docker + Docker Compose** | Contenedores y orquestación |
| **Nginx** | Servidor web para frontends |
| **JWT** | Autenticación y autorización |

---

## 📄 Licencia

Este proyecto fue desarrollado para uso gubernamental. Consultar términos de uso con el responsable del sistema.

---

## 👤 Contacto

Repositorio mantenido por **@gariarellano**  
GitHub: [https://github.com/gariarellano/DECLARANET](https://github.com/gariarellano/DECLARANET)
