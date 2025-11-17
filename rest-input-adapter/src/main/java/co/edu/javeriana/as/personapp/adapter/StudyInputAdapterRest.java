package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.StudyMapperRest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class StudyInputAdapterRest {

	@Autowired
	@Qualifier("studyOutputAdapterMaria")
	private StudyOutputPort studyOutputPortMaria;

	@Autowired
	@Qualifier("studyOutputAdapterMongo")
	private StudyOutputPort studyOutputPortMongo;

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private StudyMapperRest studyMapperRest;

	StudyInputPort studyInputPort;
	PersonInputPort personInputPort;
	ProfessionInputPort professionInputPort;

	private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
			personInputPort = new PersonUseCase(personOutputPortMaria);
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
			personInputPort = new PersonUseCase(personOutputPortMongo);
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<StudyResponse> historial(String database) {
		log.info("Into historial StudyEntity in Input Adapter");
		try {
			if (setStudyOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return studyInputPort.findAll().stream().map(studyMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return studyInputPort.findAll().stream().map(studyMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}

		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<StudyResponse>();
		}
	}

	public StudyResponse crearEstudio(StudyRequest request) {
		try {
			String database = setStudyOutputPortInjection(request.getDatabase());
			
			// Buscar la persona y profesión
			Person person = personInputPort.findOne(request.getPersonId());
			Profession profession = professionInputPort.findOne(request.getProfessionId());
			
			if (person == null) {
				log.warn("Person with ID {} not found", request.getPersonId());
				return null;
			}
			
			if (profession == null) {
				log.warn("Profession with ID {} not found", request.getProfessionId());
				return null;
			}
			
			Study study = studyInputPort.create(studyMapperRest.fromAdapterToDomain(request, person, profession));
			
			if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return studyMapperRest.fromDomainToAdapterRestMaria(study);
			} else {
				return studyMapperRest.fromDomainToAdapterRestMongo(study);
			}
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public StudyResponse buscarEstudio(String database, Integer personId, Integer professionId) {
		log.info("Into buscarEstudio StudyEntity in Input Adapter");
		try {
			if (setStudyOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				Study study = studyInputPort.findOne(personId, professionId);
				return study != null ? studyMapperRest.fromDomainToAdapterRestMaria(study) : null;
			} else {
				Study study = studyInputPort.findOne(personId, professionId);
				return study != null ? studyMapperRest.fromDomainToAdapterRestMongo(study) : null;
			}
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public StudyResponse editarEstudio(String database, Integer personId, Integer professionId, StudyRequest request) {
		log.info("Into editarEstudio StudyEntity in Input Adapter");
		try {
			String dbUsed = setStudyOutputPortInjection(database);
			
			// Buscar la persona y profesión
			Person person = personInputPort.findOne(request.getPersonId());
			Profession profession = professionInputPort.findOne(request.getProfessionId());
			
			if (person == null || profession == null) {
				log.warn("Person or Profession not found");
				return null;
			}
			
			Study study = studyInputPort.edit(personId, professionId, studyMapperRest.fromAdapterToDomain(request, person, profession));
			
			if (dbUsed.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return studyMapperRest.fromDomainToAdapterRestMaria(study);
			} else {
				return studyMapperRest.fromDomainToAdapterRestMongo(study);
			}
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public boolean eliminarEstudio(String database, Integer personId, Integer professionId) {
		log.info("Into eliminarEstudio StudyEntity in Input Adapter");
		try {
			setStudyOutputPortInjection(database);
			return studyInputPort.drop(personId, professionId);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return false;
		}
	}

	public List<StudyResponse> estudiosPorPersona(String database, Integer personId) {
		log.info("Into estudiosPorPersona in Input Adapter");
		try {
			if (setStudyOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return studyInputPort.findByPerson(personId).stream()
						.map(studyMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return studyInputPort.findByPerson(personId).stream()
						.map(studyMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<StudyResponse>();
		}
	}

	public List<StudyResponse> estudiosPorProfesion(String database, Integer professionId) {
		log.info("Into estudiosPorProfesion in Input Adapter");
		try {
			if (setStudyOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return studyInputPort.findByProfession(professionId).stream()
						.map(studyMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return studyInputPort.findByProfession(professionId).stream()
						.map(studyMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<StudyResponse>();
		}
	}
}