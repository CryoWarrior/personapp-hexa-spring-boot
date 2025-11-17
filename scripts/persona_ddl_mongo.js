const adminDb = db.getSiblingDB("admin");
const personaDb = db.getSiblingDB("persona_db");

if (!adminDb.getUser("persona_db")) {
  adminDb.createUser({
    user: "persona_db",
    pwd: "persona_db",
    roles: [
      { role: "read", db: "persona_db" },
      { role: "readWrite", db: "persona_db" },
      { role: "dbAdmin", db: "persona_db" }
    ],
    mechanisms: ["SCRAM-SHA-1", "SCRAM-SHA-256"]
  });
  print("Created MongoDB application user persona_db");
} else {
  adminDb.updateUser("persona_db", {
    roles: [
      { role: "read", db: "persona_db" },
      { role: "readWrite", db: "persona_db" },
      { role: "dbAdmin", db: "persona_db" }
    ]
  });
  print("MongoDB application user persona_db already existed - roles updated");
}

// Crear colección persona
personaDb.createCollection("persona", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "nombre", "apellido", "genero"],
      properties: {
        _id: {
          bsonType: "int",
          description: "Identificación de la persona (cédula)"
        },
        nombre: {
          bsonType: "string",
          maxLength: 45,
          description: "Nombre de la persona"
        },
        apellido: {
          bsonType: "string", 
          maxLength: 45,
          description: "Apellido de la persona"
        },
        genero: {
          enum: ["M", "F"],
          description: "Género: M para Masculino, F para Femenino"
        },
        edad: {
          bsonType: ["int", "null"],
          minimum: 0,
          maximum: 150,
          description: "Edad de la persona (opcional)"
        }
      }
    }
  }
})

// Crear colección profesion
personaDb.createCollection("profesion", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "nom"],
      properties: {
        _id: {
          bsonType: "int",
          description: "ID único de la profesión"
        },
        nom: {
          bsonType: "string",
          maxLength: 90,
          description: "Nombre de la profesión"
        },
        des: {
          bsonType: ["string", "null"],
          description: "Descripción de la profesión (opcional)"
        }
      }
    }
  }
})

// Crear colección telefono
personaDb.createCollection("telefono", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "oper", "primaryDuenio"],
      properties: {
        _id: {
          bsonType: "string",
          maxLength: 15,
          description: "Número de teléfono (PK)"
        },
        oper: {
          bsonType: "string",
          maxLength: 45,
          description: "Operador/Compañía telefónica"
        },
        primaryDuenio: {
          bsonType: "object",
          description: "Referencia al documento persona propietario"
        }
      }
    }
  }
})

// Crear colección estudios
personaDb.createCollection("estudios", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "primaryPersona", "primaryProfesion"],
      properties: {
        _id: {
          bsonType: "string",
          description: "ID compuesto: personaId-profesionId"
        },
        primaryPersona: {
          bsonType: "object",
          description: "Referencia al documento persona"
        },
        primaryProfesion: {
          bsonType: "object",
          description: "Referencia al documento profesion"
        },
        fecha: {
          bsonType: ["date", "null"],
          description: "Fecha de graduación (opcional)"
        },
        univer: {
          bsonType: ["string", "null"],
          maxLength: 50,
          description: "Universidad donde se realizó el estudio (opcional)"
        }
      }
    }
  }
})

// Crear índices para optimizar consultas de referencias
// Índices para telefono
personaDb.telefono.createIndex({"primaryDuenio.$id": 1}, {name: "idx_telefono_duenio"})

// Índices para estudios
personaDb.estudios.createIndex({"primaryPersona.$id": 1}, {name: "idx_estudios_persona"})
personaDb.estudios.createIndex({"primaryProfesion.$id": 1}, {name: "idx_estudios_profesion"})

// Índices adicionales para consultas frecuentes
personaDb.persona.createIndex({"genero": 1}, {name: "idx_persona_genero"})
personaDb.persona.createIndex({"nombre": 1, "apellido": 1}, {name: "idx_persona_nombre_completo"})
personaDb.profesion.createIndex({"nom": 1}, {name: "idx_profesion_nombre"})
