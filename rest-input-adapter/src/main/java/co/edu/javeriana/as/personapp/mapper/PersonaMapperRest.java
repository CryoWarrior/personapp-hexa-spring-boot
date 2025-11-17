package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

@Mapper
public class PersonaMapperRest {
	
	public PersonaResponse fromDomainToAdapterRestMaria(Person person) {
		return fromDomainToAdapterRest(person, "MariaDB");
	}
	public PersonaResponse fromDomainToAdapterRestMongo(Person person) {
		return fromDomainToAdapterRest(person, "MongoDB");
	}
	
	public PersonaResponse fromDomainToAdapterRest(Person person, String database) {
		return new PersonaResponse(
				person.getIdentification()+"", 
				person.getFirstName(), 
				person.getLastName(), 
				person.getAge()+"", 
				person.getGender().toString(), 
				database,
				"OK");
	}

	public Person fromAdapterToDomain(PersonaRequest request) {
		return fromAdapterToDomain(request, null);
	}

	public Person fromAdapterToDomain(PersonaRequest request, Integer identification) {
		Person person = new Person();
		Integer id = identification != null ? identification : Integer.parseInt(request.getDni());
		person.setIdentification(id);
		person.setFirstName(request.getFirstName());
		person.setLastName(request.getLastName());
		person.setAge(request.getAge() != null && !request.getAge().isEmpty() ? Integer.parseInt(request.getAge()) : null);
		person.setGender(parseGender(request.getSex()));
		return person;
	}

	private Gender parseGender(String sex) {
		if (sex == null) return Gender.OTHER;
		switch (sex.toUpperCase()) {
			case "M": case "MALE": case "MASCULINO": return Gender.MALE;
			case "F": case "FEMALE": case "FEMENINO": return Gender.FEMALE;
			default: return Gender.OTHER;
		}
	}
		
}
