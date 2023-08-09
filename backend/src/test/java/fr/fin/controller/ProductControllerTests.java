package fr.fin.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.fin.model.entity.Category;
import fr.fin.model.entity.Product;
import fr.fin.service.CategoryService;
import fr.fin.service.FileService;
import fr.fin.service.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private CategoryService categoryService;

	@MockBean
	private ProductService productService;

	@MockBean
	private FileService fileService;

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void givenExistingProducts_WhenGetAllProducts_ShouldReturnProducts() throws Exception {

		// Arrange
		List<Product> mockList = new ArrayList<Product>();
		Category category = new Category(1, "Viennoiseries", true, "Administrator", new Date(), false);
		Product product1 = new Product(1, "Pain au chocolat", true, 1.0d, false);
		product1.setCategory(category);
		Product product2 = new Product(2, "Croissant", true, 2.0d, false);
		product2.setCategory(category);

		mockList.addAll(List.of(product1, product2));
		when(productService.getAllProducts()).thenReturn(mockList);

		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.get("/products");

		// Execute and Assert
		mvc.perform(request)
				.andExpect(jsonPath("$[0].name").value("Pain au chocolat"))
				.andExpect(jsonPath("$[1].name").value("Croissant"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void givenExistingProduct_WhenGetProductById_ShouldReturnProduct() throws Exception {

		// Arrange
		Product product = new Product(1, "Pain au chocolat", true, 1.0d, false);
		when(productService.getProductById(1)).thenReturn(product);

		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.get("/products/1");

		// Execute and Assert
		mvc.perform(request)
			.andExpect(jsonPath("$.productId").value(1))
			.andExpect(jsonPath("$.name").value("Pain au chocolat"))
			.andExpect(jsonPath("$.price").value(1.0d));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void givenNonExistingProduct_WhenGetProductById_ShouldReurnStatusNotFound() throws Exception {

		// Arrange
		when(productService.getProductById(2)).thenReturn(null);

		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.get("/products/2");

		// Execute and Assert
		mvc.perform(request)
			.andExpect(status().isNotFound());
	}



}
