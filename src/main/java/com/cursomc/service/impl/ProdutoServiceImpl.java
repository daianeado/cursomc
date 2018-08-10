package com.cursomc.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Categoria;
import com.cursomc.domain.Produto;
import com.cursomc.dto.ProdutoDTO;
import com.cursomc.relatorios.ColunasPropriedadeRelatorio;
import com.cursomc.relatorios.DynamicReportsUtil;
import com.cursomc.relatorios.ExportacaoException;
import com.cursomc.relatorios.ProdutoColunas;
import com.cursomc.repositories.CategoriaRepository;
import com.cursomc.repositories.ProdutoRepository;
import com.cursomc.service.ProdutoService;
import com.cursomc.service.exceptions.ObjectNotFoundException;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

@Service
public class ProdutoServiceImpl implements ProdutoService {

	@Autowired
	private ProdutoRepository repo;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Override
	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}

	@Override
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);

		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}

	@Override
	public List<ProdutoDTO> findAll() {
		return repo.findAll().stream().map(produto -> new ProdutoDTO(produto)).collect(Collectors.toList());
	}

	@Override
	public ByteArrayOutputStream exportClient(String tipo) throws ExportacaoException {
		Page<ProdutoDTO> result = findAllForReport(0, 24, "id", "ASC").map(produto -> new ProdutoDTO(produto));
		return export(result, "Lista de Produtos", new ProdutoColunas().getParametros(), tipo);
	}

	private ByteArrayOutputStream export(Page<ProdutoDTO> page, String titulo, List<ColunasPropriedadeRelatorio> propriedades,
			String tipo) throws ExportacaoException {
		JasperReportBuilder dr = DynamicReportsUtil.getExportacao(titulo, propriedades, null, tipo);
		return DynamicReportsUtil.getReportStream(dr, page.getContent(), tipo);
	}
	
	private Page<Produto> findAllForReport(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
}