package com.app.controller;

import java.util.Objects;
import java.util.Optional;

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

import com.app.entity.ProductDTO;
import com.app.exec.ProductNotFoundException;
import com.app.service.IProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/rest/api/product")
@Api(value = "/rest/api/product", tags = "Product Service")
public class ProductRestController {

	@Autowired
	private IProductService productService;

	@GetMapping(value = "/welcome")
	@ApiOperation(value = "Shows a Default Welcome Message", tags = "Product Service")
	public String welcome() {
		return "Welcome to Product Rest  Controller";
	}

	@PostMapping(value = "/save", produces = { "application/json", "application/xml" })
	@ApiOperation(value = "Creates a New Product", tags = "Product Service")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Product Already Exist With Product Code"),
			@ApiResponse(code = 201, message = "Product Saved Successfully!"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public ResponseEntity<String> saveProduct(
			@ApiParam("Product Payload Body. Cannot be empty.") @RequestBody ProductDTO productDto) {
		ResponseEntity<String> resp = null;
		try {
			if (Objects.isNull(productDto)) {
				resp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Please pass the productDto payload body");
			} else if (productService.isProductExistByProductCode(productDto.getProductCode())) {
				resp = ResponseEntity.status(HttpStatus.OK)
						.body("Product Already Exist With Product Code - " + productDto.getProductCode());
			} else {
				Long id = productService.saveProduct(productDto);
				resp = ResponseEntity.status(HttpStatus.CREATED).body("Product Saved Successfully With Id - " + id);
			}
		} catch (Exception e) {
			resp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to save product");
			e.printStackTrace();
		}
		return resp;
	}

	@GetMapping(value = "/all", produces = { "application/json", "application/xml" })
	@ApiOperation(value = "Return list of all products", tags = "Product Service")
	public ResponseEntity<?> getAllProducts() {
		ResponseEntity<?> resp = null;
		try {
			resp = ResponseEntity.ok().body(productService.getAllProducts());
		} catch (Exception e) {
			resp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to fetch records");
			e.printStackTrace();
		}
		return resp;
	}

	
	@DeleteMapping(value = "/delete/{id}")
	@ApiOperation(value = "Deletes a product", tags = "Product Service")
	public ResponseEntity<String> deleteProduct(
			@ApiParam("Id of the Product to be delete. Cannot be empty.") @PathVariable(value = "id") Long id) {

		if (!productService.isProductExistByProductId(id)) {
			throw new ProductNotFoundException("Product Not Found With Id - " + id);
		}
		productService.deleteProduct(id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Product Deleted Successfully With Id - " + id);
	}

	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
	@ApiOperation(value = "Returns product based on unique id (Product Id)", tags = "Product Service")
	public ResponseEntity<ProductDTO> getProduct(
			@ApiParam("Id of the Product to return product. Cannot be empty.") @PathVariable(value = "id") Long id) {
		Optional<ProductDTO> optionalProduct = productService.getProduct(id);
		if (!optionalProduct.isPresent()) {
			throw new ProductNotFoundException("Product Not Found With Id - " + id);
		}
		return ResponseEntity.status(HttpStatus.OK).body(optionalProduct.get());
	}

	@PutMapping(value = "/update/{id}")
	@ApiOperation(value = "Update a existing product based on  unique id & Product payload", tags = "Product Service")
	public ResponseEntity<String> updateProduct(
			@ApiParam("Id of the Product to be update. Cannot be empty.") @PathVariable(value = "id") Long id,
			@RequestBody ProductDTO productDTO) {

		Optional<ProductDTO> optionalProduct = productService.getProduct(id);
		if (!optionalProduct.isPresent()) {
			throw new ProductNotFoundException("Product Not Found With Id - " + id);
		}

		ProductDTO product = optionalProduct.get();
		product.setProductCode(productDTO.getProductCode());
		product.setProductCost(productDTO.getProductCost());
		product.setProductDiscount(productDTO.getProductDiscount());
		product.setProductGst(productDTO.getProductGst());
		product.setProductName(productDTO.getProductName());
		product.setProductQuantity(productDTO.getProductQuantity());
		product.setProductType(productDTO.getProductType());

		productService.updateProduct(product);
		return ResponseEntity.status(HttpStatus.OK).body("Product Updated Successfully With Id - " + id);
	}
}
