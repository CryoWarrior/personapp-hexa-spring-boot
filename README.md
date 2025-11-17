# PersonApp Hexagonal – Spring Boot (Docker Ready)

Aplicación multi-módulo que implementa arquitectura hexagonal (puertos y adaptadores) para gestionar personas.  
Incluye adaptador REST, CLI, y dos salidas hacia MariaDB y MongoDB. Todo el stack puede levantarse en contenedores.

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

## Ejecución local (opcional)

1. Instala MariaDB (puerto 3307) y MongoDB (puerto 27017).
2. Ejecuta los scripts del directorio `scripts/`.
3. Compila con Maven:
   ```bash
   mvn clean package
   ```
4. Ejecuta:
   - REST: `mvn -pl rest-input-adapter spring-boot:run`
   - CLI: `mvn -pl cli-input-adapter spring-boot:run`

## Notas

- El proyecto usa Lombok; habilítalo en tu IDE.
- Hay dos aplicaciones Spring Boot independientes (REST y CLI).
- Si haces fork para trabajar tu laboratorio, evita modificar el repo base.
