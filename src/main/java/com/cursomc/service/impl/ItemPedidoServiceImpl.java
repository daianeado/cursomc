package com.cursomc.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursomc.domain.ItemPedido;
import com.cursomc.repositories.ItemPedidoRepository;
import com.cursomc.service.ItemPedidoService;

@Service
public class ItemPedidoServiceImpl implements ItemPedidoService {

	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	public ItemPedidoServiceImpl(ItemPedidoRepository itemPedidoRepository) {
		this.itemPedidoRepository = itemPedidoRepository;
	}

	@Override
	public void save(Set<ItemPedido> itens) {
		itemPedidoRepository.saveAll(itens);
	}

}
