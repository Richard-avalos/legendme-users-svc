# LegendMe Users Service

LegendMe Users Service es un microservicio para la gestión de usuarios.
Permite registrar usuarios locales o mediante Google, actualizar datos, desactivar usuarios, y consultar información de usuarios.
Construido con **Spring Boot**, **MySQL**, y **Maven**.

---

## 🚀 Tecnologías y Versiones

* **Java**: 21
* **Maven**: 3.9.9
* **Base de Datos**: MySQL
* **Spring Boot**: 3.4.3
* **JPA/Hibernate**: Spring Data JPA

---

## 🛠️ Requisitos del Sistema

1. **Java 21**

   ```bash
   java -version
   ```
2. **Maven 3.9.9**

   ```bash
   mvn -version
   ```
3. **MySQL**
   Asegurarse de tener un servidor MySQL corriendo y accesible.

---

## 📦 Configuración del Proyecto

### 1️⃣ Clonar el proyecto

```bash
git clone https://github.com/tu-usuario/legendme-users-svc.git
cd legendme-users-svc
```

### 2️⃣ Configurar la base de datos

Importa la base de datos desde un backup (`user_bd_test.sql`) o crea la base de datos.

#### Crear archivo `.env`

En la raíz del proyecto (donde está el `pom.xml`):

```env
DB_URL=jdbc:mysql://localhost:3306/user_bd_test
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contraseña
```

> ⚠️ Ajusta la URL, usuario y contraseña según tu instalación.

### 3️⃣ Ejecutar el proyecto

```bash
mvn spring-boot:run
```

La aplicación debería estar corriendo en [http://localhost:8080](http://localhost:8080)

---
## 🔒 Autenticación y Headers

A partir de las últimas versiones, **casi todos los endpoints** requieren un token **JWT**.  
Debe incluirse en el header de la solicitud:

```http

Authorization: Bearer <tu_token_jwt>

```http
```
## 📚 Endpoints principales

### 1️⃣ Crear usuario local

**POST /api/users**

**Request Body:**

```json
{
  "name": "Richard",
  "lastname": "Ávalos",
  "username": "richard01",
  "birthDate": "2001-05-12",
  "email": "richard@example.com",
  "provider": "LOCAL",
  "active": true,
  "password": "123456"
}
```

**Respuesta Exitosa (201 Created):**

```json
{
  "id": "uuid-generado",
  "name": "Richard",
  "lastname": "Ávalos",
  "username": "richard01",
  "birthDate": "2001-05-12",
  "email": "richard@example.com",
  "provider": "LOCAL",
  "active": true,
  "createdAt": "2025-10-14T15:30:00Z",
  "updatedAt": "2025-10-14T15:30:00Z"
}
```

### 2️⃣ Crear/Upsert usuario Google

**POST /api/users/google**

**Request Body:**

```json
{
  "name": "Nicole",
  "lastname": "Hernandez",
  "username": "nicoco",
  "birthDate": "2001-05-12",
  "email": "nicole@example.com",
  "provider": "GOOGLE"
}
```

**Respuesta Exitosa (200 OK):**

```json
{
  "id": "uuid-existente-o-nuevo",
  "name": "Nicole",
  "lastname": "Hernandez",
  "username": "nicoco",
  "birthDate": "2001-05-12",
  "email": "nicole@example.com",
  "provider": "GOOGLE",
  "active": true,
  "createdAt": "2025-10-13T19:44:38Z",
  "updatedAt": "2025-10-13T19:44:38Z"
}
```

### 3️⃣ Actualizar usuario parcialmente

**PATCH /api/users/{id}**

**Request Body:** (solo los campos que se desean actualizar)

```json
{
  "name": "Richard Updated",
  "username": "richard02",
  "email": "richard2@example.com"
}
```

**Respuesta Exitosa (200 OK):**

```json
{
  "id": "uuid-del-usuario",
  "name": "Richard Updated",
  "lastname": "Ávalos",
  "username": "richard02",
  "birthDate": "2001-05-12",
  "email": "richard2@example.com",
  "provider": "LOCAL",
  "active": true,
  "createdAt": "2025-10-14T15:30:00Z",
  "updatedAt": "2025-10-14T16:00:00Z"
}
```

### 4️⃣ Obtener usuario por email

**POST /api/users/by-email**

**Request Body:**

```json
{
  "email": "richard@example.com"
}
```

**Respuesta Exitosa (200 OK):**

```json
{
  "id": "uuid-del-usuario",
  "name": "Richard",
  "lastname": "Ávalos",
  "username": "richard01",
  "birthDate": "2001-05-12",
  "email": "richard@example.com",
  "provider": "LOCAL",
  "active": true,
  "createdAt": "2025-10-14T15:30:00Z",
  "updatedAt": "2025-10-14T15:30:00Z"
}
```

### 5️⃣ Obtener usuario por username

**POST /api/users/by-username**

**Request Body:**

```json
{
  "username": "richard01"
}
```

**Respuesta Exitosa (200 OK):**

```json
{
  "id": "uuid-del-usuario",
  "name": "Richard",
  "lastname": "Ávalos",
  "username": "richard01",
  "birthDate": "2001-05-12",
  "email": "richard@example.com",
  "provider": "LOCAL",
  "active": true,
  "createdAt": "2025-10-14T15:30:00Z",
  "updatedAt": "2025-10-14T15:30:00Z"
}
```

### 6️⃣ Listar todos los usuarios

**GET /api/users**

**Respuesta Exitosa (200 OK):**

```json
[
  {
    "id": "uuid-del-usuario",
    "name": "Richard",
    "lastname": "Ávalos",
    "username": "richard01",
    "birthDate": "2001-05-12",
    "email": "richard@example.com",
    "provider": "LOCAL",
    "active": true,
    "createdAt": "2025-10-14T15:30:00Z",
    "updatedAt": "2025-10-14T15:30:00Z"
  }
]
```

### 7️⃣ Desactivar usuario (soft delete)

**DELETE /api/users/{id}**

**Respuesta Exitosa (204 No Content)**

> El usuario queda marcado como `active: false`.

### ⚠️ Casos de Error Comunes

| Código | Descripción                                 | Ejemplo JSON                              |
| ------ | ------------------------------------------- | ----------------------------------------- |
| 400    | Campos obligatorios vacíos o email inválido | { "error": "Campo 'email' es requerido" } |
| 404    | Usuario no encontrado                       | { "error": "Usuario no encontrado" }      |
| 409    | Email o username ya registrado              | { "error": "El email ya está en uso" }    |
| 500    | Error interno del servidor                  | { "error": "Error inesperado" }           |

### 8️⃣ Ejemplo curl para crear usuario

```bash
curl -X POST http://localhost:8080/api/users \
-H "Content-Type: application/json" \
-d '{
  "name": "Richard",
  "lastname": "Ávalos",
  "username": "richard01",
  "birthDate": "2001-05-12",
  "email": "richard@example.com",
  "provider": "LOCAL",
  "active": true,
  "password": "123456"
}'
```
