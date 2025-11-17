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

Servicios:

- REST API: `http://localhost:3000`
- OpenAPI docs: `http://localhost:3000/api-docs`
- MariaDB: `localhost:3307` (`persona_db` / `persona_db`)
- MongoDB: `localhost:27017` (usuario `persona_db` / `persona_db`, auth DB `admin`)

### Endpoints REST disponibles

| Método | Ruta                         | Descripción                                                    |
| ------ | ---------------------------- | -------------------------------------------------------------- |
| GET    | `/api/v1/persona/{database}` | Lista personas desde `MARIA` o `MONGO` (parámetro en la ruta). |
| POST   | `/api/v1/persona`            | Crea una persona en la base indicada en el body (`database`).  |

Ejemplos:

```bash
# Listar desde MariaDB
curl http://localhost:3000/api/v1/persona/maria

# Listar desde MongoDB
curl http://localhost:3000/api/v1/persona/mongo

# Crear una persona en MariaDB
curl -X POST http://localhost:3000/api/v1/persona \
  -H "Content-Type: application/json" \
  -d '{"dni":"1001","firstName":"Juan","lastName":"Perez","age":"30","sex":"MALE","database":"MARIA"}'
```
### Documentación OpenAPI (Swagger 3)

La API REST expone la especificación y la UI interactiva gracias a `springdoc-openapi-ui`:

- JSON: `http://localhost:3000/api-docs`
- Swagger UI: `http://localhost:3000/swagger-ui/index.html`

Desde allí puedes explorar los endpoints `/api/v1/persona` y probarlos directamente.

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


