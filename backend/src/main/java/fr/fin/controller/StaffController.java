package fr.fin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.fin.auth.IsAdmin;
import fr.fin.exceptions.custom.ActionForbiddenException;
import fr.fin.exceptions.custom.ResourceNotFoundException;
import fr.fin.exceptions.custom.ValidationErrorException;
import fr.fin.model.dto.CheckPasswordDto;
import fr.fin.model.dto.StaffGestionPageDto;
import fr.fin.model.dto.StaffTablePageDto;
import fr.fin.model.entity.Staff;
import fr.fin.service.StaffService;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class StaffController {

	@Value("${finally.masteraccount}")
	private String masterAccountName;

	@Autowired
	private StaffService staffService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	/**
	 * Map Staff to Dto
	 *
	 * @param StaffGestionPageDto
	 * @return Staff
	 */
	private Staff convertToGestionEntity(StaffGestionPageDto staffDto) {
		return modelMapper.map(staffDto, Staff.class);
	}

	/**
	 * Map Dto to Staff
	 *
	 * @param staff
	 * @return StaffTablePageDto
	 */
	private StaffTablePageDto convertToTableDto(Staff staff) {
		return modelMapper.map(staff, StaffTablePageDto.class);
	}

	/**
	 * User table
	 *
	 * @return staffsDto view
	 */
	@GetMapping
	@IsAdmin
	public List<StaffTablePageDto> getAllStaff() {
		List<Staff> staffs = staffService.getAllStaffs();
		List<StaffTablePageDto> staffsDto = new ArrayList<StaffTablePageDto>();
		for (Staff staff : staffs) {
			staffsDto.add(convertToTableDto(staff));
		}
		return staffsDto;
	}

	/**
	 * Create a new staff, only manager can create a new staff, and control the
	 * validity of every input:
	 * Username cannot be null nor blank
	 * Password should be valid
	 * PasswordConfirm should be identical than Password
	 * isStatus by default
	 * CreatedBy and UpdateAt now by default
	 *
	 * @param staffDto
	 * @return ResponseEntity<StaffGestionPageDto>
	 * @throws ValidationErrorException
	 * @throws ResourceNotFoundException
	 */
	@PostMapping
	@IsAdmin
	public ResponseEntity<StaffGestionPageDto> addStaff(@RequestBody StaffGestionPageDto staffDto)
			throws ValidationErrorException, ResourceNotFoundException {

		if (staffDto.getUsername() != null && !staffDto.getUsername().isBlank() && !staffDto.getUsername().isEmpty()) {

			PasswordValidator passwordValidator = new PasswordValidator(staffDto.getPassword());

			// username cannot be exist already
			if (staffService.getStaffByUserName(staffDto.getUsername()) != null) {
				throw new ValidationErrorException(
						"Le nom d'utilisateur existe déjà, veuillez renommer l'utilisateur.");
			}

			// password needs to match the pattern
			if (!passwordValidator.isValid(staffDto.getPassword())) {
				throw new ValidationErrorException(passwordValidator.getMessage());
			}

			// passwordConfirm needs to be identical than password
			if (!staffDto.getPassword().equals(staffDto.getPasswordConfirm())) {
				throw new ValidationErrorException(
						"La confirmation de mot de passe doit être identique que le mot de passe.");
			}

			staffDto.setPassword(bCryptPasswordEncoder.encode(staffDto.getPassword()));
			staffDto.setPasswordConfirm("OK");
			staffDto.setStatus(true);
			staffDto.setCreatedAt(new Date());
			staffDto.setUpdateAt(new Date());

			Staff staffToCreate = convertToGestionEntity(staffDto);
			staffToCreate.setPasswordTrial(0);
			staffService.saveStaff(staffToCreate);
			return new ResponseEntity<StaffGestionPageDto>(staffDto, HttpStatus.CREATED);
		}

		throw new ValidationErrorException("Veuillez vérifier votre saisi.");
	}

	/**
	 * get staff info by id, prepare for updating
	 *
	 * @param id
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/{id}")
	public StaffGestionPageDto getStaffById(@PathVariable("id") Integer id) throws ResourceNotFoundException {

		if (staffService.getStaffById(id) != null && staffService.getStaffById(id).isStatus()) {
			Staff staff = staffService.getStaffById(id);
			staff.setPassword(null);
			return convertToGestionDto(staff);
		}
		throw new ResourceNotFoundException("Cet utilisateur n'existe pas");
	}

	private StaffGestionPageDto convertToGestionDto(Staff staff) {
		return modelMapper.map(staff, StaffGestionPageDto.class);
	}

	/**
	 * edit the staff, only manager can modify, need validaty input check
	 *
	 * @param id
	 * @return
	 * @throws ValidationErrorException
	 * @throws ResourceNotFoundException
	 */
	@PutMapping("/{id}")
	@IsAdmin
	public StaffTablePageDto updateStaffById(@PathVariable("id") Integer id, @RequestBody StaffGestionPageDto staffDto)
			throws ValidationErrorException, ResourceNotFoundException {
		if (staffDto.getUsername() != null) {
			Staff staffToUpdate = staffService.getStaffById(id);

			// staff cannot be 'deleted nor the same as current one
			if (staffToUpdate.isStatus()) {

				// update username
				// username cannot be exist already except than the user self
				if (staffService.getStaffByUserName(staffDto.getUsername()) != null
						&& !Objects.equals(id, staffService.getStaffByUserName(staffDto.getUsername()).getId())) {
					throw new ValidationErrorException(
							"Le nom d'utilisateur existe déjà, veuillez renommer l'utilisateur.");
				}

				staffToUpdate.setUsername(staffDto.getUsername());

				if ((staffDto.getPassword() == null && staffDto.getPasswordConfirm() == null)
						|| (staffDto.getPassword().isBlank() && staffDto.getPasswordConfirm().isBlank())
						|| (staffDto.getPassword().isEmpty() && staffDto.getPasswordConfirm().isEmpty())) {

					// leave password null will keep old password
					staffToUpdate.setPassword(staffToUpdate.getPassword());

				} else {
					// update password & passwordConfirm
					// objectify a password validator
					PasswordValidator passwordValidator = new PasswordValidator(staffDto.getPassword());

					// password needs to match the pattern
					if (!passwordValidator.isValid(staffDto.getPassword())) {
						throw new ValidationErrorException(passwordValidator.getMessage());
					}

					// passwordConfirm needs to be identical than password
					if (!staffDto.getPassword().equals(staffDto.getPasswordConfirm())) {
						throw new ValidationErrorException(
								"La confirmation de mot de passe doit être identique que le mot de passe.");
					}

					staffToUpdate.setPassword(bCryptPasswordEncoder.encode(staffDto.getPassword()));
				}

				staffToUpdate.setRole(staffDto.getRole());
				staffToUpdate.setUpdateAt(new Date());

				// update staff
				StaffTablePageDto staffUpdated = convertToTableDto(staffService.saveStaff(staffToUpdate));

				return staffUpdated;
			}
			throw new ResourceNotFoundException("Cet utilisateur n'existe pas.");
		}
		throw new ValidationErrorException("Erreur dans la requête.");
	}

	/**
	 * UPDATE the status of user, given its id, instead of completely deleting it
	 * from DB
	 *
	 * @param id The id of the user
	 * @return The user
	 * @throws ResourceNotFoundException
	 * @throws ActionForbiddenException
	 */
	@PatchMapping("/{id}")
	@IsAdmin
	public StaffGestionPageDto updateUserStatus(@PathVariable("id") Integer id)
			throws ResourceNotFoundException, ActionForbiddenException {
		Staff staff = staffService.getStaffById(id);
		if (staff != null) {
			if (!masterAccountName.equals(staff.getUsername())) {
				return convertToGestionDto(staffService.updateStaffStatus(id));
			}
			throw new ActionForbiddenException("Impossible de supprimer le compte");
		}
		throw new ResourceNotFoundException("L'utilisateur n'a pas été trouvé.");
	}

	/**
	 * GET the user given its username
	 *
	 * @param userName The username of the user
	 * @return The user
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/username/{userName}")
	@IsAdmin
	public StaffGestionPageDto findUserByUsername(@PathVariable("userName") String userName)
			throws ResourceNotFoundException {
		Staff staff = staffService.getStaffByUserName(userName);
		if (staff != null) {
			return convertToGestionDto(staffService.getStaffByUserName(userName));
		}
		throw new ResourceNotFoundException("L'utilisateur n'a pas été trouvé.");
	}

	/**
	 * Logout directely if logout w/o password or the wrong password, only correct
	 * password get sell recap.
	 * 
	 * @param checkPasswordDto
	 * @return boolean isPasswordMatchesHashedPasswordInDB
	 * @throws ValidationErrorException
	 */
	@PostMapping("/check")
	public boolean checkPasswordToLogout(@RequestBody CheckPasswordDto checkPasswordDto)
			throws ValidationErrorException {

		if (checkPasswordDto.getPassword() != null) {
			String hashedPassword = staffService.getPasswordById(checkPasswordDto.getUserId());
			return bCryptPasswordEncoder.matches(checkPasswordDto.getPassword(), hashedPassword);
		}
		return false;
	}
}
