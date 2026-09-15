# Lab Admin Service

Laboratorio para el desarrollo del servicio de administración de la plataforma de servicios criptográficos y certificados mediante Java y Spring Boot.

El objetivo es construir la solución de forma desacoplada, permitiendo integrar progresivamente la infraestructura de laboratorio y posteriormente componentes reales como HSM, PKI y TSA.

Actualmente este servicio concentra las funciones administrativas, autenticación de usuarios y control de acceso que posteriormente serán utilizadas por los servicios criptográficos y de certificados.

## Requisitos de desarrollo

- Java 21
- Docker Desktop
- Docker Compose
- Git

No es necesario instalar Maven globalmente. El proyecto utiliza **Maven Wrapper**.

Verificar Java:

```powershell
java -version
```

Verificar Docker:

```powershell
docker --version
docker compose version
```

## Clonar el proyecto

Clonar el repositorio:

```powershell
git clone <URL_DEL_REPOSITORIO>
```

Entrar al proyecto:

```powershell
cd lab-admin-service
```

Verificar la rama actual:

```powershell
git branch
git status
```

## Preparación del entorno

El proyecto utiliza un único archivo `.env` para centralizar la configuración local de cada desarrollador.

El mismo archivo es utilizado por:

- Docker Compose para configurar PostgreSQL.
- Spring Boot para configurar la conexión a la base de datos.
- Spring Security para la configuración JWT.
- El inicializador del usuario administrador.

Crear el archivo local a partir de la plantilla:

### Windows PowerShell

```powershell
Copy-Item .env.example .env
```

### Generar JWT_SECRET

Cada desarrollador debe generar su propio secreto para la firma de los tokens JWT.

En Windows PowerShell se puede generar un valor aleatorio de 256 bits codificado en Base64 con:

```powershell
$bytes = New-Object byte[] 32
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
[Convert]::ToBase64String($bytes)
$rng.Dispose()
```

El resultado debe colocarse en el archivo `.env`:

```env
JWT_SECRET=VALOR_GENERADO
```

El valor real de `JWT_SECRET` no debe almacenarse en `.env.example` ni subirse al repositorio.

## Configuración de PostgreSQL

Docker Compose y Spring Boot utilizan las mismas propiedades definidas en `.env`:

```text
DB_HOST
DB_PORT
DB_NAME
DB_USER
DB_PASSWORD
```

## Levantar PostgreSQL

Desde la raíz del proyecto ejecutar:

```powershell
docker compose up -d
```

Verificar el estado de los contenedores:

```powershell
docker compose ps
```

También puede verificarse directamente con:

```powershell
docker ps
```

PostgreSQL debe aparecer en estado `Up`.

## Compilar el proyecto

En Windows ejecutar:

```powershell
.\mvnw.cmd clean package
```

Maven descargará las dependencias necesarias, compilará el proyecto y ejecutará las pruebas configuradas.

El resultado esperado es:

```text
BUILD SUCCESS
```

## Ejecutar la aplicación

Iniciar Spring Boot:

```powershell
.\mvnw.cmd spring-boot:run
```

Si el arranque es correcto deberá aparecer un mensaje similar a:

```text
Started LabAdminServiceApplication
```

Por defecto, el servicio queda disponible en:

```text
http://localhost:8080
```

Durante el arranque se valida la conexión con PostgreSQL y se inicializan los componentes de persistencia y seguridad.

## Base de datos y migraciones

El proyecto utiliza:

- PostgreSQL 17
- Spring Data JPA
- Hibernate
- Flyway

Durante el inicio de la aplicación, Flyway verifica y aplica las migraciones correspondientes antes de que Hibernate inicialice las entidades.

Esto permite que un desarrollador pueda clonar el proyecto y generar la estructura necesaria de la base de datos a partir de las migraciones versionadas.

## Autenticación

Actualmente se encuentra implementado el flujo inicial de autenticación de usuarios administrativos.

El login permite validar:

- Conectividad con el backend.
- Conectividad con PostgreSQL.
- Existencia y estado del usuario administrativo.
- Validación de contraseña mediante BCrypt.
- Autenticación mediante Spring Security.
- Obtención del rol asociado al usuario.

El endpoint disponible actualmente es:

```text
POST /auth/login
```

Ejemplo de solicitud:

```json
{
  "username": "admin",
  "password": "PASSWORD_CONFIGURADO_LOCALMENTE"
}
```

Una autenticación correcta devuelve actualmente el usuario, su rol y el resultado del login.

La generación y validación de JWT para autenticar las siguientes peticiones se encuentra en desarrollo.

## Trabajo con Git

Cada desarrollador debe trabajar sobre su propia rama de desarrollo.

Ejemplo:

```text
main
 |
 +-- Dev-Oscar
 |
 +-- Dev-Compañero
```

Antes de comenzar nuevos cambios se recomienda actualizar la rama local con los últimos cambios integrados al proyecto.

Los cambios terminados deben integrarse posteriormente a `main` mediante el flujo de merge definido por el equipo.

### Configuraciones locales

Las configuraciones específicas de cada equipo no deben modificarse directamente en los archivos compartidos.

Por ejemplo, si un desarrollador utiliza PostgreSQL en:

```text
localhost:5432
```

y otro necesita:

```text
localhost:5433
```

cada uno debe configurar su propio `.env`:

```env
DB_PORT=5432
```

o:

```env
DB_PORT=5433
```

De esta forma los cambios particulares del entorno no se propagan accidentalmente al realizar merges entre ramas.

## Estado actual del laboratorio

Actualmente se ha validado:

- Clonado limpio del repositorio.
- Configuración local centralizada mediante archivo .env.
- PostgreSQL ejecutándose mediante Docker.
- Compilación mediante Maven Wrapper.
- Ejecución de pruebas durante el build.
- Migraciones de base de datos mediante Flyway.
- Persistencia mediante JPA/Hibernate.
- Configuración de Spring Security.
- Arranque correcto de Spring Boot.
- Tomcat ejecutándose en el puerto `8080`.
- Flujo de login.
- Parametrización del puerto local de PostgreSQL.

Actualmente en desarrollo:

- Generación de tokens JWT.
- Validación del token en las peticiones protegidas.
- Continuación de los endpoints administrativos.