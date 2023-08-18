package fr.fin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.listeners.MockCreationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.fin.model.entity.Staff;
import fr.fin.repository.StaffRepository;

@SpringBootTest
public class StaffServiceTests {

	@MockBean
	private StaffRepository staffRepository;

	@Autowired
	private StaffService staffService;

	@Test
	void givenStaffs_whenGetAllActiveStaffs_shouldReturnAListOfThem() {

		// Arrange
		List<Staff> mocklist = new ArrayList<Staff>();

		Staff staff1 = new Staff(1, "Mael", "passWord123!", 0, "ADMIN", true, new Date(), new Date());
		Staff staff2 = new Staff(2, "Jordan", "passWord123!", 0, "EMPLOYEE", true, new Date(), new Date());

		mocklist.addAll(List.of(staff1, staff2));
		when(staffRepository.findAllByStatusTrue()).thenReturn(mocklist);

		// Execute
		List<Staff> staffs = staffService.getAllStaffs();

		// Assert
		assertThat(staffs.get(0).getUsername()).isEqualTo("Mael");
		assertThat(staffs.get(1).getUsername()).isEqualTo("Jordan");
	}
	
	@Test
	void givenStaffs_whenGetAllActiveStaffs_shouldNotReturnNotActiveOnes() {

		// Arrange
		List<Staff> mocklist = new ArrayList<Staff>();

		Staff staff1 = new Staff(1, "Mael", "passWord123!", 0, "ADMIN", true, new Date(), new Date());
		Staff staff2 = new Staff(2, "Jordan", "passWord123!", 0, "EMPLOYEE", false, new Date(), new Date());
		Staff staff3 = new Staff(3, "Zoe", "passWord123!", 0, "MANAGER", false, new Date(), new Date());
		
		mocklist.addAll(List.of(staff1, staff2, staff3));
		
		for (int i=0; i < mocklist.size(); i++) {
			if (!mocklist.get(i).isStatus()) {
				mocklist.remove(i);
			}
		}

		when(staffRepository.findAllByStatusTrue()).thenReturn(mocklist);

		// Execute
		List<Staff> staffs = staffService.getAllStaffs();

		// Assert
		assertThat(staffs).hasSize(1);
	}
}
