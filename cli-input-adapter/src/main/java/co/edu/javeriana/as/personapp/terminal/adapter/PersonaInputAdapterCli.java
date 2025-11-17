package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial1() {
		log.info("Into historial PersonaEntity in Input Adapter");
		List<PersonaModelCli> persona = personInputPort.findAll().stream().map(personaMapperCli::fromDomainToAdapterCli)
					.collect(Collectors.toList());
		persona.forEach(p -> System.out.println(p.toString()));
	}
	public void historial() {
	    log.info("Into historial PersonaEntity in Input Adapter");
	    personInputPort.findAll().stream()
	        .map(personaMapperCli::fromDomainToAdapterCli)
	        .forEach(System.out::println);
	}

	public void buscarPorId(Integer id) {
		try {
			Person person = personInputPort.findOne(id);
			System.out.println(personaMapperCli.fromDomainToAdapterCli(person));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
			System.out.println("Persona no encontrada.");
		}
	}

	public void crearPersona(Integer cc, String nombre, String apellido, String genero, Integer edad) {
		Person person = buildPerson(cc, nombre, apellido, genero, edad);
		Person created = personInputPort.create(person);
		PersonaModelCli personaModelCli = personaMapperCli.fromDomainToAdapterCli(created);
		System.out.println("Persona creada: " + personaModelCli);
	}

	public void editarPersona(Integer cc, String nombre, String apellido, String genero, Integer edad) {
		try {
			Person person = buildPerson(cc, nombre, apellido, genero, edad);
			Person updated = personInputPort.edit(cc, person);
			System.out.println("Persona actualizada: " + personaMapperCli.fromDomainToAdapterCli(updated));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
			System.out.println("No fue posible actualizar la persona: " + e.getMessage());
		}
	}

	public void eliminarPersona(Integer cc) {
		try {
			boolean deleted = personInputPort.drop(cc);
			if (deleted) {
				System.out.println("Persona eliminada correctamente.");
			} else {
				System.out.println("No fue posible eliminar la persona.");
			}
		} catch (NoExistException e) {
			log.warn(e.getMessage());
			System.out.println("No fue posible eliminar la persona: " + e.getMessage());
		}
	}

	public void contarPersonas() {
		Integer count = personInputPort.count();
		System.out.println("Total de personas: " + count);
	}

	private Person buildPerson(Integer cc, String nombre, String apellido, String genero, Integer edad) {
		Person person = new Person();
		person.setIdentification(cc);
		person.setFirstName(nombre);
		person.setLastName(apellido);
		person.setGender(parseGender(genero));
		person.setAge(edad);
		person.setPhoneNumbers(new ArrayList<>());
		person.setStudies(new ArrayList<>());
		return person;
	}

	private Gender parseGender(String genero) {
		if (genero == null) return Gender.OTHER;
		switch (genero.toUpperCase()) {
			case "M": return Gender.MALE;
			case "F": return Gender.FEMALE;
			default: return Gender.OTHER;
		}
	}

}
