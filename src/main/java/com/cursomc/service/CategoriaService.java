package com.cursomc.service;

import com.cursomc.domain.Categoria;

import javassist.tools.rmi.ObjectNotFoundException;

public interface CategoriaService {

	public Categoria buscar(Integer id);

	public Categoria insert(Categoria categoria);
	
}
