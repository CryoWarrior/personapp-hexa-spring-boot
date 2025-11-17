package co.edu.javeriana.as.personapp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.PhoneInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/telefono")
public class TelefonoControllerV1 {
	
	@Autowired
	private PhoneInputAdapterRest phoneInputAdapterRest;
	
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PhoneResponse> telefonos(@PathVariable String database) {
		log.info("Into telefonos REST API");
		return phoneInputAdapterRest.historial(database.toUpperCase());
	}
	
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PhoneResponse crearTelefono(@RequestBody PhoneRequest request) {
		log.info("Into crearTelefono in REST API");
		return phoneInputAdapterRest.crearTelefono(request);
	}
	
	@ResponseBody
	@GetMapping(path = "/{database}/persona/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PhoneResponse> telefonosPorPersona(@PathVariable String database, @PathVariable Integer personId) {
		log.info("Into telefonosPorPersona REST API");
		return phoneInputAdapterRest.telefonosPorPersona(database.toUpperCase(), personId);
	}
	
	@ResponseBody
	@GetMapping(path = "/{database}/operador/{company}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PhoneResponse> telefonosPorOperador(@PathVariable String database, @PathVariable String company) {
		log.info("Into telefonosPorOperador REST API");
		return phoneInputAdapterRest.telefonosPorOperador(database.toUpperCase(), company);
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
	public PhoneResponse telefono(@PathVariable String database, @PathVariable String number) {
		log.info("Into telefono detail REST API");
		return phoneInputAdapterRest.buscarTelefono(database.toUpperCase(), number);
	}

	@ResponseBody
	@PutMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PhoneResponse editarTelefono(@PathVariable String database, @PathVariable String number,
			@RequestBody PhoneRequest request) {
		log.info("Into editarTelefono REST API");
		return phoneInputAdapterRest.editarTelefono(database.toUpperCase(), number, request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Response eliminarTelefono(@PathVariable String database, @PathVariable String number) {
		log.info("Into eliminarTelefono REST API");
		boolean deleted = phoneInputAdapterRest.eliminarTelefono(database.toUpperCase(), number);
		if (deleted) {
			return new Response("OK", "Phone deleted successfully", LocalDateTime.now());
		} else {
			return new Response("ERROR", "Phone could not be deleted", LocalDateTime.now());
		}
	}
}