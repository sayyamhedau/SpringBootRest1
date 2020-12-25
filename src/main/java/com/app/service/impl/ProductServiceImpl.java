package com.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.ProductDTO;
import com.app.repo.ProductRepository;
import com.app.service.IProductService;

@Service
public class ProductServiceImpl implements IProductService {

	@Autowired
	private ProductRepository productRepo;

	@Override
	public Long saveProduct(ProductDTO product) {
		return productRepo.save(product).getId();
	}

	@Override
	public void updateProduct(ProductDTO product) {
		productRepo.save(product);
	}

	@Override
	public void deleteProduct(Long id) {
		productRepo.deleteById(id);
	}

	@Override
	public Optional<ProductDTO> getProduct(Long id) {
		return productRepo.findById(id);
	}

	@Override
	public List<ProductDTO> getAllProducts() {
		return productRepo.findAll();
	}

	@Override
	public boolean isProductExistByProductCode(String productCode) {
		Optional<ProductDTO> optionalProduct = productRepo.findByProductCode(productCode);
		return optionalProduct.isPresent();
	}

	@Override
	public boolean isProductExistByProductId(Long id) {
		return productRepo.findById(id).isPresent();
	}

}
