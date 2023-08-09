package fr.fin.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.fin.model.dto.product.ProductGestionPageDto;
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


	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void givenAProductDtoWithImage_WhenAddProduct_ShouldReturnNewProduct() throws Exception {

		// Arrange
		// Mock Dto
		ProductGestionPageDto dto = new ProductGestionPageDto();
		Category category = new Category(1, "Sucreries", true, "Administrator", new Date(), false);
		dto.setName("Cookie");
		dto.setCategoryId(1);
		dto.setPrice(1.0d);
		dto.setTax(0.20d);
		String jsonDto = mapper.writeValueAsString(dto);

		// Mock Product
		Product product = new Product(1, "Cookie", true, 1.0d, false);
		product.setTax(0.20d);
		product.setCategory(category);

		// Mock Multipart
		MockMultipartFile imagePart = new MockMultipartFile("file", "image.png", "image/*", "1".getBytes());
		MockMultipartFile productPart = new MockMultipartFile("product", "", "application/json", jsonDto.getBytes(Charset.forName("UTF-8")));

		when(fileService.createImage(imagePart)).thenReturn("images/image.png");
		when(categoryService.getCategoryById(1)).thenReturn(category);
		when(productService.createProduct(any(Product.class))).thenReturn(product);

		// Execute
		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.multipart("/products")
					.file(imagePart)
					.file(productPart);

		// Assert
		mvc.perform(request)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.productId").value(1))
			.andExpect(jsonPath("$.name").value("Cookie"));
		verify(fileService, times(1)).createImage(imagePart);
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void givenAProductDtoWithoutImage_WhenAddProduct_ShouldReturnNewProduct() throws Exception {

		// Arrange
		// Mock Dto
		ProductGestionPageDto dto = new ProductGestionPageDto();
		Category category = new Category(1, "Sucreries", true, "Administrator", new Date(), false);
		dto.setName("Cookie");
		dto.setCategoryId(1);
		dto.setPrice(1.0d);
		dto.setTax(0.20d);
		String jsonDto = mapper.writeValueAsString(dto);

		// Mock Product
		Product product = new Product(1, "Cookie", true, 1.0d, false);
		product.setTax(0.20d);
		product.setCategory(category);

		// Mock Multipart
		MockMultipartFile productPart = new MockMultipartFile("product", "", "application/json", jsonDto.getBytes(Charset.forName("UTF-8")));

		when(categoryService.getCategoryById(1)).thenReturn(category);
		when(productService.createProduct(any(Product.class))).thenReturn(product);

		// Execute
		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.multipart("/products")
					.file(productPart);

		// Assert
		mvc.perform(request)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.productId").value(1))
			.andExpect(jsonPath("$.name").value("Cookie"));

		verify(fileService, never()).createImage(any());
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void givenAnInvalidErrorDto_WhenAddProduct_ShouldReturnBadRequestStatus() throws Exception {

		// Arrange
		// Mock Dto
		ProductGestionPageDto dto = new ProductGestionPageDto();
		dto.setName("Cooki!!!<<<@$$$e");
		dto.setCategoryId(1);
		String jsonDto = mapper.writeValueAsString(dto);

		// Mock Multipart
		MockMultipartFile productPart = new MockMultipartFile("product", "", "application/json", jsonDto.getBytes(Charset.forName("UTF-8")));

		// Execute
		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.multipart("/products")
					.file(productPart);

		// Assert
		mvc.perform(request)
			.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void givenAProductDtoWithImage_WhenUpdateProduct_ShouldReturnUpdatedProduct() throws Exception {

		// Arrange
		// Mock Dto
		ProductGestionPageDto dto = new ProductGestionPageDto();
		Category category = new Category(1, "Sucreries", true, "Administrator", new Date(), false);
		dto.setName("Cookie");
		dto.setCategoryId(1);
		dto.setPrice(1.0d);
		dto.setTax(0.20d);
		String jsonDto = mapper.writeValueAsString(dto);

		// Mock Product
		Product product = new Product(1, "Cookie", true, 1.0d, false);
		product.setTax(0.20d);
		product.setCategory(category);

		// Mock Multipart
		MockMultipartFile productPart = new MockMultipartFile("product", "", "application/json", jsonDto.getBytes(Charset.forName("UTF-8")));
		MockMultipartFile imagePart = new MockMultipartFile("file", "image.png", "image/*", "1".getBytes());

		when(productService.getProductById(1)).thenReturn(product);
		when(categoryService.getCategoryById(1)).thenReturn(category);
		when(productService.createProduct(any(Product.class))).thenReturn(product);

		// Execute
		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.multipart("/products/1")
					.file(productPart)
					.file(imagePart)
					.with(req -> {
						req.setMethod("PUT");
						return req;
					});

		// Assert
		mvc.perform(request)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.productId").value(1))
			.andExpect(jsonPath("$.name").value("Cookie"));

		verify(fileService, times(1)).createImage(any());
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void givenAProductDtoWithoutImage_WhenUpdateProduct_ShouldReturnUpdatedProduct() throws Exception {

		// Arrange
		// Mock Dto
		ProductGestionPageDto dto = new ProductGestionPageDto();
		Category category = new Category(1, "Sucreries", true, "Administrator", new Date(), false);
		dto.setName("Cookie");
		dto.setCategoryId(1);
		dto.setPrice(1.0d);
		dto.setTax(0.20d);
		String jsonDto = mapper.writeValueAsString(dto);

		// Mock Product
		Product product = new Product(1, "Cookie", true, 1.0d, false);
		product.setTax(0.20d);
		product.setCategory(category);

		// Mock Multipart
		MockMultipartFile productPart = new MockMultipartFile("product", "", "application/json", jsonDto.getBytes(Charset.forName("UTF-8")));

		when(productService.getProductById(1)).thenReturn(product);
		when(categoryService.getCategoryById(1)).thenReturn(category);
		when(productService.createProduct(any(Product.class))).thenReturn(product);

		// Execute
		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.multipart("/products/1")
					.file(productPart)
					.with(req -> {
						req.setMethod("PUT");
						return req;
					});

		// Assert
		mvc.perform(request)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.productId").value(1))
			.andExpect(jsonPath("$.name").value("Cookie"));

		verify(fileService, never()).createImage(any());
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void givenANonExistentId_WhenUpdateProduct_ShouldReturnStatusNotFound() throws Exception {

		// Arrange
		// Mock Dto
		ProductGestionPageDto dto = new ProductGestionPageDto();
		dto.setName("Cookie");
		dto.setCategoryId(1);
		dto.setPrice(1.0d);
		dto.setTax(0.20d);
		String jsonDto = mapper.writeValueAsString(dto);

		// Mock Multipart
		MockMultipartFile productPart = new MockMultipartFile("product", "", "application/json", jsonDto.getBytes(Charset.forName("UTF-8")));

		when(productService.getProductById(1)).thenReturn(null);

		// Execute
		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.multipart("/products/1")
					.file(productPart)
					.with(req -> {
						req.setMethod("PUT");
						return req;
					});

		// Assert
		mvc.perform(request)
			.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void givenABadDto_WhenUpdateProduct_ShouldReturnStatusBadRequest() throws Exception {

		// Arrange
		// Mock Dto
		ProductGestionPageDto dto = new ProductGestionPageDto();
		dto.setName("Cookie!!!!<<<$$$$&&&");
		dto.setCategoryId(null);
		dto.setPrice(-1d);
		dto.setTax(-0.20d);
		String jsonDto = mapper.writeValueAsString(dto);

		// Mock Multipart
		MockMultipartFile productPart = new MockMultipartFile("product", "", "application/json", jsonDto.getBytes(Charset.forName("UTF-8")));

		// Execute
		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders.multipart("/products/1")
					.file(productPart)
					.with(req -> {
						req.setMethod("PUT");
						return req;
					});

		// Assert
		mvc.perform(request)
			.andExpect(status().isBadRequest());
	}


}
