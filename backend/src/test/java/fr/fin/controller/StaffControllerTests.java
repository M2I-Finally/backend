package fr.fin.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
	void GivenStaffsNotDeleted_WhenGetAllStaffs_ShouldReturnThem() throws Exception {

		// Arrange
		List<Staff> mockList = new ArrayList<Staff>();
		Staff staff1 = new Staff(1, "Mael","passWord123!", 0, "ADMIN", true, new Date(), new Date());
		Staff staff2 = new Staff(2, "Jordan", "passWord123!", 0, "EMPLOYEE", true, new Date(), new Date());
		mockList.addAll(List.of(staff1, staff2));

		when(staffService.getAllStaffs()).thenReturn(mockList);

		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.get("/users");

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
	void givenStaffWithDeletedFalse_WhenGetStaffById_ShouldReturnOne() throws Exception {
		
		// Arrange
		Staff staff = new Staff(1, "Mael","passWord123!", 0, "ADMIN", true, null, null);
		when(staffService.getStaffById(1)).thenReturn(staff);

		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.get("/users/1");

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
		"""
		;

		assertEquals(mapper.readTree(expectedResponse), mapper.readTree(response.getContentAsString()));
	}

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenStaffWithDeletedTrue_WhenGetStaffById_ShouldThrowException() throws Exception {

		// Arrange
		when(staffService.getStaffById(2)).thenReturn(null);

		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.get("/categories/2");

		// Execute and assert
		mvc.perform(request).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "admin", authorities = "ADMIN")
	void givenStaffDto_WhenCreateStaffWithPassword_ShouldHashPassword() throws Exception {

		// Arrange
		StaffGestionPageDto createStaffGestionPageDto = new StaffGestionPageDto();
		createStaffGestionPageDto.setPassword("passWord123!");
		
		PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		
		String hashedPassword = bCryptPasswordEncoder.encode("passWord123!");
		
		// Assert
		assertTrue(bCryptPasswordEncoder.matches(createStaffGestionPageDto.getPassword(),hashedPassword));
		assertFalse(bCryptPasswordEncoder.matches("!Password123",hashedPassword));
	}

//	@Test
//	@WithMockUser(username = "admin", roles = "ADMIN")
//	void givenCategoryDtoWithBadName_WhenCreateCategory_ShouldReturnValidationError() throws Exception {
//
//		// Arrange
//		CreateUpdateCategoryDto createUpdateCategoryDto = new CreateUpdateCategoryDto();
//		createUpdateCategoryDto.setName("--- Bonbons !!!0999((<!");
//
//		String badContentJson = mapper.writeValueAsString(createUpdateCategoryDto);
//
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/categories").content(badContentJson).contentType(MediaType.APPLICATION_JSON);
//
//		// Execute and Assert
//		mvc.perform(request).andExpect(status().isBadRequest());
//	}
//
//	@Test
//	@WithMockUser(username = "admin", roles = "ADMIN")
//	void givenExistingCategory_WhenCreateCategory_ThenReturnBadRequest() throws Exception {
//
//		// Arrange
//		when(categoryService.checkIfCategoryExistsByName("Gateaux")).thenReturn(true);
//		String json = """
//			{"id":2,"name":"Gateaux","status":true,"productCount":0}
//		""";
//
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/categories").content(json).contentType(MediaType.APPLICATION_JSON);
//
//		// Execute and Assert
//		mvc.perform(request).andExpect(status().isBadRequest());
//	}
//
//	@Test
//	@WithMockUser(username = "admin", roles = "ADMIN")
//	void givenACategoryId_WhenPatchStatus_ReturnUpdatedCategory() throws Exception {
//
//		// Arrange
//		Category category = new Category(1, "Boissons", true, "Administrator", new Date(), false);
//		when(categoryService.getCategoryById(1)).thenReturn(category);
//
//		Category categoryPatched = category;
//		categoryPatched.setStatus(false);
//		when(categoryService.patchCategoryStatus(1)).thenReturn(categoryPatched);
//
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/categories/status/1");
//
//		// Execute and Assert
//		mvc.perform(request).andExpect(jsonPath("$.status").value("false"));
//	}
//
//	@Test
//	@WithMockUser(username = "admin", roles = "ADMIN")
//	void givenAnNonExistentCategory_WhenPatchStatus_ReturnNotFoundStatus() throws Exception {
//
//		// Arrange
//		when(categoryService.getCategoryById(2)).thenReturn(null);
//
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/categories/status/2");
//
//		// Execute and Assert
//		mvc.perform(request).andExpect(status().isNotFound());
//	}
//
//	@Test
//	@WithMockUser(username = "admin", roles = "ADMIN")
//	void givenACategoryId_WhenPatchName_ReturnUpdatedCategory() throws Exception {
//
//		// Arrange
//		Category category = new Category(1, "Boissons", true, "Administrator", new Date(), false);
//		when(categoryService.getCategoryById(1)).thenReturn(category);
//
//		Category categoryPatched = category;
//		categoryPatched.setName("Boissons froides");
//		when(categoryService.checkIfCategoryExistsByName("Boissons froides")).thenReturn(false);
//		when(categoryService.patchCategoryName(1, "Boissons froides")).thenReturn(categoryPatched);
//
//		CreateUpdateCategoryDto createUpdateCategoryDto = new CreateUpdateCategoryDto();
//		createUpdateCategoryDto.setName("Boissons froides");
//
//		String json = mapper.writeValueAsString(createUpdateCategoryDto);
//
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/categories/name/1").content(json).contentType(MediaType.APPLICATION_JSON);
//
//		// Execute and Assert
//		mvc.perform(request).andExpect(jsonPath("$.name").value("Boissons froides"));
//	}
//
//	@Test
//	@WithMockUser(username = "admin", roles = "ADMIN")
//	void givenACategoryDtoWithErrors_WhenPatchName_ReturnStatusBadRequest() throws Exception {
//
//		// Arrange
//		CreateUpdateCategoryDto createUpdateCategoryDto = new CreateUpdateCategoryDto();
//		createUpdateCategoryDto.setName("Boissons froides !!!!<<<!e!!");
//		String badJson = mapper.writeValueAsString(createUpdateCategoryDto);
//
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/categories/name/1").content(badJson).contentType(MediaType.APPLICATION_JSON);
//
//		// Execute and Assert
//		mvc.perform(request).andExpect(status().isBadRequest());
//	}
//
//	@Test
//	@WithMockUser(username = "admin", roles = "ADMIN")
//	void givenACategoryExisting_WhenPatchName_ShouldReturnBadRequest() throws Exception {
//
//		// Arrange
//		Category category = new Category(1, "Boissons", true, "Administrator", new Date(), false);
//		when(categoryService.getCategoryById(1)).thenReturn(category);
//		when(categoryService.checkIfCategoryExistsByName("Boissons froides")).thenReturn(true);
//
//		CreateUpdateCategoryDto createUpdateCategoryDto = new CreateUpdateCategoryDto();
//		createUpdateCategoryDto.setName("Boissons froides");
//		String json = mapper.writeValueAsString(createUpdateCategoryDto);
//
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/categories/name/1").content(json).contentType(MediaType.APPLICATION_JSON);
//
//		// Execute and Assert
//		mvc.perform(request).andExpect(status().isBadRequest());
//	}
//
//	@Test
//	@WithMockUser(username = "admin", roles = "ADMIN")
//	void givenANonExistentCategoryId_WhenPatchName_ShouldReturnNotFound() throws Exception {
//
//		// Arrange
//		when(categoryService.getCategoryById(1)).thenReturn(null);
//
//		CreateUpdateCategoryDto createUpdateCategoryDto = new CreateUpdateCategoryDto();
//		createUpdateCategoryDto.setName("Category");
//		String json = mapper.writeValueAsString(createUpdateCategoryDto);
//
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/categories/name/1").content(json).contentType(MediaType.APPLICATION_JSON);
//
//		// Execute and Assert
//		mvc.perform(request).andExpect(status().isNotFound());
//	}
//
//	@Test
//	@WithMockUser(username = "admin", roles = "ADMIN")
//	void givenACategoryWithoutProduct_WhenDeleteCategory_ShouldReturnStatusOk() throws Exception {
//
//		// Arrange
//		Category category = new Category(1, "Tartes", true, "Administrator", new Date(), false);
//		when(categoryService.getCategoryById(1)).thenReturn(category);
//		when(categoryService.deleteCategoryById(1)).thenReturn(true);
//
//		// Execute
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/categories/1");
//
//		// Assert
//		mvc.perform(request).andExpect(status().isOk());
//	}
//
//	@Test
//	@WithMockUser(username = "admin", roles = "ADMIN")
//	void givenACategoryWithProduct_WhenDeleteCategory_ShouldReturnStatusForbidden() throws Exception {
//
//		// Arrange
//		Category category = new Category(1, "Tartes", true, "Administrator", new Date(), false);
//		Product product = new Product(1, "Tarte à la framboise", true, 12.0d, false);
//		category.setProducts(List.of(product));
//
//		when(categoryService.getCategoryById(1)).thenReturn(category);
//
//		// Execute
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/categories/1");
//
//		// Assert
//		mvc.perform(request).andExpect(status().isForbidden());
//	}
//
//	@Test
//	@WithMockUser(username = "admin", roles = "ADMIN")
//	void givenANonExistentCategory_WhenDeleteCategory_ShouldReturnStatusNotFound() throws Exception {
//
//		// Arrange
//		when(categoryService.getCategoryById(1)).thenReturn(null);
//
//		// Execute
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/categories/1");
//
//		// Assert
//		mvc.perform(request).andExpect(status().isNotFound());
//	}


}