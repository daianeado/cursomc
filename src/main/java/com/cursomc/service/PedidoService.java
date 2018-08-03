package com.cursomc.service;

import org.springframework.data.domain.Page;

import com.cursomc.domain.Pedido;

public interface PedidoService {

	public Pedido find(Integer id);
	
	public Pedido insert(Pedido pedido);
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction);
}
