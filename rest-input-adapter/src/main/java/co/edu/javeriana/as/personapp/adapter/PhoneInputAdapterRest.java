package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.PhoneMapperRest;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PhoneInputAdapterRest {

	@Autowired
	@Qualifier("phoneOutputAdapterMaria")
	private PhoneOutputPort phoneOutputPortMaria;

	@Autowired
	@Qualifier("phoneOutputAdapterMongo")
	private PhoneOutputPort phoneOutputPortMongo;

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PhoneMapperRest phoneMapperRest;

	PhoneInputPort phoneInputPort;
	PersonInputPort personInputPort;

	private String setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
			personInputPort = new PersonUseCase(personOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
			personInputPort = new PersonUseCase(personOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<PhoneResponse> historial(String database) {
		log.info("Into historial PhoneEntity in Input Adapter");
		try {
			if (setPhoneOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return phoneInputPort.findAll().stream().map(phoneMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return phoneInputPort.findAll().stream().map(phoneMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}

		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<PhoneResponse>();
		}
	}

	public PhoneResponse crearTelefono(PhoneRequest request) {
		try {
			String database = setPhoneOutputPortInjection(request.getDatabase());
			
			// Buscar la persona owner
			Integer ownerId = Integer.parseInt(request.getOwnerId());
			Person owner = personInputPort.findOne(ownerId);
			
			if (owner == null) {
				log.warn("Person with ID {} not found", ownerId);
				return null;
			}
			
			Phone phone = phoneInputPort.create(phoneMapperRest.fromAdapterToDomain(request, owner));
			
			if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return phoneMapperRest.fromDomainToAdapterRestMaria(phone);
			} else {
				return phoneMapperRest.fromDomainToAdapterRestMongo(phone);
			}
		} catch (InvalidOptionException | NumberFormatException | NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public List<PhoneResponse> telefonosPorPersona(String database, Integer personId) {
		log.info("Into telefonosPorPersona in Input Adapter");
		try {
			if (setPhoneOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return phoneInputPort.getPhonesByOwner(personId).stream()
						.map(phoneMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return phoneInputPort.getPhonesByOwner(personId).stream()
						.map(phoneMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return new ArrayList<PhoneResponse>();
		}
	}

	public List<PhoneResponse> telefonosPorOperador(String database, String company) {
		log.info("Into telefonosPorOperador in Input Adapter");
		try {
			if (setPhoneOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return phoneInputPort.getPhonesByCompany(company).stream()
						.map(phoneMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return phoneInputPort.getPhonesByCompany(company).stream()
						.map(phoneMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<PhoneResponse>();
		}
	}
}