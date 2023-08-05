package fr.fin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.fin.exceptions.custom.ResourceNotFoundException;
import fr.fin.exceptions.custom.ValidationErrorException;
import fr.fin.model.dto.product.ProductGestionPageDto;
import fr.fin.model.dto.product.ProductShopPageDto;
import fr.fin.model.entity.Category;
import fr.fin.model.entity.Product;
import fr.fin.service.CategoryService;
import fr.fin.service.FileService;
import fr.fin.service.ProductService;
import jakarta.validation.Valid;

@RequestMapping("/products")
@CrossOrigin
@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ModelMapper modelMapper;


	/**
	 * GET a list of products from the database
	 * @return	List<ProductShopPageDto> containing products information
	 */
	@GetMapping
	public List<ProductShopPageDto> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		List<ProductShopPageDto> productsDto = new ArrayList<ProductShopPageDto>();

		for (Product product : products) {
			if(product.getPicture() != null) {
				product.setPicture(getBaseUrl() + product.getPicture());
			}
			ProductShopPageDto productDto = convertToShopDto(product);
			productDto.setCategoryId(product.getCategory().getId());
			productsDto.add(convertToShopDto(product));
		}
		return productsDto;
	}

	/**
	 * GET product information for a given id
	 * @param id	The Id of the product
	 * @return		Information about the product
	 */
	@GetMapping("/{id}")
	public ProductGestionPageDto getProductById(@PathVariable("id") Integer id) throws ResourceNotFoundException {
		if (productService.getProductById(id) != null) {
			Product product = productService.getProductById(id);
			if(product.getPicture() != null) {
				product.setPicture(getBaseUrl() + product.getPicture());
			}
			return convertToGestionDto(product);
		}

		throw new ResourceNotFoundException("Le produit n'a pas été trouvé");
	}

	/**
	 * Handle POST Mapping of a multi-part form-data with ProductGestionPageDto Object and a MultiPartFile
	 * @param productDto	Represents the data to send
	 * @param file			File that will be uploaded as an image (not required)
	 * @return				The product created in database
	 * @throws IOException	If the file was not processed correctly
	 */
	@PostMapping
	public ResponseEntity<ProductGestionPageDto> addProduct(
			@RequestPart("product") @Valid ProductGestionPageDto productDto,
			BindingResult bindingResult,
			@RequestPart(value = "file", required = false) MultipartFile file) throws IOException, ResourceNotFoundException, ValidationErrorException {

		if(bindingResult.hasErrors()) {
			throw new ValidationErrorException("Erreur de validation");
		}

		if (productDto != null) {

			// If there is a file, we process it and generate a relative URL
      		if (file != null) {
				String pictureRelativeURL = fileService.createImage(file);
				productDto.setPicture(pictureRelativeURL);
			}

      		// We need to set the category before creating the product
      		Category productCategory = categoryService.getCategoryById(productDto.getCategoryId());
      		Product productToCreate = modelMapper.map(productDto, Product.class);
      		productToCreate.setCategory(productCategory);

      		productToCreate = productService.createProduct(productToCreate);

			return new ResponseEntity<ProductGestionPageDto>(modelMapper.map(productToCreate, ProductGestionPageDto.class), HttpStatus.CREATED);
		}

		throw new ResourceNotFoundException("Le produit n'a pas été trouvé");
	}

	/**
	 * UPDATE information for a given product, given an identifier
	 * @param id			The id of the product
	 * @param productDto	Represents the data to send
	 * @param file			File that will be uploaded as an image (not required)
	 * @return				The product created in database
	 * @throws IOException	If the file was not processed correctly
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ProductGestionPageDto> updateProduct(
			@PathVariable("id") Integer id,
			@Valid @RequestPart("product") ProductGestionPageDto productDto,
			BindingResult bindingResult,
			@RequestPart(value = "file", required = false) MultipartFile file) throws IOException, ResourceNotFoundException, ValidationErrorException {

		if(bindingResult.hasErrors()) {
			throw new ValidationErrorException("Erreur de validation");
		}

		if (productService.getProductById(id) != null) {

			Product updatedProduct = productService.getProductById(id);
			Category updatedProductCategory = categoryService.getCategoryById(productDto.getCategoryId());

			updatedProduct.setName(productDto.getName());
			updatedProduct.setDescription(productDto.getDescription());
			updatedProduct.setCategory(updatedProductCategory);
			updatedProduct.setPrice(productDto.getPrice());
			updatedProduct.setTax(productDto.getTax());

			// If file is not null, we create a new image
			if (file != null) {
				String pictureRelativeURL = fileService.createImage(file);
				updatedProduct.setPicture(pictureRelativeURL);
			}

			// If file is null and product has already a picture, we set it again to avoid deletion
			if(file == null && productDto.getPicture() != null) {
				System.out.println(productDto.getPicture());
				updatedProduct.setPicture(productDto.getPicture());
			}

			updatedProduct.setUpdatedAt(new Date());
			updatedProduct = productService.createProduct(updatedProduct);
			return new ResponseEntity<ProductGestionPageDto>(convertToGestionDto(updatedProduct), HttpStatus.CREATED);
		}

		throw new ResourceNotFoundException("Le produit n'a pas été trouvé");
	}

	/**
	 * UPDATE the status of a product, given its id
	 * @param id	The id of the product
	 * @return		The product with its status updated
	 */
	@PatchMapping("/{id}")
	public ProductGestionPageDto updateProductStatus(@PathVariable("id") Integer id) {
		return convertToGestionDto(productService.updateProductStatus(id));
	}

	/**
	 * DELETE a product, given its id
	 * @param id	The id of the product
	 * @return		An empty object
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer id) throws ResourceNotFoundException {
		Product product = productService.getProductById(id);
		if (product != null && !product.isDeleted()) {
			productService.delete(id);
			return new ResponseEntity<String>("[]", HttpStatus.OK);
		}
		throw new ResourceNotFoundException("Le produit n'a pas été trouvé");
	}

	/**
	 * Get category for a given product ID
	 * @param id	The id of the category
	 * @return		A List
	 */
	@GetMapping("/category/{id}")
	public List<ProductShopPageDto> getProductByCategory(@PathVariable Integer id) {
		List<Product> products = productService.getProductsByCategory(id);
		List<ProductShopPageDto> productsDto = new ArrayList<ProductShopPageDto>();
		for( Product product: products ) {
			if(product.getPicture() != null) {
				product.setPicture(getBaseUrl() + product.getPicture());
			}
			productsDto.add(convertToShopDto(product));
		}
		return productsDto;
	}

	private ProductGestionPageDto convertToGestionDto(Product product) {
		return modelMapper.map(product, ProductGestionPageDto.class);
	}

	private ProductShopPageDto convertToShopDto(Product product) {
		return modelMapper.map(product, ProductShopPageDto.class);
	}

	private String getBaseUrl() {
		return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
	}
}
