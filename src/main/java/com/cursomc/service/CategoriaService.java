package com.cursomc.service;

import com.cursomc.domain.Categoria;

public interface CategoriaService {

	public Categoria find(Integer id);

	public Categoria insert(Categoria categoria);

	public Categoria update(Categoria categoria);
	
}
