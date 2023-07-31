package fr.fin.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.fin.model.dto.StaffGestionPageDto;
import fr.fin.model.dto.StaffTablePageDto;
import fr.fin.model.entity.Staff;
import fr.fin.service.StaffService;

@RestController
@RequestMapping("/users")
public class StaffController {
	// only manager can CRUD users/staffs
	private final String ROLE_CAN_UPDATE_USER = "manager";

	@Autowired
	private StaffService staffService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * User table
	 * @return staffsDto view
	 */
	@GetMapping
	public List<StaffTablePageDto> getAllStaff() {
		List<Staff> staffs = staffService.getAllStaffs();
		List<StaffTablePageDto> staffsDto = new ArrayList<StaffTablePageDto>();
		for (Staff staff : staffs) {
			staffsDto.add(convertToTableDto(staff));
		}
		return staffsDto;
	}

	private StaffTablePageDto convertToTableDto(Staff staff) {
		return modelMapper.map(staff, StaffTablePageDto.class);
	}

	/**
	 * Only manager can delete staff, manager cannot delete himself
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteStaff(@PathVariable("id") Integer id) {
		List<Staff> managers = staffService.getManager();

		if (staffService.getStaffById(id) == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le staff n'existe pas");
		}

		if (Arrays.asList(managers).contains(id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un manager ne peut pas être supprimé.");
		}
		// peut pas supprimer lui-même => besoin d'un utilisateur de loggé

		staffService.deleteStaffById(id);
		return new ResponseEntity<String>("Staff supprimé", HttpStatus.OK);

	}

	/**
	 * Create a new staff, only manager can create a new staff, and control the
	 * validity of every input
	 * 
	 * @param staffDto
	 * @return
	 */
	@PostMapping
	public ResponseEntity<StaffGestionPageDto> addStaff(@RequestBody StaffGestionPageDto staffDto) {
		if (staffDto != null && !staffDto.getUsername().isBlank()
				&& ROLE_CAN_UPDATE_USER.equals(staffDto.getRole().toLowerCase())) {
			staffService.createStaff(convertToGestionEntity(staffDto));
			return new ResponseEntity<StaffGestionPageDto>(staffDto, HttpStatus.CREATED);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur dans la requête");
	}

	private Staff convertToGestionEntity(StaffGestionPageDto staffDto) {
		return modelMapper.map(staffDto, Staff.class);
	}

	/**
	 * get staff info by id, prepare for updating
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("{id}")
	public StaffGestionPageDto getStaffById(@PathVariable("id") Integer id) {
		if (staffService.getStaffById(id) != null) {
			Staff staff = staffService.getStaffById(id);
			return convertToGestionDto(staff);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found");
	}

	private StaffGestionPageDto convertToGestionDto(Staff staff) {
		return modelMapper.map(staff, StaffGestionPageDto.class);
	}

	/**
	 * edit the staff, only manager can modify, need validaty input check
	 * 
	 * @param id
	 * @return
	 */
	@PutMapping("{id}")
	public ResponseEntity<StaffGestionPageDto> updateStaffById(@PathVariable("id") Integer id,
			@RequestBody StaffGestionPageDto staffDto) {
		if (staffDto != null) {
			Staff staffToUpdate = staffService.getStaffById(id);

			// update username
			if (staffDto.getUsername() != null) {
				staffToUpdate.setUsername(staffDto.getUsername());
			}

			// update password & passwordConfirm
			if (staffDto.getPassword() != null) {
				//objectify a password validator
				PasswordValidator passwordValidator = new PasswordValidator(staffDto.getPassword());
				
				//password needs to match the pattern
				if (!passwordValidator.isValid(staffDto.getPassword())) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,passwordValidator.getMessage());
				}

				//passwordConfirm needs to be identical than password
				if (!staffDto.getPassword().equals(staffDto.getPasswordConfirm())) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"La confirmation de mot de passe doit être identique que le mot de passe.");
				}


				staffToUpdate.setPassword(staffDto.getPassword());
			}

			staffToUpdate.setRole(staffDto.getRole());
			staffToUpdate.setUpdateAt(new Date());
			
			//update staff
			staffService.createStaff(staffToUpdate);

			return new ResponseEntity<StaffGestionPageDto>(staffDto, HttpStatus.OK);
		}

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur dans la requête");
	}

}
