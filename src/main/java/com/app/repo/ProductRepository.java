package com.app.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.ProductDTO;

@Repository
public interface ProductRepository extends RevisionRepository<ProductDTO, Long, Integer>, JpaRepository<ProductDTO, Long> {
	Optional<ProductDTO> findByProductCode(String productCode);
}
