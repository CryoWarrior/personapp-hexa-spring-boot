package co.edu.javeriana.as.personapp.mariadb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;

public interface EstudiosRepositoryMaria extends JpaRepository<EstudiosEntity, EstudiosEntityPK> {
	
	List<EstudiosEntity> findByEstudiosEntityPK_CcPer(Integer ccPer);
	
	List<EstudiosEntity> findByEstudiosEntityPK_IdProf(Integer idProf);
}