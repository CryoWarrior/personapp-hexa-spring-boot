package co.edu.javeriana.as.personapp.mongo.mapper;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import lombok.NonNull;

@Mapper
public class EstudiosMapperMongo {

	@Autowired
	private PersonaMapperMongo personaMapperMongo;

	public EstudiosDocument fromDomainToAdapter(Study study) {
		EstudiosDocument estudio = new EstudiosDocument();
		estudio.setId(validateId(study.getPerson().getIdentification(), study.getProfession().getIdentification()));
		estudio.setPrimaryPersona(validatePrimaryPersona(study.getPerson()));
		estudio.setPrimaryProfesion(validatePrimaryProfesion(study.getProfession()));
		estudio.setFecha(validateFecha(study.getGraduationDate()));
		estudio.setUniver(validateUniver(study.getUniversityName()));
		return estudio;
	}

	private String validateId(@NonNull Integer identificationPerson, @NonNull Integer identificationProfession) {
		return identificationPerson + "-" + identificationProfession;
	}

	private PersonaDocument validatePrimaryPersona(@NonNull Person person) {
		return person != null ? personaMapperMongo.fromDomainToAdapter(person) : new PersonaDocument();
	}

	private ProfesionDocument validatePrimaryProfesion(@NonNull Profession profession) {
		if (profession == null) return new ProfesionDocument();
		ProfesionDocument profesionDocument = new ProfesionDocument();
		profesionDocument.setId(profession.getIdentification());
		profesionDocument.setNom(profession.getName());
		profesionDocument.setDes(profession.getDescription() != null ? profession.getDescription() : "");
		// No mapear estudios para evitar referencia circular
		return profesionDocument;
	}

	private LocalDate validateFecha(LocalDate graduationDate) {
		return graduationDate != null ? graduationDate : null;
	}

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosDocument estudiosDocument) {
		Study study = new Study();
		study.setPerson(validatePerson(estudiosDocument.getPrimaryPersona()));
		study.setProfession(validateProfession(estudiosDocument.getPrimaryProfesion()));
		study.setGraduationDate(validateGraduationDate(estudiosDocument.getFecha()));
		study.setUniversityName(validateUniversityName(estudiosDocument.getUniver()));
		return study;
	}

	private @NonNull Person validatePerson(PersonaDocument personaDocument) {
		if (personaDocument != null) {
			// Crear persona simple sin referencias circulares
			Person person = new Person();
			person.setIdentification(personaDocument.getId());
			person.setFirstName(personaDocument.getNombre());
			person.setLastName(personaDocument.getApellido());
			person.setAge(personaDocument.getEdad());
			person.setGender(validateGender(personaDocument.getGenero()));
			// NO mapear estudios ni teléfonos aquí para evitar referencia circular
			return person;
		}
		return new Person();
	}

	private co.edu.javeriana.as.personapp.domain.Gender validateGender(String genero) {
		return "F".equals(genero) ? co.edu.javeriana.as.personapp.domain.Gender.FEMALE : 
			   "M".equals(genero) ? co.edu.javeriana.as.personapp.domain.Gender.MALE : 
			   co.edu.javeriana.as.personapp.domain.Gender.OTHER;
	}

	private Integer validateAge(Integer edad) {
		return edad != null && edad >= 0 ? edad : null;
	}

	private @NonNull Profession validateProfession(ProfesionDocument profesionDocument) {
		if (profesionDocument != null) {
			// Crear profesión simple sin referencias circulares
			Profession profession = new Profession();
			profession.setIdentification(profesionDocument.getId());
			profession.setName(profesionDocument.getNom());
			profession.setDescription(validateDescription(profesionDocument.getDes()));
			// NO mapear estudios para evitar referencia circular
			return profession;
		}
		return new Profession();
	}

	private String validateDescription(String des) {
		return des != null ? des : "";
	}

	private LocalDate validateGraduationDate(LocalDate fecha) {
		return fecha != null ? fecha : null;
	}

	private String validateUniversityName(String univer) {
		return univer != null ? univer : "";
	}
}