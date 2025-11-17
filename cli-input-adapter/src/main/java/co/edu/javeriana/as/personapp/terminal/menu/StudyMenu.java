package co.edu.javeriana.as.personapp.terminal.menu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;


import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.StudyInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StudyMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_BUSCAR = 2;
	private static final int OPCION_CREAR = 3;
	private static final int OPCION_EDITAR = 4;
	private static final int OPCION_ELIMINAR = 5;
	private static final int OPCION_CONTAR = 6;
	private static final int OPCION_ESTUDIOS_POR_PERSONA = 7;
	private static final int OPCION_ESTUDIOS_POR_PROFESION = 8;



	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	public void iniciarMenu(StudyInputAdapterCli studyInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuMotorPersistencia();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR_MODULOS:
					isValid = true;
					break;
				case PERSISTENCIA_MARIADB:
					studyInputAdapterCli.setStudyOutputPortInjection("MARIA");
					menuOpciones(studyInputAdapterCli, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					studyInputAdapterCli.setStudyOutputPortInjection("MONGO");
					menuOpciones(studyInputAdapterCli, keyboard);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(StudyInputAdapterCli studyInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuEstudios();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR_MODULOS:
					isValid = true;
					System.out.println("Regresando al menú principal...");
					break;
				case OPCION_VER_TODO:
					studyInputAdapterCli.historial();
					break;
				case OPCION_BUSCAR:
					buscarEstudio(studyInputAdapterCli, keyboard);
					break;
				case OPCION_CREAR:
					crearEstudio(studyInputAdapterCli, keyboard);
					break;
				case OPCION_EDITAR:
					editarEstudio(studyInputAdapterCli, keyboard);
					break;
				case OPCION_ELIMINAR:
					eliminarEstudio(studyInputAdapterCli, keyboard);
					break;
				case OPCION_CONTAR:
					studyInputAdapterCli.contar();
					break;
				case OPCION_ESTUDIOS_POR_PERSONA:
					estudiosPorPersona(studyInputAdapterCli, keyboard);
					break;
				case OPCION_ESTUDIOS_POR_PROFESION:
					estudiosPorProfesion(studyInputAdapterCli, keyboard);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (Exception e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void mostrarMenuMotorPersistencia() {
		System.out.println("----------------------");
		System.out.println("SELECCIONE MOTOR DE PERSISTENCIA");
		System.out.println("----------------------");
		System.out.println("1 para MariaDB");
		System.out.println("2 para MongoDB");
		System.out.println("0 para regresar");
	}

	private void mostrarMenuEstudios() {
		System.out.println("----------------------");
		System.out.println("MENU DEL MÓDULO ESTUDIOS");
		System.out.println("----------------------");
		System.out.println("1 para ver todos los estudios");
		System.out.println("2 para buscar un estudio");
		System.out.println("3 para crear un estudio");
		System.out.println("4 para editar un estudio");
		System.out.println("5 para eliminar un estudio");
		System.out.println("6 para contar estudios");
		System.out.println("7 para ver estudios por persona");
		System.out.println("8 para ver estudios por profesión");
		System.out.println("0 para regresar");
	}

	private int leerOpcion(Scanner keyboard) {
		try {
			System.out.print("Ingrese una opción: ");
			return keyboard.nextInt();
		} catch (InputMismatchException e) {
			log.warn("Solo se permiten números.");
			keyboard.nextLine(); // Limpiar el buffer
			return leerOpcion(keyboard);
		}
	}

	private void buscarEstudio(StudyInputAdapterCli studyInputAdapterCli, Scanner keyboard) {
		try {
			System.out.print("Ingrese el ID de la persona: ");
			Integer personId = keyboard.nextInt();
			System.out.print("Ingrese el ID de la profesión: ");
			Integer professionId = keyboard.nextInt();
			studyInputAdapterCli.buscar(personId, professionId);
		} catch (InputMismatchException e) {
			log.warn("Ingrese un número válido");
			keyboard.nextLine();
		}
	}

	private void crearEstudio(StudyInputAdapterCli studyInputAdapterCli, Scanner keyboard) {
		try {
			StudyModelCli studyModelCli = leerDatosEstudio(keyboard);
			studyInputAdapterCli.crear(studyModelCli);
		} catch (InputMismatchException e) {
			log.warn("Ingrese un número válido");
			keyboard.nextLine();
		}
	}

	private void editarEstudio(StudyInputAdapterCli studyInputAdapterCli, Scanner keyboard) {
		try {
			System.out.print("Ingrese el ID de la persona del estudio a editar: ");
			Integer oldPersonId = keyboard.nextInt();
			System.out.print("Ingrese el ID de la profesión del estudio a editar: ");
			Integer oldProfessionId = keyboard.nextInt();
			
			StudyModelCli studyModelCli = leerDatosEstudio(keyboard);
			studyInputAdapterCli.editar(oldPersonId, oldProfessionId, studyModelCli);
		} catch (InputMismatchException e) {
			log.warn("Ingrese un número válido");
			keyboard.nextLine();
		}
	}

	private void eliminarEstudio(StudyInputAdapterCli studyInputAdapterCli, Scanner keyboard) {
		try {
			System.out.print("Ingrese el ID de la persona: ");
			Integer personId = keyboard.nextInt();
			System.out.print("Ingrese el ID de la profesión: ");
			Integer professionId = keyboard.nextInt();
			studyInputAdapterCli.eliminar(personId, professionId);
		} catch (InputMismatchException e) {
			log.warn("Ingrese un número válido");
			keyboard.nextLine();
		}
	}

	private void estudiosPorPersona(StudyInputAdapterCli studyInputAdapterCli, Scanner keyboard) {
		try {
			System.out.print("Ingrese el ID de la persona: ");
			Integer personId = keyboard.nextInt();
			studyInputAdapterCli.estudiosPorPersona(personId);
		} catch (InputMismatchException e) {
			log.warn("Ingrese un número válido");
			keyboard.nextLine();
		}
	}

	private void estudiosPorProfesion(StudyInputAdapterCli studyInputAdapterCli, Scanner keyboard) {
		try {
			System.out.print("Ingrese el ID de la profesión: ");
			Integer professionId = keyboard.nextInt();
			studyInputAdapterCli.estudiosPorProfesion(professionId);
		} catch (InputMismatchException e) {
			log.warn("Ingrese un número válido");
			keyboard.nextLine();
		}
	}

	private StudyModelCli leerDatosEstudio(Scanner keyboard) {
		keyboard.nextLine(); // Limpiar buffer
		
		System.out.print("Ingrese el ID de la persona: ");
		Integer personId = keyboard.nextInt();
		
		System.out.print("Ingrese el ID de la profesión: ");
		Integer professionId = keyboard.nextInt();
		keyboard.nextLine(); // Limpiar buffer
		
		System.out.print("Ingrese la fecha de graduación (yyyy-MM-dd) o presione Enter para omitir: ");
		String graduationDate = keyboard.nextLine();
		if (graduationDate.trim().isEmpty()) {
			graduationDate = null;
		} else {
			try {
				// Validar formato de fecha
				LocalDate.parse(graduationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			} catch (DateTimeParseException e) {
				System.out.println("Formato de fecha inválido. Se omitirá la fecha.");
				graduationDate = null;
			}
		}
		
		System.out.print("Ingrese el nombre de la universidad: ");
		String universityName = keyboard.nextLine();
		if (universityName.trim().isEmpty()) {
			universityName = null;
		}
		
		return new StudyModelCli(personId, null, professionId, null, graduationDate, universityName);
	}
}