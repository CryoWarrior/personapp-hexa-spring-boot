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

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
public class PersonaControllerV1 {
	
	@Autowired
	private PersonaInputAdapterRest personaInputAdapterRest;
	
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PersonaResponse> personas(@PathVariable String database) {
		log.info("Into personas REST API");
			return personaInputAdapterRest.historial(database.toUpperCase());
	}
	
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PersonaResponse crearPersona(@RequestBody PersonaRequest request) {
		log.info("Into crearPersona REST API");
		return personaInputAdapterRest.crearPersona(request);
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonaResponse persona(@PathVariable String database, @PathVariable Integer id) {
		log.info("Into persona detail REST API");
		return personaInputAdapterRest.buscarPersona(database.toUpperCase(), id);
	}

	@ResponseBody
	@PutMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PersonaResponse editarPersona(@PathVariable String database, @PathVariable Integer id,
			@RequestBody PersonaRequest request) {
		log.info("Into editarPersona REST API");
		return personaInputAdapterRest.editarPersona(database.toUpperCase(), id, request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Response eliminarPersona(@PathVariable String database, @PathVariable Integer id) {
		log.info("Into eliminarPersona REST API");
		boolean deleted = personaInputAdapterRest.eliminarPersona(database.toUpperCase(), id);
		if (deleted) {
			return new Response("OK", "Person deleted successfully", LocalDateTime.now());
		} else {
			return new Response("ERROR", "Person could not be deleted", LocalDateTime.now());
		}
	}
}
