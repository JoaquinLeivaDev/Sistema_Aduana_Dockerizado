# aduana-api

## Descripción
Microservicio base del sistema de Aduana. Contiene la lógica de negocio principal, persistencia real con JPA + Hibernate sobre Oracle Cloud, y expone endpoints REST consumidos por los microservicios orquestadores del sistema. Forma parte de una arquitectura de microservicios desarrollada con Spring Boot.

## Integrantes
- Joaquín Leiva
- Octavio Echeverría
- Thiara Rojas
- Luna Bustamante

## Otros Microservicios
- `api-gateway`: Enrutamiento centralizado.
- `aduana-usuarios-api`: Gestión de usuarios y roles.
- `aduana-reportes-api`: Gestión de reportes.
- `aduana-eureka-server`: Descubrimiento de servicios.

## Documentación Swagger
- Local: `http://localhost:8080/swagger-ui/index.html`

## Ejecución
### Local
```bash
./mvnw.cmd spring-boot:run
```
### Remota
Requiere configurar variables de entorno en el panel de despliegue (ej. Railway/Render): `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.3.5
- Spring Data JPA + Hibernate
- Oracle Cloud (conexión vía Wallet)
- Spring Boot Validation
- Lombok
- Maven

## Entidades del dominio

| Entidad | Descripción |
|---------|-------------|
| `Usuario` | Usuarios del sistema con rol asignado |
| `Rol` | Roles de acceso del sistema |
| `Pasajero` | Pasajeros registrados en aduana |
| `Documento` | Documentos asociados a pasajeros |
| `Vehiculo` | Vehículos con generación de SATVA |
| `Turno` | Turnos asignados a pasajeros |
| `Reporte` | Reportes generados por usuarios |

## Endpoints REST

### Usuarios `/api/v1/usuarios`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/usuarios` | Lista todos los usuarios |
| GET | `/api/v1/usuarios/{id}` | Obtiene un usuario por ID |
| GET | `/api/v1/usuarios/{id}/reportes` | Obtiene reportes de un usuario |
| POST | `/api/v1/usuarios` | Registra un nuevo usuario |
| POST | `/api/v1/usuarios/login` | Autenticación de usuario |
| POST | `/api/v1/usuarios/{id}/logout` | Cierre de sesión |
| PUT | `/api/v1/usuarios/{id}` | Actualiza un usuario |
| DELETE | `/api/v1/usuarios/{id}` | Elimina un usuario |

### Pasajeros `/api/v1/pasajeros`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/pasajeros` | Lista todos los pasajeros |
| GET | `/api/v1/pasajeros/{id}` | Obtiene un pasajero por ID |
| GET | `/api/v1/pasajeros/rut/{rut}` | Obtiene un pasajero por RUT |
| POST | `/api/v1/pasajeros` | Registra un nuevo pasajero |
| PUT | `/api/v1/pasajeros/{id}` | Actualiza un pasajero |
| DELETE | `/api/v1/pasajeros/{id}` | Elimina un pasajero |

### Vehículos `/api/v1/vehiculos`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/vehiculos` | Lista todos los vehículos |
| GET | `/api/v1/vehiculos/{id}` | Obtiene un vehículo por ID |
| GET | `/api/v1/vehiculos/patente/{patente}` | Obtiene un vehículo por patente |
| POST | `/api/v1/vehiculos` | Registra un nuevo vehículo |
| PUT | `/api/v1/vehiculos/{id}` | Actualiza un vehículo |
| PATCH | `/api/v1/vehiculos/{id}/satva` | Genera SATVA para el vehículo |
| DELETE | `/api/v1/vehiculos/{id}` | Elimina un vehículo |

### Reportes `/api/v1/reportes`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/reportes` | Lista todos los reportes |
| GET | `/api/v1/reportes/{id}` | Obtiene un reporte por ID |
| GET | `/api/v1/reportes/usuario/{idUsuario}` | Obtiene reportes por usuario |
| GET | `/api/v1/reportes/tipo/{tipo}` | Obtiene reportes por tipo |
| POST | `/api/v1/reportes` | Registra un nuevo reporte |
| DELETE | `/api/v1/reportes/{id}` | Elimina un reporte |

### Roles `/api/v1/roles`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/roles` | Lista todos los roles |
| POST | `/api/v1/roles` | Crea un nuevo rol |

### Turnos `/api/v1/turnos`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/turnos` | Lista todos los turnos |
| GET | `/api/v1/turnos/{id}` | Obtiene un turno por ID |
| POST | `/api/v1/turnos` | Crea un nuevo turno |
| DELETE | `/api/v1/turnos/{id}` | Elimina un turno |

## Estructura del proyecto

```
src/main/java/cl/duocuc/aduana_api/
├── controller/
│   ├── DocumentoController.java
│   ├── PasajeroController.java
│   ├── ReporteController.java
│   ├── RolController.java
│   ├── TurnoController.java
│   ├── UsuarioController.java
│   └── VehiculoController.java
├── dto/
│   ├── ApiResponse.java
│   ├── DocumentoRequestDTO.java
│   ├── DocumentoResponseDTO.java
│   ├── LoginRequestDTO.java
│   ├── PasajeroRequestDTO.java
│   ├── PasajeroResponseDTO.java
│   ├── ReporteRequestDTO.java
│   ├── ReporteResponseDTO.java
│   ├── RolRequestDTO.java
│   ├── RolResponseDTO.java
│   ├── TurnoRequestDTO.java
│   ├── TurnoResponseDTO.java
│   ├── UsuarioRequestDTO.java
│   ├── UsuarioResponseDTO.java
│   ├── VehiculoRequestDTO.java
│   └── VehiculoResponseDTO.java
├── exception/
│   ├── DocumentoNotFoundException.java
│   ├── GlobalExceptionHandler.java
│   ├── PasajeroNotFoundException.java
│   ├── ReporteNotFoundException.java
│   ├── RolNotFoundException.java
│   ├── TurnoNotFoundException.java
│   ├── UsuarioNotFoundException.java
│   └── VehiculoNotFoundException.java
├── model/
│   ├── Documento.java
│   ├── Pasajero.java
│   ├── Reporte.java
│   ├── Rol.java
│   ├── Turno.java
│   ├── Usuario.java
│   └── Vehiculo.java
├── repository/
│   ├── DocumentoRepository.java
│   ├── PasajeroRepository.java
│   ├── ReporteRepository.java
│   ├── RolRepository.java
│   ├── TurnoRepository.java
│   ├── UsuarioRepository.java
│   └── VehiculoRepository.java
├── service/
│   ├── DocumentoService.java
│   ├── PasajeroService.java
│   ├── ReporteService.java
│   ├── RolService.java
│   ├── TurnoService.java
│   ├── UsuarioService.java
│   └── VehiculoService.java
└── AduanaApiApplication.java
```
