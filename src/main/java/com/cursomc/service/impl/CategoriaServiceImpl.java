package com.cursomc.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Categoria;
import com.cursomc.dto.CategoriaDTO;
import com.cursomc.relatorios.CategoriaColunas;
import com.cursomc.relatorios.ColunasPropriedadeRelatorio;
import com.cursomc.relatorios.DynamicReportsUtil;
import com.cursomc.relatorios.ExportacaoException;
import com.cursomc.repositories.CategoriaRepository;
import com.cursomc.service.CategoriaService;
import com.cursomc.service.exceptions.DataIntegrityException;
import com.cursomc.service.exceptions.ObjectNotFoundException;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

@Service
public class CategoriaServiceImpl implements CategoriaService {

	private CategoriaRepository categoriaRepository;

	@Autowired
	public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}

	@Override
	public Categoria find(Integer id) {
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		return categoria.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	@Override
	public Categoria insert(Categoria categoria) {
		categoria.setId(null);
		return categoriaRepository.save(categoria);
	}

	@Override
	public Categoria update(Categoria categoria) {
		Categoria novaCategoria = find(categoria.getId());
		updateData(novaCategoria, categoria);
		return categoriaRepository.save(categoria);
	}

	@Override
	public void delete(Integer id) {
		find(id);
		try {
			categoriaRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos.");
		}
	}

	@Override
	public List<CategoriaDTO> findAll() {
		return categoriaRepository.findAll().stream().map(categoria -> new CategoriaDTO(categoria))
				.collect(Collectors.toList());
	}

	@Override
	public Page<CategoriaDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return categoriaRepository.findAll(pageRequest).map(categoria -> new CategoriaDTO(categoria));
	}

	@Override
	public Categoria fromDTO(CategoriaDTO categoriaDTO) {
		return new Categoria(categoriaDTO.getId(), categoriaDTO.getNome());
	}

	private void updateData(Categoria novaCategoria, Categoria categoria) {
		novaCategoria.setNome(categoria.getNome());
	}

	@Override
	public ByteArrayOutputStream exportClient(String tipo) throws ExportacaoException {
		Page<CategoriaDTO> result = findAllForReport(0, 24, "id", "ASC").map(categoria -> new CategoriaDTO(categoria));
		return export(result, "Lista de Categorias", new CategoriaColunas().getParametros(), tipo);
	}

	private ByteArrayOutputStream export(Page<CategoriaDTO> page, String titulo, List<ColunasPropriedadeRelatorio> propriedades,
			String tipo) throws ExportacaoException {
		JasperReportBuilder dr = DynamicReportsUtil.getExportacao(titulo, propriedades, null, tipo);
		return DynamicReportsUtil.getReportStream(dr, page.getContent(), tipo);
	}
	
	private Page<Categoria> findAllForReport(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return categoriaRepository.findAll(pageRequest);
	}

}
