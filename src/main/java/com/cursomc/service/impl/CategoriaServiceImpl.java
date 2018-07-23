package com.cursomc.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Categoria;
import com.cursomc.repositories.CategoriaRepository;
import com.cursomc.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService{

	private CategoriaRepository categoriaRepository;
	
	@Autowired
	public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}
	
	@Override
	public Categoria buscar(Integer id) {
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		return categoria.orElse(null);
	}

}
