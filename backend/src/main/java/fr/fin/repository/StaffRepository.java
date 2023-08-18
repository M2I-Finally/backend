package fr.fin.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.fin.model.entity.Staff;

@Repository
public interface StaffRepository extends CrudRepository<Staff, Integer> {

	List<Staff> findByStatusTrue();
	Staff findByUsernameIgnoreCase(String username);
}
