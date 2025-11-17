package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {

	private StudyOutputPort studyPersistence;

	public StudyUseCase(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyPersistence) {
		this.studyPersistence = studyPersistence;
	}

	@Override
	public void setPersistence(StudyOutputPort studyOutputPort) {
		this.studyPersistence = studyOutputPort;
	}

	@Override
	public Study create(Study study) {
		log.debug("Into create Study on Application Domain");
		return studyPersistence.save(study);
	}

	@Override
	public Study edit(Integer personId, Integer professionId, Study study) throws NoExistException {
		Study oldStudy = studyPersistence.findById(personId, professionId);
		if (oldStudy != null) {
			return studyPersistence.save(study);
		}
		throw new NoExistException("The study (person " + personId + ", profession " + professionId
				+ ") does not exist into db, cannot be edited");
	}

	@Override
	public Boolean drop(Integer personId, Integer professionId) throws NoExistException {
		Study oldStudy = studyPersistence.findById(personId, professionId);
		if (oldStudy != null) {
			return studyPersistence.delete(personId, professionId);
		}
		throw new NoExistException("The study (person " + personId + ", profession " + professionId
				+ ") does not exist into db, cannot be dropped");
	}

	@Override
	public List<Study> findAll() {
		log.info("Output Study: " + studyPersistence.getClass());
		return studyPersistence.find();
	}

	@Override
	public Study findOne(Integer personId, Integer professionId) throws NoExistException {
		Study study = studyPersistence.findById(personId, professionId);
		if (study != null) {
			return study;
		}
		throw new NoExistException("The study (person " + personId + ", profession " + professionId
				+ ") does not exist into db, cannot be found");
	}

	@Override
	public List<Study> findByPerson(Integer personId) {
		return studyPersistence.findByPerson(personId);
	}

	@Override
		public List<Study> findByProfession(Integer professionId) {
		return studyPersistence.findByProfession(professionId);
	}

	@Override
	public Integer count() {
		return findAll().size();
	}
}
