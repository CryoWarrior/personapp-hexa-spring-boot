package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;

@Mapper
public class EstudiosMapperMaria {



	public EstudiosEntity fromDomainToAdapter(Study study) {
		EstudiosEntityPK estudioPK = new EstudiosEntityPK();
		estudioPK.setCcPer(study.getPerson().getIdentification());
		estudioPK.setIdProf(study.getProfession().getIdentification());
		EstudiosEntity estudio = new EstudiosEntity();
		estudio.setEstudiosPK(estudioPK);
		estudio.setFecha(validateFecha(study.getGraduationDate()));
		estudio.setUniver(validateUniver(study.getUniversityName()));
		return estudio;
	}

	private Date validateFecha(LocalDate graduationDate) {
		return graduationDate != null
				? Date.from(graduationDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
				: null;
	}

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosEntity estudiosEntity) {
		Study study = new Study();
		study.setPerson(validatePerson(estudiosEntity.getPersona()));
		study.setProfession(validateProfession(estudiosEntity.getProfesion()));
		study.setGraduationDate(validateGraduationDate(estudiosEntity.getFecha()));
		study.setUniversityName(validateUniversityName(estudiosEntity.getUniver()));
		return study;
	}

	private @lombok.NonNull co.edu.javeriana.as.personapp.domain.Person validatePerson(co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity personaEntity) {
		if (personaEntity != null) {
			// Crear persona simple sin referencias circulares
			co.edu.javeriana.as.personapp.domain.Person person = new co.edu.javeriana.as.personapp.domain.Person();
			person.setIdentification(personaEntity.getCc());
			person.setFirstName(personaEntity.getNombre());
			person.setLastName(personaEntity.getApellido());
			person.setAge(personaEntity.getEdad());
			person.setGender(validateGender(personaEntity.getGenero()));
			// NO mapear estudios ni teléfonos aquí para evitar referencia circular
			return person;
		}
		return new co.edu.javeriana.as.personapp.domain.Person();
	}

	private co.edu.javeriana.as.personapp.domain.Gender validateGender(Character genero) {
		return genero != null && genero.equals('F') ? co.edu.javeriana.as.personapp.domain.Gender.FEMALE : 
			   genero != null && genero.equals('M') ? co.edu.javeriana.as.personapp.domain.Gender.MALE : 
			   co.edu.javeriana.as.personapp.domain.Gender.OTHER;
	}

	private Integer validateAge(Integer edad) {
		return edad != null && edad >= 0 ? edad : null;
	}

	private @lombok.NonNull co.edu.javeriana.as.personapp.domain.Profession validateProfession(co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity profesionEntity) {
		if (profesionEntity != null) {
			// Crear profesión simple sin referencias circulares
			co.edu.javeriana.as.personapp.domain.Profession profession = new co.edu.javeriana.as.personapp.domain.Profession();
			profession.setIdentification(profesionEntity.getId());
			profession.setName(profesionEntity.getNom());
			profession.setDescription(profesionEntity.getDes() != null ? profesionEntity.getDes() : "");
			// NO mapear estudios para evitar referencia circular
			return profession;
		}
		return new co.edu.javeriana.as.personapp.domain.Profession();
	}

	private LocalDate validateGraduationDate(Date fecha) {
		if (fecha != null) {
			// Convertir java.sql.Date a LocalDate usando valueOf
			if (fecha instanceof java.sql.Date) {
				return ((java.sql.Date) fecha).toLocalDate();
			} else {
				// Para java.util.Date usar toInstant
				return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}
		}
		return null;
	}

	private String validateUniversityName(String univer) {
		return univer != null ? univer : "";
	}
}