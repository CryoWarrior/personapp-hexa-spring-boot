package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private ProfesionMapperCli profesionMapperCli;

	private ProfessionInputPort professionInputPort;

	public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial ProfesionEntity in Input Adapter");
		professionInputPort.findAll().stream()
				.map(profesionMapperCli::fromDomainToAdapterCli)
				.forEach(System.out::println);
	}

	public void buscarPorId(Integer id) {
		try {
			Profession profession = professionInputPort.findOne(id);
			System.out.println(profesionMapperCli.fromDomainToAdapterCli(profession));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
			System.out.println("Profesión no encontrada.");
		}
	}

	public void crearProfesion(Integer id, String nombre, String descripcion) {
		Profession profession = buildProfession(id, nombre, descripcion);
		Profession created = professionInputPort.create(profession);
		ProfesionModelCli profesionModelCli = profesionMapperCli.fromDomainToAdapterCli(created);
		System.out.println("Profesión creada: " + profesionModelCli);
	}

	public void editarProfesion(Integer id, String nombre, String descripcion) {
		try {
			Profession profession = buildProfession(id, nombre, descripcion);
			Profession updated = professionInputPort.edit(id, profession);
			System.out.println("Profesión actualizada: " + profesionMapperCli.fromDomainToAdapterCli(updated));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
			System.out.println("No fue posible actualizar la profesión: " + e.getMessage());
		}
	}

	public void eliminarProfesion(Integer id) {
		try {
			boolean deleted = professionInputPort.drop(id);
			if (deleted) {
				System.out.println("Profesión eliminada correctamente.");
			} else {
				System.out.println("No fue posible eliminar la profesión.");
			}
		} catch (NoExistException e) {
			log.warn(e.getMessage());
			System.out.println("No fue posible eliminar la profesión: " + e.getMessage());
		}
	}

	public void contarProfesiones() {
		Integer count = professionInputPort.count();
		System.out.println("Total de profesiones: " + count);
	}

	private Profession buildProfession(Integer id, String nombre, String descripcion) {
		Profession profession = new Profession();
		profession.setIdentification(id);
		profession.setName(nombre);
		profession.setDescription(descripcion);
		profession.setStudies(new ArrayList<>());
		return profession;
	}
}

