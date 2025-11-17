package co.edu.javeriana.as.personapp.mongo.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import lombok.NonNull;

@Mapper
public class TelefonoMapperMongo {

	@Autowired
	private PersonaMapperMongo personaMapperMongo;

	public TelefonoDocument fromDomainToAdapter(Phone phone) {
		TelefonoDocument telefonoDocument = new TelefonoDocument();
		telefonoDocument.setId(phone.getNumber());
		telefonoDocument.setOper(phone.getCompany());
		telefonoDocument.setPrimaryDuenio(validateDuenio(phone.getOwner()));
		return telefonoDocument;
	}

	private PersonaDocument validateDuenio(@NonNull Person owner) {
		return owner != null ? personaMapperMongo.fromDomainToAdapter(owner) : new PersonaDocument();
	}

	public Phone fromAdapterToDomain(TelefonoDocument telefonoDocument) {
		Phone phone = new Phone();
		phone.setNumber(telefonoDocument.getId());
		phone.setCompany(telefonoDocument.getOper());
		phone.setOwner(validateOwner(telefonoDocument.getPrimaryDuenio()));
		return phone;
	}

	private @NonNull Person validateOwner(PersonaDocument duenio) {
		if (duenio != null) {
			// Crear persona simple sin referencias circulares
			Person person = new Person();
			person.setIdentification(duenio.getId());
			person.setFirstName(duenio.getNombre());
			person.setLastName(duenio.getApellido());
			person.setAge(duenio.getEdad());
			person.setGender(validateGender(duenio.getGenero()));
			// NO mapear phones aquí para evitar referencia circular
			return person;
		}
		return new Person();
	}
	
	private @NonNull Gender validateGender(String sex) {
		if (sex == null) return Gender.OTHER;
		return sex.equalsIgnoreCase("FEMALE") ? Gender.FEMALE : 
			   sex.equalsIgnoreCase("MALE") ? Gender.MALE : Gender.OTHER;
	}
}