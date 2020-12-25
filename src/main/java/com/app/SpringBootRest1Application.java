package com.app;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.app.entity.ProductDTO;
import com.app.repo.ProductRepository;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class SpringBootRest1Application {
	@Autowired
	private ProductRepository productRepository;

	private static final Logger log = LoggerFactory.getLogger(SpringBootRest1Application.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRest1Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			List<ProductDTO> productList = Arrays.asList(
					new ProductDTO("P001", "Redmi Note5 Pro", "Electronics", 16000.0, 6.0, 8.0, 10),
					new ProductDTO("P002", "Redmi Note6 Pro", "Electronics", 16000.0, 6.0, 8.0, 10),
					new ProductDTO("P003", "Men's Shirt", "cloth", 600.0, 6.0, 8.0, 20),
					new ProductDTO("P004", "Women's jeans", "Cloth", 1200.0, 6.0, 8.0, 30));

			productRepository.saveAll(productList);
			log.info("Products Records Saved Successfully!");
		};
	}
}
