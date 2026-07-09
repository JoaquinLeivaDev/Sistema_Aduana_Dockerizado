# Sistema Aduana Dockerizado

Sistema de gestión aduanera basado en microservicios, con enrutamiento centralizado mediante API Gateway y descubrimiento de servicios con Eureka. El proyecto está completamente dockerizado para facilitar su despliegue y ejecución.

## Arquitectura

El sistema está compuesto por los siguientes servicios:

- **api-gateway**: Punto de entrada único al sistema. Enruta las peticiones hacia los microservicios correspondientes usando Spring Cloud Gateway.
- **eureka-server**: Servidor de descubrimiento de servicios (Netflix Eureka). Permite que los microservicios se registren y se localicen entre sí dinámicamente.
- **aduana-api**: Microservicio encargado de la gestión de los procesos y trámites aduaneros.
- **aduana-usuarios-api**: Microservicio encargado de la gestión de usuarios del sistema (registro, autenticación, roles, etc.).
- **aduana-reportes-api**: Microservicio encargado de la generación y consulta de reportes del sistema.

## Tecnologías utilizadas

- Java / Spring Boot
- Spring Cloud Gateway
- Netflix Eureka (Service Discovery)
- Docker / Docker Compose
- Swagger / OpenAPI (documentación de APIs)
- Mockito (pruebas unitarias aisladas)

## Integrantes y responsabilidades

### Joaquín Leiva
- Desarrollo del microservicio **aduana-api**: lógica de negocio, endpoints y persistencia de los procesos aduaneros.
- Configuración del **api-gateway** (rutas, filtros de reescritura de path) y su integración con Eureka.

### Luna Bustamante
- Configuración y despliegue del **api-gateway** y del **eureka-server**.
- Definición de las rutas de enrutamiento hacia cada microservicio y su registro dinámico en Eureka.

### Thiara Rojas
- Desarrollo del microservicio **aduana-reportes-api**: endpoints de generación y consulta de reportes.

### Octavio Echeverría
- Documentación de las APIs con **Swagger/OpenAPI**: anotaciones `@Operation`, `@ApiResponses` y descripción de códigos de respuesta en los controladores de los distintos microservicios.
- Implementación de pruebas unitarias con **Mockito**, aisladas de la base de datos, en reemplazo de los tests que dependían de un contexto de Spring completo.

## Cómo levantar el proyecto

```bash
docker-compose up --build
```

Esto levanta todos los servicios (Eureka, API Gateway y los tres microservicios) en la red interna de Docker.

## Acceso a los servicios

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8090

## Documentación de APIs (Swagger)

A través del API Gateway:

- Aduana API: http://localhost:8090/aduana/swagger-ui.html
- Usuarios API: http://localhost:8090/usuarios/swagger-ui.html
- Reportes API: http://localhost:8090/reportes/swagger-ui.html

## Pruebas

Las pruebas unitarias de los microservicios se ejecutan de forma aislada mediante **Mockito**, sin necesidad de levantar una base de datos real ni el contexto completo de Spring.

```bash
./mvnw test
```
