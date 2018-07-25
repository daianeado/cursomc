package com.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cursomc.domain.Pedido;
import com.cursomc.service.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {
	private PedidoService pedidoService;

	@Autowired
	public PedidoResource(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id) {
		Pedido pedido = pedidoService.buscar(id);
		return ResponseEntity.ok().body(pedido);
	}
}
