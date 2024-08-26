# Financial Transactions Service

## Descripción

El proyecto **Financial Transactions Service** es una aplicación backend para la gestión de transacciones financieras. Implementada en Java con Spring Boot, esta aplicación proporciona servicios RESTful para realizar operaciones relacionadas con personas y transacciones dentro de un sistema bancario. La aplicación está diseñada para ser fácil de desplegar y extender, utilizando Docker para la contenedorización y PostgreSQL como base de datos.

## Tecnologías Utilizadas

- **Java 17**: Lenguaje de programación principal.
- **Spring Boot**: Framework para la construcción de aplicaciones Java basadas en Spring.
- **PostgreSQL**: Sistema de gestión de bases de datos relacional.
- **Docker**: Plataforma para la creación y gestión de contenedores.
- **JPA/Hibernate**: Herramientas para la persistencia de datos.

## Estructura del Proyecto

- **Modelos**: Clases que representan las entidades del sistema, como `Persona`.
- **Repositorios**: Interfaces para la interacción con la base de datos.
- **Servicios**: Lógica de negocio y operaciones del sistema.
- **Controladores**: Exponen los endpoints REST para las operaciones de la API.

## Instrucciones de Despliegue

### 1. Configuración del Entorno

Asegurarse de tener Docker y Docker Compose instalados en la máquina. Podemos verificar esto ejecutando `docker --version` y `docker-compose --version` en el terminal.

### 2. Clonar el Repositorio

```bash
git clone https://github.com/yaandrango/financial-transactions-service.git
cd financial-transactions-service
```

### 3. Construcción y Despliegue

1. **Crear y Ejecutar los Contenedores**

   Ejecutar el siguiente comando para construir y lanzar los contenedores:

   ```bash
   docker-compose up --build
   ```
   Esto creará y levantará los contenedores para la aplicación backend y la base de datos.

2. **Acceder a la Aplicación**

La aplicación estará disponible en http://localhost:8080. Podemos utilizar herramientas como Postman para probar los endpoints.

### 4. Datos Iniciales

Para datos iniciales en la base de datos, consulta el archivo BaseDatos.sql que se encuentra en la carpeta docs. Podemos usar este archivo para importar datos en la base de datos en tu entorno local o en Docker.

## Endpoints

Aquí se detallan algunos de los endpoints disponibles en la aplicación:

    GET /api/personas/listar: Obtiene una lista de todas las personas.
    POST /api/personas/crear: Crea una nueva persona.
    GET /api/clientes/listar: Obtiene una lista de todos los clientes.
    POST /api/clientes/crear: Crea un nuevo cliente.
    GET /api/cuentas/listar: Obtiene una lista de todas las cuentas.
    POST /api/cuentas/crear: Crea una nueva cuenta.
    GET /api/movimientos/listar: Obtiene una lista de todos los movimientos.
    POST /api/movimientos/crear: Crea un nuevo movimiento.

## Postman Collection

La colección de Postman para probar los endpoints está disponible en docs/Financial Transaction.postman_collection.json. Podemos importarla en Postman para realizar pruebas de integración.

## Contacto

Para cualquier consulta adicional, puedes contactar a:

- **Nombre**: Yoselin Andrango
- **Email**: yoselinanabel@gmail.com
