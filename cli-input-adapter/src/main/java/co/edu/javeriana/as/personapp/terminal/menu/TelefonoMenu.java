package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelefonoMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_BUSCAR_POR_NUMERO = 2;
	private static final int OPCION_CREAR = 3;
	private static final int OPCION_EDITAR = 4;
	private static final int OPCION_ELIMINAR = 5;
	private static final int OPCION_TELEFONOS_POR_PERSONA = 6;
	private static final int OPCION_TELEFONOS_POR_OPERADOR = 7;
	private static final int OPCION_CONTAR_TELEFONOS = 8;

	public void iniciarMenu(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
					telefonoInputAdapterCli.setPhoneOutputPortInjection("MARIA");
					menuOpciones(telefonoInputAdapterCli, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					telefonoInputAdapterCli.setPhoneOutputPortInjection("MONGO");
					menuOpciones(telefonoInputAdapterCli, keyboard);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
					telefonoInputAdapterCli.historial();
					break;
				case OPCION_BUSCAR_POR_NUMERO:
					buscarPorNumero(telefonoInputAdapterCli, keyboard);
					break;
				case OPCION_CREAR:
					crearTelefono(telefonoInputAdapterCli, keyboard);
					break;
				case OPCION_EDITAR:
					editarTelefono(telefonoInputAdapterCli, keyboard);
					break;
				case OPCION_ELIMINAR:
					eliminarTelefono(telefonoInputAdapterCli, keyboard);
					break;
				case OPCION_TELEFONOS_POR_PERSONA:
					telefonosPorPersona(telefonoInputAdapterCli, keyboard);
					break;
				case OPCION_TELEFONOS_POR_OPERADOR:
					telefonosPorOperador(telefonoInputAdapterCli, keyboard);
					break;
				case OPCION_CONTAR_TELEFONOS:
					telefonoInputAdapterCli.contarTelefonos();
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InputMismatchException e) {
				log.warn("Solo se permiten números.");
				keyboard.nextLine(); // Limpiar buffer
			}
		} while (!isValid);
	}

	private void buscarPorNumero(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
		System.out.print("Ingrese el número de teléfono: ");
		String numero = keyboard.next();
		telefonoInputAdapterCli.buscarPorNumero(numero);
	}

	private void telefonosPorPersona(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
		System.out.print("Ingrese el ID de la persona: ");
		try {
			Integer personId = keyboard.nextInt();
			telefonoInputAdapterCli.telefonosPorPersona(personId);
		} catch (InputMismatchException e) {
			log.warn("Debe ingresar un número válido.");
			keyboard.nextLine(); // Limpiar buffer
		}
	}

	private void telefonosPorOperador(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
		System.out.print("Ingrese el nombre del operador: ");
		String operador = keyboard.next();
		telefonoInputAdapterCli.telefonosPorOperador(operador);
	}

	private void crearTelefono(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
		try {
			keyboard.nextLine(); // Limpiar buffer
			System.out.print("Ingrese el número de teléfono: ");
			String numero = keyboard.nextLine();
			System.out.print("Ingrese el operador: ");
			String operador = keyboard.nextLine();
			System.out.print("Ingrese el ID del dueño: ");
			Integer duenioId = keyboard.nextInt();
			telefonoInputAdapterCli.crearTelefono(numero, operador, duenioId);
		} catch (InputMismatchException e) {
			log.warn("Debe ingresar datos válidos.");
			keyboard.nextLine();
		}
	}

	private void editarTelefono(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
		try {
			keyboard.nextLine(); // Limpiar buffer
			System.out.print("Ingrese el número de teléfono a editar: ");
			String numero = keyboard.nextLine();
			System.out.print("Ingrese el nuevo operador: ");
			String operador = keyboard.nextLine();
			System.out.print("Ingrese el nuevo ID del dueño: ");
			Integer duenioId = keyboard.nextInt();
			telefonoInputAdapterCli.editarTelefono(numero, operador, duenioId);
		} catch (InputMismatchException e) {
			log.warn("Debe ingresar datos válidos.");
			keyboard.nextLine();
		}
	}

	private void eliminarTelefono(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
		try {
			keyboard.nextLine(); // Limpiar buffer
			System.out.print("Ingrese el número de teléfono a eliminar: ");
			String numero = keyboard.nextLine();
			telefonoInputAdapterCli.eliminarTelefono(numero);
		} catch (Exception e) {
			log.warn("Error al eliminar teléfono.");
			keyboard.nextLine();
		}
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todos los teléfonos");
		System.out.println(OPCION_BUSCAR_POR_NUMERO + " para buscar por número");
		System.out.println(OPCION_CREAR + " para crear un teléfono");
		System.out.println(OPCION_EDITAR + " para editar un teléfono");
		System.out.println(OPCION_ELIMINAR + " para eliminar un teléfono");
		System.out.println(OPCION_TELEFONOS_POR_PERSONA + " para ver teléfonos de una persona");
		System.out.println(OPCION_TELEFONOS_POR_OPERADOR + " para ver teléfonos por operador");
		System.out.println(OPCION_CONTAR_TELEFONOS + " para contar teléfonos");
		System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
	}

	private void mostrarMenuMotorPersistencia() {
		System.out.println("----------------------");
		System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
		System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
		System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
	}

	private int leerOpcion(Scanner keyboard) {
		try {
			System.out.print("Ingrese una opción: ");
			return keyboard.nextInt();
		} catch (InputMismatchException e) {
			log.warn("Solo se permiten números.");
			keyboard.nextLine(); // Limpiar buffer
			return leerOpcion(keyboard);
		}
	}
}