package com.cursomc.service;

import java.util.List;

import com.cursomc.domain.Categoria;
import com.cursomc.dto.CategoriaDTO;

public interface CategoriaService {

	public Categoria find(Integer id);

	public Categoria insert(Categoria categoria);

	public Categoria update(Categoria categoria);

	public void delete(Integer id);

	public List<CategoriaDTO> findAll();
	
}
