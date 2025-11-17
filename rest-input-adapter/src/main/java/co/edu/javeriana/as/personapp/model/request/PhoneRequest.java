package co.edu.javeriana.as.personapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneRequest {
	private String number;
	private String company;
	private String ownerId;      // ID del propietario como String
	private String database;     // Base de datos a usar ("MARIA"/"MONGO")
}