package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;

@Mapper
public class TelefonoMapperCli {

	public TelefonoModelCli fromDomainToAdapterCli(Phone phone) {
		TelefonoModelCli telefonoModelCli = new TelefonoModelCli();
		telefonoModelCli.setNumero(phone.getNumber());
		telefonoModelCli.setOperador(phone.getCompany());
		telefonoModelCli.setDuenioId(phone.getOwner().getIdentification());
		telefonoModelCli.setDuenioNombre(phone.getOwner().getFirstName() + " " + phone.getOwner().getLastName());
		return telefonoModelCli;
	}
}