package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.terminal.mapper.TelefonoMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterCli {

	@Autowired
	@Qualifier("phoneOutputAdapterMaria")
	private PhoneOutputPort phoneOutputPortMaria;

	@Autowired
	@Qualifier("phoneOutputAdapterMongo")
	private PhoneOutputPort phoneOutputPortMongo;

	@Autowired
	private TelefonoMapperCli telefonoMapperCli;

	PhoneInputPort phoneInputPort;

	public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial TelefonoEntity in Input Adapter");
		phoneInputPort.findAll().stream()
			.map(telefonoMapperCli::fromDomainToAdapterCli)
			.forEach(System.out::println);
	}

	public void buscarPorNumero(String numero) {
		try {
			TelefonoModelCli telefono = telefonoMapperCli.fromDomainToAdapterCli(phoneInputPort.findOne(numero));
			System.out.println(telefono);
		} catch (NoExistException e) {
			System.out.println("Teléfono no encontrado: " + e.getMessage());
		}
	}

	public void telefonosPorPersona(Integer personId) {
		try {
			List<TelefonoModelCli> telefonos = phoneInputPort.getPhonesByOwner(personId).stream()
					.map(telefonoMapperCli::fromDomainToAdapterCli)
					.collect(Collectors.toList());
			
			if (telefonos.isEmpty()) {
				System.out.println("No se encontraron teléfonos para la persona con ID: " + personId);
			} else {
				System.out.println("Teléfonos de la persona " + personId + ":");
				telefonos.forEach(System.out::println);
			}
		} catch (NoExistException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void telefonosPorOperador(String operador) {
		List<TelefonoModelCli> telefonos = phoneInputPort.getPhonesByCompany(operador).stream()
				.map(telefonoMapperCli::fromDomainToAdapterCli)
				.collect(Collectors.toList());
		
		if (telefonos.isEmpty()) {
			System.out.println("No se encontraron teléfonos del operador: " + operador);
		} else {
			System.out.println("Teléfonos del operador " + operador + ":");
			telefonos.forEach(System.out::println);
		}
	}

	public Integer contarTelefonos() {
		Integer count = phoneInputPort.count();
		System.out.println("Total de teléfonos: " + count);
		return count;
	}

	public void crearTelefono(String numero, String operador, Integer duenioId) {
		try {
			// Crear un teléfono básico sin validar persona existente para CLI
			co.edu.javeriana.as.personapp.domain.Phone phone = new co.edu.javeriana.as.personapp.domain.Phone();
			phone.setNumber(numero);
			phone.setCompany(operador);
			
			// Crear persona básica para referencia
			co.edu.javeriana.as.personapp.domain.Person owner = new co.edu.javeriana.as.personapp.domain.Person();
			owner.setIdentification(duenioId);
			phone.setOwner(owner);
			
			co.edu.javeriana.as.personapp.domain.Phone created = phoneInputPort.create(phone);
			System.out.println("Teléfono creado: " + telefonoMapperCli.fromDomainToAdapterCli(created));
		} catch (Exception e) {
			log.warn(e.getMessage());
			System.out.println("No fue posible crear el teléfono: " + e.getMessage());
		}
	}

	public void editarTelefono(String numero, String operador, Integer duenioId) {
		try {
			co.edu.javeriana.as.personapp.domain.Phone phone = new co.edu.javeriana.as.personapp.domain.Phone();
			phone.setNumber(numero);
			phone.setCompany(operador);
			
			// Crear persona básica para referencia
			co.edu.javeriana.as.personapp.domain.Person owner = new co.edu.javeriana.as.personapp.domain.Person();
			owner.setIdentification(duenioId);
			phone.setOwner(owner);
			
			co.edu.javeriana.as.personapp.domain.Phone updated = phoneInputPort.edit(numero, phone);
			System.out.println("Teléfono actualizado: " + telefonoMapperCli.fromDomainToAdapterCli(updated));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
			System.out.println("No fue posible actualizar el teléfono: " + e.getMessage());
		}
	}

	public void eliminarTelefono(String numero) {
		try {
			boolean deleted = phoneInputPort.drop(numero);
			if (deleted) {
				System.out.println("Teléfono eliminado correctamente.");
			} else {
				System.out.println("No fue posible eliminar el teléfono.");
			}
		} catch (NoExistException e) {
			log.warn(e.getMessage());
			System.out.println("No fue posible eliminar el teléfono: " + e.getMessage());
		}
	}
}