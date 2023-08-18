package fr.fin.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.fin.model.dto.StaffGestionPageDto;
import fr.fin.model.entity.Staff;
import fr.fin.service.StaffService;

@SpringBootTest
@AutoConfigureMockMvc
public class StaffControllerTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private StaffService staffService;

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenStaffsNotDeleted_whenGetAllStaffs_shouldReturnThem() throws Exception {

		// Arrange
		List<Staff> mockList = new ArrayList<Staff>();
		Staff staff1 = new Staff(1, "Mael", "passWord123!", 0, "ADMIN", true, new Date(), new Date());
		Staff staff2 = new Staff(2, "Jordan", "passWord123!", 0, "EMPLOYEE", true, new Date(), new Date());
		mockList.addAll(List.of(staff1, staff2));

		when(staffService.getAllStaffs()).thenReturn(mockList);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/users");

		// Execute
		MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

		// Assert
		String expectedResponse = """
				[
					{"id":1,"username":"Mael","role":"ADMIN","status":true},
					{"id":2,"username":"Jordan","role":"EMPLOYEE","status":true}
				]
				""";

		assertEquals(mapper.readTree(expectedResponse), mapper.readTree(response.getContentAsString()));
	}

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenStaffIsDeleted_whenGetAllStaffs_shouldNotReturnThem() throws Exception {

		// Arrange
		List<Staff> mockList = new ArrayList<Staff>();
		Staff staff1 = new Staff(1, "Mael", "passWord123!", 0, "ADMIN", true, new Date(), new Date());
		Staff staff2 = new Staff(2, "Jordan", "passWord123!", 0, "EMPLOYEE", false, new Date(), new Date());
		mockList.addAll(List.of(staff1));

		when(staffService.getAllStaffs()).thenReturn(mockList);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/users");

		// Execute
		MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

		// Assert
		String expectedResponse = """
					[{"id":1,"username":"Mael","role":"ADMIN","status":true}]
				""";

		assertEquals(mapper.readTree(expectedResponse), mapper.readTree(response.getContentAsString()));
	}

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenStaffWithDeletedFalse_whenGetStaffById_shouldReturnOne() throws Exception {

		// Arrange
		Staff staff = new Staff(1, "Mael", "passWord123!", 0, "ADMIN", true, null, null);
		when(staffService.getStaffById(1)).thenReturn(staff);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/users/1");

		// Execute
		MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

		// Assert
		String expectedResponse = """
						{
					    "id": 1,
					    "username": "Mael",
					    "password": null,
					    "passwordConfirm": null,
					    "role": "ADMIN",
					    "status": true,
					    "createdAt": null,
					    "updateAt": null
						}
				""";

		assertEquals(mapper.readTree(expectedResponse), mapper.readTree(response.getContentAsString()));
	}

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenStaffWithDeletedTrue_whenGetStaffById_shouldNotReturn() throws Exception {

		// Arrange
		Staff staff = new Staff(1, "Mael", "passWord123!", 0, "ADMIN", false, null, null);
		when(staffService.getStaffById(1)).thenReturn(null);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/users/1");

		// Execute and Assert
		mvc.perform(request).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenStaffWithDeletedTrue_whenGetStaffById_shouldThrowException() throws Exception {

		// Arrange
		when(staffService.getStaffById(2)).thenReturn(null);

		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.get("/users/2");

		// Execute and assert
		mvc.perform(request).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenStaffGestionPageDto_whenCreateStaffWithPassword_shouldHashPasswordGiven() throws Exception {

		// Arrange
		StaffGestionPageDto createStaffGestionPageDto = new StaffGestionPageDto();
		createStaffGestionPageDto.setPassword("passWord123!");

		PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		String hashedPassword = bCryptPasswordEncoder.encode("passWord123!");

		// Assert
		assertTrue(bCryptPasswordEncoder.matches(createStaffGestionPageDto.getPassword(), hashedPassword));
	}

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenStaffGestionPageDto_whenCreateStaffWithPassword_shouldHashGivenPassword() throws Exception {

		// Arrange
		StaffGestionPageDto createStaffGestionPageDto = new StaffGestionPageDto();
		createStaffGestionPageDto.setPassword("passWord123!");

		PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		String hashedPassword = bCryptPasswordEncoder.encode("passWord123!");

		// Assert
		assertFalse(bCryptPasswordEncoder.matches("!Password123", hashedPassword));
	}

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenStaffGesionPageDtoWithBadPasswordConfirmaion_whenCreateStaff_shouldReturnValidationError() throws Exception {

		// Arrange
		StaffGestionPageDto createStaffGestionPageDto = new StaffGestionPageDto();
		createStaffGestionPageDto.setUsername("Zoe");
		createStaffGestionPageDto.setPassword("passWord123!");
		createStaffGestionPageDto.setPasswordConfirm("!Password123");

		String badContentJson = mapper.writeValueAsString(createStaffGestionPageDto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users").content(badContentJson).contentType(MediaType.APPLICATION_JSON);

		// Execute and Assert
		mvc.perform(request).andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenStaffGesionPageDtoWithValidatedInfo_whenCreateStaff_shouldReturnValidationStatusOk() throws Exception {

		// Arrange
		StaffGestionPageDto createStaffGestionPageDto = new StaffGestionPageDto();
		createStaffGestionPageDto.setUsername("Zoe");
		createStaffGestionPageDto.setPassword("passWord123!");
		createStaffGestionPageDto.setPasswordConfirm("passWord123!");
		createStaffGestionPageDto.setRole("MANAGER");

		String json = mapper.writeValueAsString(createStaffGestionPageDto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users").content(json).contentType(MediaType.APPLICATION_JSON);

		// Execute and Assert
		mvc.perform(request).andExpect(status().isCreated());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenStaffGesionPageDtoWithNoUsername_whenCreateStaff_shouldReturnNotFoundMessage() throws Exception {

		// Arrange
		StaffGestionPageDto createStaffGestionPageDto = new StaffGestionPageDto();
		createStaffGestionPageDto.setUsername(null);

		String json = mapper.writeValueAsString(createStaffGestionPageDto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users").content(json).contentType(MediaType.APPLICATION_JSON);

		// Execute and Assert
		mvc.perform(request).andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenExistingStaffName_whenCreateStaff_thenReturnBadRequest() throws Exception {
		
		// Arrange
		Staff staff = new Staff(2, "Jordan", "passWord123!", 0, "EMPLOYEE", true, new Date(), new Date());
		when(staffService.getStaffByUserName("Jordan")).thenReturn(staff);
		String json = """
				{
				"id":4,
			    "username":"Jordan",
			    "password":"passWord123!",
			    "passwordConfirm":"passWord123!",
			    "role": "EMPLOYEE"
			    }
		""";

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users").content(json).contentType(MediaType.APPLICATION_JSON);

		// Execute and Assert
		mvc.perform(request).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenExistingStaffName_whenCreateStaffWithUsernameExistingInLowercase_thenReturnBadRequest() throws Exception {
		
		// Arrange
		Staff staff = new Staff(2, "Jordan", "passWord123!", 0, "EMPLOYEE", true, new Date(), new Date());
		when(staffService.getStaffByUserName("jordan")).thenReturn(staff);
		String json = """
				{
				"id":4,
			    "username":"jordan",
			    "password":"passWord123!",
			    "passwordConfirm":"passWord123!",
			    "role": "EMPLOYEE"
			    }
		""";

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users").content(json).contentType(MediaType.APPLICATION_JSON);

		// Execute and Assert
		mvc.perform(request).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenAnNonExistentStaff_whenPatchStatus_returnNotFoundStatus() throws Exception {

		// Arrange
		when(staffService.getStaffById(2)).thenReturn(null);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/users/2");

		// Execute and Assert
		mvc.perform(request).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenAStaffId_whenEditNameAndPassword_thenReturnUpdatedStaff() throws Exception {

		// Arrange


		//Simulate an existing staff
		Staff staff = new Staff(1, "Mael", "passWord123!", 0, "ADMIN", true, null, null);
		when(staffService.getStaffById(1)).thenReturn(staff);

		Staff staffToUpdate = staff;
		staffToUpdate.setUsername("MaelLePatron");

		//simulate update staff
		staff.setUsername("MaelLePatron");
		when(staffService.saveStaff(staffToUpdate)).thenReturn(staff);
		String json = """
			{
			"id":1,
		    "username":"MaelLePatron",
		    "password":"!passWord123",
		    "passwordConfirm":"!passWord123",
		    "role": "ADMIN"
		    }
		""";

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/users/1").content(json).contentType(MediaType.APPLICATION_JSON);

		// Execute
		MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

		// Assert
		String expectedResponse = """
						{
					    "id": 1,
					    "username": "MaelLePatron",
					    "role": "ADMIN",
					    "status": true
						}
				""";

		assertEquals(mapper.readTree(expectedResponse), mapper.readTree(response.getContentAsString()));
	}


	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenAStaffId_whenEditName_thenReturnUpdatedStaff() throws Exception {

		// Arrange


		//Simulate an existing staff
		Staff staff = new Staff(1, "Mael", "passWord123!", 0, "ADMIN", true, null, null);
		when(staffService.getStaffById(1)).thenReturn(staff);

		Staff staffToUpdate = staff;
		staffToUpdate.setUsername("MaelLePatron");

		//simulate update staff
		staff.setUsername("MaelLePatron");
		when(staffService.saveStaff(staffToUpdate)).thenReturn(staff);
		String json = """
			{
			"id":1,
		    "username":"MaelLePatron",
		    "password":null,
		    "passwordConfirm":null,
		    "role": "ADMIN"
		    }
		""";

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/users/1").content(json).contentType(MediaType.APPLICATION_JSON);

		// Execute
		MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

		// Assert
		String expectedResponse = """
						{
					    "id": 1,
					    "username": "MaelLePatron",
					    "role": "ADMIN",
					    "status": true
						}
				""";

		assertEquals(mapper.readTree(expectedResponse), mapper.readTree(response.getContentAsString()));
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenAnActiveStaff_WhenDeleteStaff_ShouldReturnStatusOk() throws Exception {

		// Arrange
		Staff staff = new Staff(1, "Mael", "passWord123!", 0, "ADMIN", true, null, null);
		when(staffService.getStaffById(1)).thenReturn(staff);
		staff.setStatus(false);
		when(staffService.updateStaffStatus(1)).thenReturn(staff);

		// Execute
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/users/1");

		// Assert
		mvc.perform(request).andExpect(status().isOk());
	}


	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenANonExistentStaff_whenDeleteStaff_ShouldReturnStatusNotFound() throws Exception {

		// Arrange
		when(staffService.getStaffById(1)).thenReturn(null);

		// Execute
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/users/1");

		// Assert
		mvc.perform(request).andExpect(status().isNotFound());
	}

}