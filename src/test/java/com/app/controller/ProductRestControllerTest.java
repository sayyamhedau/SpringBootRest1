package com.app.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.app.entity.ProductDTO;
import com.app.exec.ProductNotFoundException;
import com.app.service.impl.ProductServiceImpl;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
class ProductRestControllerTest {

	@MockBean
	private ProductServiceImpl productServiceImpl;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldCreateMockMvc() {
		assertNotNull(mockMvc);
	}

	@Test
	void shouldReturnDefaultMsg() throws Exception {
		mockMvc.perform(get("/rest/api/product/welcome"))
				.andExpect(MockMvcResultMatchers.content().string("Welcome to Product Rest  Controller"));
	}

	@Test
	void shouldReturnListOfProducts() throws Exception {
		when(productServiceImpl.getAllProducts()).thenReturn(Stream
				.of(new ProductDTO(1006L, "P006", "Laptop1", "Electronics", 16000.0, 3.0, 5.0, 10),
					new ProductDTO(1007L, "P007", "Laptop7", "Electronics", 26000.0, 4.0, 7.0, 10))
				.collect(Collectors.toList()));

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/rest/api/product/all")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", Matchers.is(2)))
				.andExpect(jsonPath("$[0].id", Matchers.is(1006)))
				.andExpect(jsonPath("$[0].productCode", Matchers.is("P006")))
				.andExpect(jsonPath("$[0].productName", Matchers.is("Laptop1")))
				.andExpect(jsonPath("$[0].productType", Matchers.is("Electronics")))
				.andExpect(jsonPath("$[0].productCost", Matchers.is(16000.0)))
				.andExpect(jsonPath("$[0].productCost", Matchers.is(16000.0)))
				.andExpect(jsonPath("$[0].productDiscount", Matchers.is(3.0)))
				.andExpect(jsonPath("$[0].productGst", Matchers.is(5.0)))
				.andExpect(jsonPath("$[0].productQuantity", Matchers.is(10)));
	}

	@Test
	void shouldCreateNewProduct() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/rest/api/product/save")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(asJsonString(new ProductDTO(1007L, "P007", "Laptop7", "Electronics", 26000.0, 4.0, 7.0, 10))))
				.andExpect(status().isCreated());
	}

	@Test
	void shouldDeleteProductRecord() throws Exception {
		ProductDTO productDTO = new ProductDTO(1007L, "P007", "Laptop7", "Electronics", 26000.0, 4.0, 7.0, 10);
		
		ProductServiceImpl spyProductServiceImpl = Mockito.spy(ProductServiceImpl.class);
		when(productServiceImpl.isProductExistByProductId(productDTO.getId())).thenReturn(true);
		doNothing().when(spyProductServiceImpl).deleteProduct(productDTO.getId());
		
		ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.delete("/rest/api/product/delete/{id}", productDTO.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted());
		
		assertNotNull(result.andReturn().getResponse().getContentAsString(), () -> "Content Should not be null");
		verify(productServiceImpl, times(1)).deleteProduct(productDTO.getId());
	}
	
	@Test
	void shouldReturnOnlyOneProduct() throws Exception {
		ProductDTO productDTO = new ProductDTO(1007L, "P007", "Laptop7", "Electronics", 26000.0, 4.0, 7.0, 10);
		
		when(productServiceImpl.getProduct(1007L)).thenReturn(Optional.of(productDTO));
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rest/api/product/1007")
				.accept(MediaType.APPLICATION_JSON))	
				.andExpect(status().isOk());
	}
	
	
	@Test
	void getProduct_shouldReturnProductNotFoundException() throws Exception {
		when(productServiceImpl.getProduct(1005L)).thenReturn(Optional.empty());
		ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/rest/api/product/1005")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		
		assertEquals(ProductNotFoundException.class, 
				result.andReturn().getResolvedException().getClass(), () -> "Should throw ProductNotFoundException while Getting Single Product");
	}
	
	@Test
	void updateProduct_shouldReturnProductNotFoundException() throws Exception {
		Mockito.when(productServiceImpl.isProductExistByProductId(Mockito.anyLong())).thenReturn(false);
		
		ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.delete("/rest/api/product/delete/{id}", Mockito.anyLong())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		
		assertEquals(ProductNotFoundException.class, result.andReturn().getResolvedException().getClass(), () -> "Should throw ProductNotFoundException While Deleting Product");
	}
	
	@Test
	void shouldUpdateProductRecord() throws Exception{
		ProductDTO productDTO = new ProductDTO(1007L, "P007", "Laptop7", "Electronics", 26000.0, 4.0, 7.0, 10);
		
		when(productServiceImpl.getProduct(1007L)).thenReturn(Optional.of(productDTO));
		doNothing().when(productServiceImpl).updateProduct(productDTO);
		
		this.mockMvc.perform(MockMvcRequestBuilders.put("/rest/api/product/update/1007")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(productDTO)))
				.andExpect(status().isOk());
	}
	
	private static String asJsonString(final Object obj) throws JsonProcessingException {
		return new ObjectMapper().setSerializationInclusion(Include.NON_NULL).writeValueAsString(obj);
	}
	
}
