package com.cursomc.service;

import com.cursomc.domain.Pedido;

public interface PedidoService {

	public Pedido find(Integer id);
	
	public Pedido insert(Pedido pedido);
}
