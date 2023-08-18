package fr.fin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.listeners.MockCreationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.fin.model.entity.Product;
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
		
		mocklist.addAll(List.of(staff1));

		when(staffRepository.findAllByStatusTrue()).thenReturn(mocklist);

		// Execute
		List<Staff> staffs = staffService.getAllStaffs();

		// Assert
		assertThat(staffs).hasSize(1);
	}
	
	@Test
	void givenStaffIdAndDeletedIsFalse_WhenFindById_ShouldReturnExistingStaff() {

		// Arrange
		Staff staff = new Staff(1, "Mael", "passWord123!", 0, "ADMIN", true, new Date(), new Date());
		when(staffRepository.findById(1)).thenReturn(Optional.of(staff));

		/// Execute
		Staff result = staffService.getStaffById(1);

		// Assert
		assertThat(result.getUsername()).isEqualTo("Mael");
	}
	
	@Test
	void givenNotExistingStaffId_whenFindById_shouldReturnNull() {

		// Arrange
		when(staffRepository.findById(2)).thenReturn(Optional.ofNullable(null));

		// Execute
		Staff result = staffService.getStaffById(2);

		// Assert
		assertThat(result).isNull();
	}
	
	@Test
	void givenStaff_whenCreateNewStaff_shouldReturnNewStaff() {

		// Arrange
		Staff staff = new Staff();
		staff.setId(1);
		staff.setUsername("Mael");

		when(staffRepository.save(staff)).thenReturn(staff);

		// Execute
		Staff result = staffService.createStaff(staff);

		// Assert
		assertThat(result.getUsername()).isEqualTo("Mael");
	}
	
	@Test
	void givenStaffIsNull_WhenDelete_ShouldReturnError() {

		// Arrange
		when(staffRepository.findById(2)).thenReturn(Optional.ofNullable(null));

		// Execute
		Staff result = staffService.updateStaffStatus(2);

		// Assert
		assertThat(result).isNull();;
		verify(staffRepository, never()).delete(any(Staff.class));
		verify(staffRepository, never()).save(any(Staff.class));
	}
	
	@Test
	void givenStatusTrue_WhenPatchStaffStatus_ShouldBeFalse() {

		// Arrange
		Staff staff = new Staff(1, "Mael", "passWord123!", 0, "ADMIN", true, new Date(), new Date());
		when(staffRepository.findById(1)).thenReturn(Optional.ofNullable(staff));
		when(staffRepository.save(staff)).thenReturn(staff);

		// Execute
		Staff result = staffService.updateStaffStatus(1);

		// Assert
		assertThat(result.isStatus()).isFalse();
	}
}
