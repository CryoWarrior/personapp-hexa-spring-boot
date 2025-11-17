package co.edu.javeriana.as.personapp.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;

public interface EstudiosRepositoryMongo extends MongoRepository<EstudiosDocument, String> {
	
	List<EstudiosDocument> findByPrimaryPersona_Id(Integer personId);
	
	List<EstudiosDocument> findByPrimaryProfesion_Id(Integer professionId);
}