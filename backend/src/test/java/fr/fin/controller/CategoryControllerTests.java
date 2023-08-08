package fr.fin.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.fin.model.entity.Category;
import fr.fin.service.CategoryService;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private CategoryService categoryService;

	@Test
	@WithMockUser(username = "user", roles = "ADMIN")
	void GivenCategoriesWithDeletedFalse_WhenGetAllCategories_ShouldReturnThem() throws Exception {

		// Arrange
		List<Category> mockList = new ArrayList<Category>();
		Category category1 = new Category(1, "Pains", true, "Administrator", new Date(), false);
		Category category2 = new Category(2, "Patisseries", true, "Administrator", new Date(), false);
		mockList.addAll(List.of(category1, category2));

		when(categoryService.getAllCategories()).thenReturn(mockList);

		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.get("/categories");

		// Execute
		MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

		// Assert
		String expectedResponse = """
			[
			{"id":1,"name":"Pains","status":true,"productCount":0},
			{"id":2,"name":"Patisseries","status":true,"productCount":0}
			]
		""";

		assertEquals(mapper.readTree(expectedResponse), mapper.readTree(response.getContentAsString()));

	}

}
