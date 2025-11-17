package co.edu.javeriana.as.personapp.terminal.adapter;

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
import co.edu.javeriana.as.personapp.terminal.mapper.StudyMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class StudyInputAdapterCli {

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
	private StudyMapperCli studyMapperCli;

	StudyInputPort studyInputPort;
	PersonInputPort personInputPort;
	ProfessionInputPort professionInputPort;

	public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
			personInputPort = new PersonUseCase(personOutputPortMaria);
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
			personInputPort = new PersonUseCase(personOutputPortMongo);
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial StudyEntity in Input Adapter");
		studyInputPort.findAll().stream()
			.map(studyMapperCli::fromDomainToAdapterCli)
			.forEach(System.out::println);
	}

	public void crear(StudyModelCli studyModelCli) {
		log.info("Into crear StudyEntity in Input Adapter");
		try {
			Person person = personInputPort.findOne(studyModelCli.getPersonId());
			Profession profession = professionInputPort.findOne(studyModelCli.getProfessionId());
			
			if (person != null && profession != null) {
				Study study = studyMapperCli.fromAdapterToDomain(studyModelCli, person, profession);
				studyInputPort.create(study);
				System.out.println("Study created successfully");
			} else {
				System.out.println("Person or Profession not found");
			}
		} catch (NoExistException e) {
			log.warn(e.getMessage());
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void buscar(Integer personId, Integer professionId) {
		log.info("Into buscar StudyEntity in Input Adapter");
		try {
			Study study = studyInputPort.findOne(personId, professionId);
			if (study != null) {
				StudyModelCli studyModelCli = studyMapperCli.fromDomainToAdapterCli(study);
				System.out.println(studyModelCli.toString());
			} else {
				System.out.println("Study not found");
			}
		} catch (NoExistException e) {
			log.warn(e.getMessage());
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void editar(Integer personId, Integer professionId, StudyModelCli studyModelCli) {
		log.info("Into editar StudyEntity in Input Adapter");
		try {
			Person person = personInputPort.findOne(studyModelCli.getPersonId());
			Profession profession = professionInputPort.findOne(studyModelCli.getProfessionId());
			
			if (person != null && profession != null) {
				Study study = studyMapperCli.fromAdapterToDomain(studyModelCli, person, profession);
				studyInputPort.edit(personId, professionId, study);
				System.out.println("Study updated successfully");
			} else {
				System.out.println("Person or Profession not found");
			}
		} catch (NoExistException e) {
			log.warn(e.getMessage());
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void eliminar(Integer personId, Integer professionId) {
		log.info("Into eliminar StudyEntity in Input Adapter");
		try {
			if (studyInputPort.drop(personId, professionId)) {
				System.out.println("Study deleted successfully");
			} else {
				System.out.println("Study could not be deleted");
			}
		} catch (NoExistException e) {
			log.warn(e.getMessage());
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void contar() {
		log.info("Into contar StudyEntity in Input Adapter");
		System.out.println("Total studies: " + studyInputPort.count());
	}

	public void estudiosPorPersona(Integer personId) {
		log.info("Into estudiosPorPersona in Input Adapter");
		List<StudyModelCli> studies = studyInputPort.findByPerson(personId).stream()
				.map(studyMapperCli::fromDomainToAdapterCli)
				.collect(Collectors.toList());
		studies.forEach(System.out::println);
	}

	public void estudiosPorProfesion(Integer professionId) {
		log.info("Into estudiosPorProfesion in Input Adapter");
		List<StudyModelCli> studies = studyInputPort.findByProfession(professionId).stream()
				.map(studyMapperCli::fromDomainToAdapterCli)
				.collect(Collectors.toList());
		studies.forEach(System.out::println);
	}
}