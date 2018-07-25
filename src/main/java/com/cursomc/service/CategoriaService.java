package com.cursomc.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.cursomc.domain.Categoria;
import com.cursomc.dto.CategoriaDTO;

public interface CategoriaService {

	public Categoria find(Integer id);

	public Categoria insert(Categoria categoria);

	public Categoria update(Categoria categoria);

	public void delete(Integer id);

	public List<CategoriaDTO> findAll();

	public Page<CategoriaDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction);

	public Categoria fromDTO(CategoriaDTO categoriaDTO);
}
