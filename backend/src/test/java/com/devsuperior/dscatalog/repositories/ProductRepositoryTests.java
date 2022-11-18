package com.devsuperior.dscatalog.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;

	private long nonExistingId;
	private long countTotalProducts;
	private long existingId;

	@BeforeEach
	void setUp() throws Exception {
		nonExistingId = 303L;
		countTotalProducts = 25L;
		existingId = 1L;
	}

	// Nomenclatura de um teste: <AÇÃO> should <EFEITO> [when <CENÁRIO>]

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {

		// Padrão AAA

		// Arrange: instancie os objetos necessários
		// long existingId = 1L;

		// Act: execute as ações necessárias
		repository.deleteById(existingId);
		Optional<Product> result = repository.findById(existingId);

		// Assert: declare o que deveria acontecer (resultado esperado)
		assertFalse(result.isPresent());
	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdNotExists() {

		assertThrows(EmptyResultDataAccessException.class, () -> {

			repository.deleteById(nonExistingId);
		});
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		assertNotNull(product.getId());
		assertTrue(countTotalProducts + 1 == product.getId());
	}
	
	@Test
	public void findByIdShouldReturnOptionalNotEmptyWhenIdExists() {
		
		Optional<Product> result = repository.findById(existingId);
		
		assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnOptionalEmptyWhenIdNotExists() {
		
		Optional<Product> result = repository.findById(nonExistingId);
		
		assertTrue(result.isEmpty());
	}
}
