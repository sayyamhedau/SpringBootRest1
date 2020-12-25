package com.app.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.entity.ProductDTO;
import com.app.repo.ProductRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ProductServiceImplTest {

	@MockBean
	private ProductRepository productRepository;

	@Autowired
	private ProductServiceImpl productServiceImpl;

	private static ProductDTO productDTO;

	@BeforeAll
	static void setUp() {
		productDTO = new ProductDTO(1001L, "P001", "Redmi Note5 Pro", "Electronics", 16000.0, 4.0, 3.0, 12);
	}

	@Test
	void shouldCreateNewProduct() {
		when(productRepository.save(Mockito.any(ProductDTO.class))).thenReturn(productDTO);
		assertEquals(1001L, productRepository.save(productDTO).getId());
	}

	@Test
	void shouldReturnAllProducts() {
		List<ProductDTO> productList = Stream
				.of(new ProductDTO(1001L, "P001", "Product1", "Electronics", 16000.0, 4.0, 3.0, 12),
						new ProductDTO(1001L, "P001", "Product1", "Electronics", 16000.0, 4.0, 3.0, 12))
				.collect(Collectors.toList());

		when(productRepository.findAll()).thenReturn(productList);
		assertEquals(2, productServiceImpl.getAllProducts().size(), () -> "All products are not fetched");
	}

	@Test
	void shouldReturnOnlyOneProduct() {
		when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(productDTO));
		assertEquals(productDTO, productServiceImpl.getProduct(Mockito.anyLong()).get());
	}

	@Test
	void checkIsProductExistByProductCode() {
		when(productRepository.findByProductCode(Mockito.anyString())).thenReturn(Optional.of(productDTO));
		assertTrue(productServiceImpl.isProductExistByProductCode(Mockito.anyString()));
	}

	@Test
	void checkIsProductExistByProductId() {
		when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(productDTO));
		assertTrue(productServiceImpl.isProductExistByProductId(Mockito.anyLong()));
	}

	@Test
	void shouldDeleteProductRecord() {
		productServiceImpl.deleteProduct(productDTO.getId());
		productServiceImpl.deleteProduct(productDTO.getId());
		verify(productRepository, times(2)).deleteById(productDTO.getId());
	}
	
	@Test
	void shouldUpdateProductRecord() {
		when(productRepository.save(Mockito.any(ProductDTO.class))).thenReturn(productDTO);
		productServiceImpl.updateProduct(productDTO);
		verify(productRepository, times(1)).save(productDTO);
	}
}
