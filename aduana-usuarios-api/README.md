# aduana-usuarios-api

## Descripción
Microservicio de gestión de **usuarios y roles** del Sistema Aduana. Posee persistencia propia en Oracle y consume servicios de reportes vía Feign Client.

## Integrantes
- Joaquín Leiva
- Octavio Echeverría
- Thiara Rojas
- Luna Bustamante

## Otros Microservicios
- `aduana-api`: Lógica central de negocio y persistencia.
- `api-gateway`: Enrutamiento centralizado.
- `aduana-reportes-api`: Gestión de reportes.
- `aduana-eureka-server`: Descubrimiento de servicios.

## Documentación Swagger
- Local: `http://localhost:8082/swagger-ui.html`

## Ejecución
### Local
```bash
./mvnw.cmd spring-boot:run
```
### Remota
Requiere configurar variables de entorno: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `REPORTES_API_URL`.

---

## Tecnologías utilizadas

- Java 17 / Spring Boot 3.3.5
- Spring Data JPA + Oracle (wallet)
- Spring Cloud OpenFeign (consumo de aduana-reportes-api)
- Spring Cloud Netflix Eureka Client (perfil local)
- Spring Security Crypto (BCrypt para contraseñas)
- springdoc-openapi (Swagger UI)
- JUnit 5 + Mockito
- JaCoCo (cobertura de pruebas)

## Arquitectura de datos

| Entidad | Dueño de la persistencia |
|---|---|
| Usuario, Rol | **aduana-usuarios-api** (este servicio) |
| Pasajero, Vehículo, Documento, Turno | aduana-api |
| Reporte | aduana-reportes-api |

`Reporte` no tiene una relación JPA directa con `Usuario` (entidades en distintos servicios); guarda `idUsuario` como referencia simple y la validación cruzada se hace vía REST.

## Endpoints REST

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/usuarios` | Lista todos los usuarios |
| GET | `/api/v1/usuarios/{id}` | Obtiene un usuario por id |
| POST | `/api/v1/usuarios` | Crea un nuevo usuario (password hasheada con BCrypt) |
| POST | `/api/v1/usuarios/login` | Autentica username/password |
| PUT | `/api/v1/usuarios/{id}` | Actualiza un usuario |
| DELETE | `/api/v1/usuarios/{id}` | Elimina un usuario |
| GET | `/api/v1/usuarios/{id}/reportes` | Reportes del usuario (Feign → aduana-reportes-api) |
| GET | `/api/v1/roles` | Lista todos los roles |
| GET | `/api/v1/roles/{id}` | Busca un rol por id |
| POST | `/api/v1/roles` | Crea un nuevo rol |

## Estructura del proyecto

```
src/main/java/cl/duocuc/aduana_usuarios_api/
├── client/        -> ReportesClient (Feign hacia aduana-reportes-api)
├── config/        -> PasswordConfig (BCryptPasswordEncoder)
├── controller/     -> UsuarioController, RolController
├── dto/           -> Request/Response DTOs, ApiResponse
├── exception/      -> Excepciones de dominio + GlobalExceptionHandler
├── model/         -> Usuario, Rol (entidades JPA)
├── repository/     -> UsuarioRepository, RolRepository
└── service/       -> UsuarioService, RolService
```

## Ejecución local

1. Copia `.env.example` a `.env` y completa tus credenciales de Oracle (nunca subir `.env`).
2. Levanta el Eureka Server (puerto 8761) si quieres descubrimiento dinámico.
3. `./mvnw.cmd spring-boot:run`

## Ejecución con Docker

```bash
docker build -t aduana-usuarios-api .
docker run -p 8082:8082 --env-file .env aduana-usuarios-api
```

## Ejecución remota (Render)

Configurar como variables de entorno en el panel de Render: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `REPORTES_API_URL`, y activar el perfil `prod` con `SPRING_PROFILES_ACTIVE=prod`.

## Pruebas unitarias

```bash
./mvnw.cmd test
./mvnw.cmd jacoco:report   # reporte en target/site/jacoco/index.html
```
