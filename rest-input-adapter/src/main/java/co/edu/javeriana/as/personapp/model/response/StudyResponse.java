package co.edu.javeriana.as.personapp.model.response;

import co.edu.javeriana.as.personapp.model.request.StudyRequest;

public class StudyResponse extends StudyRequest {

	private String status;
	private String personName;
	private String professionName;

	public StudyResponse(Integer personId, Integer professionId, String graduationDate,
			String universityName, String database, String status, String personName, String professionName) {
		super(personId, professionId, graduationDate, universityName, database);
		this.status = status;
		this.personName = personName;
		this.professionName = professionName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getProfessionName() {
		return professionName;
	}

	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}
}