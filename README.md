# Banco App -- Reto Tecnico (Backend + Frontend + Docker)

Este repositorio contiene la solucion completa al reto tecnico
"Arquitectura Microservicio", que incluye:

-   **Backend Java Spring Boot**
-   **Frontend Angular**
-   **Pruebas unitarias**
-   **Reporte PDF en Base64**
-   **Persistencia en PostgreSQL**
-   **Despliegue con Docker**
-   **Coleccion de Postman**
-   **Script de base de datos (BaseDatos.sql)**

------------------------------------------------------------------------

## üìå 1. Arquitectura General

La solucion esta dividida en dos proyectos:

    /banco-backend
    /banco-frontend

Ambos proyectos pueden ejecutarse localmente o dentro de contenedores
Docker usando `docker-compose`.

------------------------------------------------------------------------

## üß± 2. Backend -- Spring Boot

### Tecnologias usadas

-   Java 17\
-   Spring Boot 3.x\
-   Gradle o Maven (dependiendo de la version final requerida)\
-   JPA / Hibernate\
-   PostgreSQL\
-   OpenPDF para generacion de PDF\
-   JUnit 5 + Mockito\
-   Dockerfile

### Entidades implementadas

  Entidad          Descripcion
  ---------------- --------------------------------------------------------
  **Persona**      Clase base con datos personales
  **Cliente**      Extiende Persona; contiene clienteId, estado, password
  **Cuenta**       Numero cuenta, tipo, saldo inicial, estado
  **Movimiento**   Fecha, tipo (CREDITO o DEBITO), valor, saldo

> Se utiliza la estrategia recomendada: **Persona como entidad real en
> base de datos** (siguiendo el PDF del reto).

### Endpoints

  Metodo   URL              Descripcion
  -------- ---------------- ---------------------------
  GET      /clientes        Listar clientes
  POST     /clientes        Crear cliente
  PUT      /clientes/{id}   Editar
  DELETE   /clientes/{id}   Eliminar
  GET      /cuentas         Listar cuentas
  POST     /cuentas         Crear
  GET      /movimientos     Listar
  POST     /movimientos     Registrar movimiento
  GET      /reportes        Reporte JSON + PDF Base64

### Validaciones

-   Un cliente puede tener multiples cuentas\
-   CREDITO suma saldo\
-   DEBITO resta saldo\
-   Si saldo es 0 y el movimiento es DEBITO ?error: **"Saldo no
    disponible"**\
-   Reporte devuelve todas las cuentas del cliente + movimientos + PDF
    base64

------------------------------------------------------------------------

## üß™ 3. Pruebas Unitarias

### Backend

-   Pruebas con JUnit5 y Mockito\
-   Se incluyen al menos **dos pruebas de endpoints**, como pide el reto

### Frontend

-   Pruebas realizadas con **Jest**\
-   Se validan:
    -   carga de datos\
    -   errores\
    -   envio de formularios\
    -   uso de servicios mock

------------------------------------------------------------------------

## üñ•Ô∏?4. Frontend -- Angular

### Tecnologias

-   Angular 18\
-   Jest (Testing)\
-   RxJS\
-   HTML + SCSS (no frameworks de UI)\
-   Arquitectura limpia en `core/` `features/` `shared/`

### Funcionalidades

-   CRUD de Cliente\
-   CRUD de Cuenta\
-   CRUD de Movimiento\
-   Reporte con descarga de PDF\
-   Busqueda rapida (filtro)\
-   Validaciones visibles

------------------------------------------------------------------------

## üóÑÔ∏?5. BaseDatos.sql

Incluye:

-   Creacion de tablas\
-   Datos de ejemplo\
-   Relaciones\
-   UUIDs\
-   Compatibilidad con PostgreSQL

------------------------------------------------------------------------

## üê≥ 6. Despliegue con Docker

### 6.1 Ejecutar backend en Docker

Dentro de `/banco-backend`:

    docker build -t banco-backend .
    docker run -p 8080:8080 --env-file=.env banco-backend

### 6.2 Ejecutar frontend en Docker

Dentro de `/banco-frontend`:

    docker build -t banco-frontend .
    docker run -p 4200:80 banco-frontend

### 6.3 Ejecutar todo junto via docker-compose

Desde la raiz del repo:

    docker compose up -d

Containers levantados:

    postgres
    banco-backend
    banco-frontend

------------------------------------------------------------------------

## üß™ 7. Postman

Se incluye:

-   `/postman/banco.postman_collection.json`
-   Permite validar todos los endpoints

------------------------------------------------------------------------

## üßæ 8. Como probar

### Backend

    http://localhost:8080/api/clientes
    http://localhost:8080/api/cuentas
    http://localhost:8080/api/movimientos
    http://localhost:8080/api/reportes?fechaInicio=...&fechaFin=...&clienteId=...

### Frontend

    http://localhost:4200

### Pruebas frontend

    npm test

### Pruebas backend

    mvn test

------------------------------------------------------------------------

## üì¨ 9. Entregables solicitados

‚ú?Backend completo\
‚ú?Frontend completo\
‚ú?BaseDatos.sql\
‚ú?Dockerfile backend\
‚ú?Dockerfile frontend\
‚ú?docker-compose.yml\
‚ú?Pruebas backend\
‚ú?Pruebas frontend\
‚ú?Postman\
‚ú?README

------------------------------------------------------------------------

## üë®‚Äçüí?Autor

Ejecutado por: **Manuel Cepeda**
