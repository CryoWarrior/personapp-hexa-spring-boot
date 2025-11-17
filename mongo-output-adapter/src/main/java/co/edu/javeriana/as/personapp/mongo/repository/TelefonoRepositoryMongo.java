package co.edu.javeriana.as.personapp.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;

public interface TelefonoRepositoryMongo extends MongoRepository<TelefonoDocument, String> {
	
	List<TelefonoDocument> findByPrimaryDuenio_Id(Integer personId);
	
	List<TelefonoDocument> findByOper(String company);
}