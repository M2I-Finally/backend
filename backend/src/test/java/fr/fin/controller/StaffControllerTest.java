package fr.fin.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import fr.fin.model.dto.StaffGestionPageDto;
import fr.fin.model.entity.Staff;
import fr.fin.service.StaffService;

@SpringBootTest
@AutoConfigureMockMvc
public class StaffControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private StaffService staffService;
	
	@BeforeEach
	public void initMael() {
		Staff mael = new Staff(
				"mael",
				"password",
				0,
				"manager",
				true,
				new Date(),
				new Date()
				);
	}
	
	
	
	
	@Test
	void when_managerDeleteAStaffExisting_then_givenIdStaffDeleted() throws Exception {
		
		
		mvc.perform(MockMvcRequestBuilders.get("/users")).andExpect(status().isOk());
	}
	
	

	@Test
	void when_deleteAStaffNotExisting_then_throwNotExistingError() {
		
		String request = "";
		assertEquals("Le staff n'existe pas",request);
	}
	
	@Test
	void when_employeTriesToDelete_then_throwBadRequestError() {
		
		String request = "";
		assertEquals("Un manager ne peut pas être supprimé.",request);
	}
	
	@Test
	void when_managerUpdateStaffById_then_StaffUpdatedAccordingToUserInput() {
		Staff jordan = new Staff(
				"jordan",
				"password",
				0,
				"employe",
				true,
				new Date(),
				new Date()
				);
		jordan.setStaffId(2);
		
		StaffGestionPageDto staffDto = new StaffGestionPageDto();
		
		staffDto.setUsername("zoe");
		staffDto.setPassword("psw123");
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put("/users?id=2")
				.content("""
						{
						"id":2,
						"name":"zoe",
						"password":"password",
						}
						""")
				.contentType(MediaType.APPLICATION_JSON);
		
		verify(staffService, times(1)).saveStaff(jordan);
	}
	
	@Test
	void when_employeUpdateStaffById_then_throwBadRequestError() {
		
	}
	
	@Test
	void when_passwordConfirmIsNotTheSameThanPassword_then_throwBadRequestError() {
		
	}
	
}
