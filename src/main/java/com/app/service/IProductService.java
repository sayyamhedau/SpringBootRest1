package com.app.service;

import java.util.List;
import java.util.Optional;

import com.app.entity.ProductDTO;

public interface IProductService {
	public Long saveProduct(ProductDTO product);

	public void updateProduct(ProductDTO product);

	public void deleteProduct(Long id);

	public Optional<ProductDTO> getProduct(Long id);

	public List<ProductDTO> getAllProducts();

	public boolean isProductExistByProductCode(String productCode);

	public boolean isProductExistByProductId(Long id);

}
