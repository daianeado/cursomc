package com.cursomc.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Pedido;
import com.cursomc.repositories.PedidoRepository;
import com.cursomc.service.PedidoService;
import com.cursomc.service.exceptions.ObjectNotFoundException;

@Service
public class PedidoServiceImpl implements PedidoService {

	private PedidoRepository pedidoRepository;

	@Autowired
	public PedidoServiceImpl(PedidoRepository pedidoRepository) {
		this.pedidoRepository = pedidoRepository;
	}

	@Override
	public Pedido find(Integer id) {
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		return pedido.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	@Override
	public Pedido insert(Pedido pedido) {
		// TODO Auto-generated method stub
		return null;
	}

}
