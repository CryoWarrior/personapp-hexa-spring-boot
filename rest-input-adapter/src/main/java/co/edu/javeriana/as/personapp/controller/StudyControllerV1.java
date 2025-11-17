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

import co.edu.javeriana.as.personapp.adapter.StudyInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/estudio")
public class StudyControllerV1 {

	@Autowired
	private StudyInputAdapterRest studyInputAdapterRest;

	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<StudyResponse> estudios(@PathVariable String database) {
		log.info("Into estudios REST API");
		return studyInputAdapterRest.historial(database.toUpperCase());
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{personId}/{professionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public StudyResponse estudio(@PathVariable String database, @PathVariable Integer personId, @PathVariable Integer professionId) {
		log.info("Into estudio detail REST API");
		return studyInputAdapterRest.buscarEstudio(database.toUpperCase(), personId, professionId);
	}

	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StudyResponse crearEstudio(@RequestBody StudyRequest request) {
		log.info("Into crearEstudio REST API");
		return studyInputAdapterRest.crearEstudio(request);
	}

	@ResponseBody
	@PutMapping(path = "/{database}/{personId}/{professionId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StudyResponse editarEstudio(@PathVariable String database, @PathVariable Integer personId, @PathVariable Integer professionId,
			@RequestBody StudyRequest request) {
		log.info("Into editarEstudio REST API");
		return studyInputAdapterRest.editarEstudio(database.toUpperCase(), personId, professionId, request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{personId}/{professionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Response eliminarEstudio(@PathVariable String database, @PathVariable Integer personId, @PathVariable Integer professionId) {
		log.info("Into eliminarEstudio REST API");
		boolean deleted = studyInputAdapterRest.eliminarEstudio(database.toUpperCase(), personId, professionId);
		if (deleted) {
			return new Response("OK", "Study deleted successfully", LocalDateTime.now());
		} else {
			return new Response("ERROR", "Study could not be deleted", LocalDateTime.now());
		}
	}

	@ResponseBody
	@GetMapping(path = "/{database}/persona/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<StudyResponse> estudiosPorPersona(@PathVariable String database, @PathVariable Integer personId) {
		log.info("Into estudiosPorPersona REST API");
		return studyInputAdapterRest.estudiosPorPersona(database.toUpperCase(), personId);
	}

	@ResponseBody
	@GetMapping(path = "/{database}/profesion/{professionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<StudyResponse> estudiosPorProfesion(@PathVariable String database, @PathVariable Integer professionId) {
		log.info("Into estudiosPorProfesion REST API");
		return studyInputAdapterRest.estudiosPorProfesion(database.toUpperCase(), professionId);
	}
}