package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();		
		
		List<CategoryDTO> listDTO = list.stream()
				.map(category -> new CategoryDTO(category))
				.collect(Collectors.toList());
		
		/* Algoritmo equivalente a funcao Lambda acima
		 * 
		List<CategoryDTO> listDTO = new ArrayList<>();
		for (Category c : list) {
			listDTO.add(new CategoryDTO(c));
		} */
		
		return listDTO;
	}
}
