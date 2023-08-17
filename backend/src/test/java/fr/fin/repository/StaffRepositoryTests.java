package fr.fin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import fr.fin.model.entity.Staff;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StaffRepositoryTests {

	@Autowired
	private StaffRepository staffRepository;

	@Test
	@DisplayName("findAllByStatusTrue(), Should return active staffs")
	@Sql("Staff_findAllByDeletedFalse.sql")
	void findByStatusTrue_ShouldReturn_ActiveStaffs() {
		List<Staff> staffs = staffRepository.findAllByStatusTrue();
		assertThat(staffs.get(0).getId()).isEqualTo(1);
		assertThat(staffs.get(1).getId()).isEqualTo(2);
	}

	@Test
	@DisplayName("findByUsername(username), should return staff with given user")
	@Sql("Staff_findStaffByUserNameDeletedFalse.sql")
	void findByUsername_ShouldReturn_User() {
		Staff staff = staffRepository.findByUsername("Mael");
		assertThat(staff.getUsername()).isEqualTo("Mael");
	}
	


}
