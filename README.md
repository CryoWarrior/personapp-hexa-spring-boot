# PersonApp Hexagonal – Spring Boot

Aplicación multi-módulo que implementa arquitectura hexagonal (puertos y adaptadores) para gestionar personas.  
Incluye adaptador REST, CLI, y dos salidas hacia MariaDB y MongoDB.

## Autores
- Diego Alejandro Jara Rojas
- Laura Isabel Blanco

## Estructura

```
common/                 → anotaciones y excepciones compartidas  
domain/                 → entidades del dominio  
application/            → casos de uso y puertos de negocio  
maria-output-adapter/   → persistencia MariaDB vía Spring Data JPA  
mongo-output-adapter/   → persistencia MongoDB vía Spring Data Mongo  
rest-input-adapter/     → API REST (Spring Boot) puerto 3000  
cli-input-adapter/      → aplicación de consola (Spring Boot CLI)
scripts/                → DDL / DML para MariaDB y MongoDB
docker-compose.yml      → orquesta REST, CLI, MariaDB y MongoDB
```

## Prerrequisitos

- Docker 24+ y Docker Compose v2.
- Opcional: Java 11 + Maven si quieres ejecutar en local sin contenedores.

## Uso con Docker

```bash
# Construir imágenes y levantar todo (rest + dbs)
docker compose up -d --build

# Ver estado
docker compose ps

# Logs de la API
docker compose logs rest-api -f
```

## Servicios disponibles

| Servicio     | Puerto | Descripción                       |
|--------------|--------|-----------------------------------|
| REST API     | 3000   | Endpoints CRUD de MR              |
| Swagger UI   | 3000   | Documentación interactiva         |
| MariaDB      | 3307   | Base de datos relacional          |
| MongoDB      | 27017  | Base NoSQL con autenticación      |
| CLI          | n/a    | Aplicación interactiva en terminal |
 
### Endpoints REST disponibles - (Swagger 3)

La API REST expone la especificación y la UI interactiva gracias a `springdoc-openapi-ui`:

- JSON: `http://localhost:3000/api-docs`
- Swagger UI: `http://localhost:3000/swagger-ui/index.html`

Desde allí puedes explorar los endpoints y probarlos directamente.

Endpoints principales:

- POST: /api/v1/mr
- GET: /api/v1/mr
- GET: /api/v1/mr/{id}
- PUT: /api/v1/mr/{id}
- DELETE: /api/v1/mr/{id}

### CLI

El CLI es interactivo; ejecútalo bajo demanda:

```bash
docker compose run --rm cli
```

Cuando termine (opción 0), el contenedor se elimina automáticamente.

## Scripts de base de datos

- `scripts/persona_ddl_maria.sql` y `scripts/persona_dml_maria.sql`: crean y poblan MariaDB.
- `scripts/persona_ddl_mongo.js` y `scripts/persona_dml_mongo.js`: crean usuario, colecciones e inserts en MongoDB.

Los scripts se aplican automáticamente al levantar los contenedores (gracias a los volúmenes montados en `docker-compose.yml`).  
Para resembrar datos, usa `docker compose down -v` y levanta de nuevo.


