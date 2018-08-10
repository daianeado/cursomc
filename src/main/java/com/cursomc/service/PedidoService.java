package com.cursomc.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.data.domain.Page;

import com.cursomc.domain.Pedido;
import com.cursomc.dto.PedidoReportDTO;
import com.cursomc.relatorios.ExportacaoException;

public interface PedidoService {

	public Pedido find(Integer id);
	
	public Pedido insert(Pedido pedido);
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction);

	public List<PedidoReportDTO> findAll();

	public ByteArrayOutputStream exportClient(String pdf) throws ExportacaoException;
}
