package com.cursomc.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.cursomc.domain.Produto;

public interface ProdutoService {

	public Produto find(Integer id);

	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy,
			String direction);
}
