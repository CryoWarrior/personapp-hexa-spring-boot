package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonaMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_BUSCAR_POR_ID = 2;
	private static final int OPCION_CREAR = 3;
	private static final int OPCION_EDITAR = 4;
	private static final int OPCION_ELIMINAR = 5;
	private static final int OPCION_CONTAR = 6;

	public void iniciarMenu(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
					personaInputAdapterCli.setPersonOutputPortInjection("MARIA");
					menuOpciones(personaInputAdapterCli,keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					personaInputAdapterCli.setPersonOutputPortInjection("MONGO");
					menuOpciones(personaInputAdapterCli,keyboard);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			}  catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuOpciones();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
					isValid = true;
					break;
				case OPCION_VER_TODO:
					personaInputAdapterCli.historial();					
					break;
				case OPCION_BUSCAR_POR_ID:
					buscarPorId(personaInputAdapterCli, keyboard);
					break;
				case OPCION_CREAR:
					crearPersona(personaInputAdapterCli, keyboard);
					break;
				case OPCION_EDITAR:
					editarPersona(personaInputAdapterCli, keyboard);
					break;
				case OPCION_ELIMINAR:
					eliminarPersona(personaInputAdapterCli, keyboard);
					break;
				case OPCION_CONTAR:
					personaInputAdapterCli.contarPersonas();
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InputMismatchException e) {
				log.warn("Solo se permiten números.");
			}
		} while (!isValid);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todas las personas");
		System.out.println(OPCION_BUSCAR_POR_ID + " para buscar una persona por CC");
		System.out.println(OPCION_CREAR + " para crear una persona");
		System.out.println(OPCION_EDITAR + " para editar una persona");
		System.out.println(OPCION_ELIMINAR + " para eliminar una persona");
		System.out.println(OPCION_CONTAR + " para contar las personas");
		System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
	}

	private void mostrarMenuMotorPersistencia() {
		System.out.println("----------------------");
		System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
		System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
		System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
	}

	private void buscarPorId(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		try {
			System.out.print("Ingrese la CC de la persona: ");
			Integer cc = keyboard.nextInt();
			personaInputAdapterCli.buscarPorId(cc);
		} catch (InputMismatchException e) {
			log.warn("Debe ingresar un número válido.");
			keyboard.nextLine();
		}
	}

	private void crearPersona(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		try {
			System.out.print("Ingrese la CC de la persona: ");
			Integer cc = keyboard.nextInt();
			keyboard.nextLine();
			System.out.print("Ingrese el nombre: ");
			String nombre = keyboard.nextLine();
			System.out.print("Ingrese el apellido: ");
			String apellido = keyboard.nextLine();
			System.out.print("Ingrese el género (M/F): ");
			String genero = keyboard.nextLine();
			System.out.print("Ingrese la edad: ");
			Integer edad = keyboard.nextInt();
			personaInputAdapterCli.crearPersona(cc, nombre, apellido, genero, edad);
		} catch (InputMismatchException e) {
			log.warn("Debe ingresar datos válidos.");
			keyboard.nextLine();
		}
	}

	private void editarPersona(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		try {
			System.out.print("Ingrese la CC de la persona a editar: ");
			Integer cc = keyboard.nextInt();
			keyboard.nextLine();
			System.out.print("Ingrese el nuevo nombre: ");
			String nombre = keyboard.nextLine();
			System.out.print("Ingrese el nuevo apellido: ");
			String apellido = keyboard.nextLine();
			System.out.print("Ingrese el nuevo género (M/F): ");
			String genero = keyboard.nextLine();
			System.out.print("Ingrese la nueva edad: ");
			Integer edad = keyboard.nextInt();
			personaInputAdapterCli.editarPersona(cc, nombre, apellido, genero, edad);
		} catch (InputMismatchException e) {
			log.warn("Debe ingresar datos válidos.");
			keyboard.nextLine();
		}
	}

	private void eliminarPersona(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		try {
			System.out.print("Ingrese la CC de la persona a eliminar: ");
			Integer cc = keyboard.nextInt();
			personaInputAdapterCli.eliminarPersona(cc);
		} catch (InputMismatchException e) {
			log.warn("Debe ingresar un número válido.");
			keyboard.nextLine();
		}
	}

	private int leerOpcion(Scanner keyboard) {
		try {
			System.out.print("Ingrese una opción: ");
			return keyboard.nextInt();
		} catch (InputMismatchException e) {
			log.warn("Solo se permiten números.");
			return leerOpcion(keyboard);
		}
	}

}
