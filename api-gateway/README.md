# API Gateway - Sistema Aduana

## Descripción
API Gateway centralizado que actúa como punto único de entrada para el sistema de microservicios de Aduana. Realiza el enrutamiento estático hacia los servicios correspondientes, simplificando la comunicación con el cliente.

## Integrantes
- Joaquín Leiva
- Octavio Echeverría
- Thiara Rojas
- Luna Bustamante

## Microservicios gestionados
- `aduana-api`: Lógica central de negocio y persistencia.
- `aduana-usuarios-api`: Gestión de usuarios y roles.
- `aduana-reportes-api`: Gestión y generación de reportes.

## Rutas del Gateway (Puerto 8090)
- `/aduana/**` -> `http://localhost:8080`
- `/usuarios/**` -> `http://localhost:8082`
- `/reportes/**` -> `http://localhost:8081`

## Ejecución
### Local
```bash
./mvnw.cmd spring-boot:run
```
### Remota
Configurar las rutas en el panel de despliegue según el entorno.
