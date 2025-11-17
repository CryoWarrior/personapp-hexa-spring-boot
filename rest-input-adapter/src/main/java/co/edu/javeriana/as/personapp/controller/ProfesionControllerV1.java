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

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profesion")
public class ProfesionControllerV1 {

	@Autowired
	private ProfesionInputAdapterRest profesionInputAdapterRest;

	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProfesionResponse> profesiones(@PathVariable String database) {
		log.info("Into profesiones REST API");
		return profesionInputAdapterRest.historial(database.toUpperCase());
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse profesion(@PathVariable String database, @PathVariable Integer id) {
		log.info("Into profesion detail REST API");
		return profesionInputAdapterRest.buscarProfesion(database.toUpperCase(), id);
	}

	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse crearProfesion(@RequestBody ProfesionRequest request) {
		log.info("Into crearProfesion REST API");
		return profesionInputAdapterRest.crearProfesion(request);
	}

	@ResponseBody
	@PutMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse editarProfesion(@PathVariable String database, @PathVariable Integer id,
			@RequestBody ProfesionRequest request) {
		log.info("Into editarProfesion REST API");
		return profesionInputAdapterRest.editarProfesion(database.toUpperCase(), id, request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Response eliminarProfesion(@PathVariable String database, @PathVariable Integer id) {
		log.info("Into eliminarProfesion REST API");
		boolean deleted = profesionInputAdapterRest.eliminarProfesion(database.toUpperCase(), id);
		if (deleted) {
			return new Response("OK", "Profession deleted successfully", LocalDateTime.now());
		} else {
			return new Response("ERROR", "Profession could not be deleted", LocalDateTime.now());
		}
	}
}

