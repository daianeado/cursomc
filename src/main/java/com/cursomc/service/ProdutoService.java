package com.cursomc.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.data.domain.Page;

import com.cursomc.domain.Produto;
import com.cursomc.dto.ProdutoDTO;
import com.cursomc.relatorios.ExportacaoException;

public interface ProdutoService {

	public Produto find(Integer id);

	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy,
			String direction);

	public List<ProdutoDTO> findAll();

	public ByteArrayOutputStream exportClient(String pdf) throws ExportacaoException;
}
