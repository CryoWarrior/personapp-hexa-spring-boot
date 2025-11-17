package co.edu.javeriana.as.personapp.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;

@Component
public class StudyMapperRest {

	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public StudyResponse fromDomainToAdapterRestMaria(Study study) {
		return new StudyResponse(
				study.getPerson() != null && study.getPerson().getIdentification() != null && study.getPerson().getIdentification() != 0 ? study.getPerson().getIdentification() : null,
				study.getProfession() != null && study.getProfession().getIdentification() != null && study.getProfession().getIdentification() != 0 ? study.getProfession().getIdentification() : null,
				study.getGraduationDate() != null ? study.getGraduationDate().format(dateFormatter) : null,
				study.getUniversityName(),
				DatabaseOption.MARIA.toString(),
				"OK",
				study.getPerson() != null && !"Unknown".equals(study.getPerson().getFirstName()) ? study.getPerson().getFirstName() + " " + study.getPerson().getLastName() : null,
				study.getProfession() != null && !"Unknown".equals(study.getProfession().getName()) ? study.getProfession().getName() : null
		);
	}

	public StudyResponse fromDomainToAdapterRestMongo(Study study) {
		return new StudyResponse(
				study.getPerson() != null && study.getPerson().getIdentification() != null && study.getPerson().getIdentification() != 0 ? study.getPerson().getIdentification() : null,
				study.getProfession() != null && study.getProfession().getIdentification() != null && study.getProfession().getIdentification() != 0 ? study.getProfession().getIdentification() : null,
				study.getGraduationDate() != null ? study.getGraduationDate().format(dateFormatter) : null,
				study.getUniversityName(),
				DatabaseOption.MONGO.toString(),
				"OK",
				study.getPerson() != null && !"Unknown".equals(study.getPerson().getFirstName()) ? study.getPerson().getFirstName() + " " + study.getPerson().getLastName() : null,
				study.getProfession() != null && !"Unknown".equals(study.getProfession().getName()) ? study.getProfession().getName() : null
		);
	}

	public Study fromAdapterToDomain(StudyRequest request, Person person, Profession profession) {
		Study study = new Study();
		study.setPerson(person);
		study.setProfession(profession);
		if (request.getGraduationDate() != null && !request.getGraduationDate().isEmpty()) {
			study.setGraduationDate(LocalDate.parse(request.getGraduationDate(), dateFormatter));
		}
		study.setUniversityName(request.getUniversityName());
		return study;
	}
}