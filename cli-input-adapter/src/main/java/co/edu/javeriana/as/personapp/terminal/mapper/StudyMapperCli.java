package co.edu.javeriana.as.personapp.terminal.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;

@Component
public class StudyMapperCli {

	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public StudyModelCli fromDomainToAdapterCli(Study study) {
		StudyModelCli studyModelCli = new StudyModelCli();
		studyModelCli.setPersonId(study.getPerson() != null ? study.getPerson().getIdentification() : null);
		studyModelCli.setPersonName(study.getPerson() != null 
			? study.getPerson().getFirstName() + " " + study.getPerson().getLastName() 
			: null);
		studyModelCli.setProfessionId(study.getProfession() != null ? study.getProfession().getIdentification() : null);
		studyModelCli.setProfessionName(study.getProfession() != null ? study.getProfession().getName() : null);
		studyModelCli.setGraduationDate(study.getGraduationDate() != null 
			? study.getGraduationDate().format(dateFormatter) 
			: null);
		studyModelCli.setUniversityName(study.getUniversityName());
		return studyModelCli;
	}

	public Study fromAdapterToDomain(StudyModelCli studyModelCli, Person person, Profession profession) {
		Study study = new Study();
		study.setPerson(person);
		study.setProfession(profession);
		if (studyModelCli.getGraduationDate() != null && !studyModelCli.getGraduationDate().isEmpty()) {
			study.setGraduationDate(LocalDate.parse(studyModelCli.getGraduationDate(), dateFormatter));
		}
		study.setUniversityName(studyModelCli.getUniversityName());
		return study;
	}
}