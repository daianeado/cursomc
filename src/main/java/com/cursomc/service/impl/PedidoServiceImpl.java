package com.cursomc.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cursomc.domain.ItemPedido;
import com.cursomc.domain.PagamentoComBoleto;
import com.cursomc.domain.Pedido;
import com.cursomc.domain.enums.EstadoPagamento;
import com.cursomc.repositories.PedidoRepository;
import com.cursomc.service.BoletoService;
import com.cursomc.service.ClienteService;
import com.cursomc.service.EmailService;
import com.cursomc.service.ItemPedidoService;
import com.cursomc.service.PagamentoService;
import com.cursomc.service.PedidoService;
import com.cursomc.service.ProdutoService;
import com.cursomc.service.exceptions.ObjectNotFoundException;

@Service
public class PedidoServiceImpl implements PedidoService {

	private PedidoRepository pedidoRepository;

	private BoletoService boletoService;

	private PagamentoService pagamentoService;

	private ProdutoService produtoService;

	private ItemPedidoService itemPedidoService;

	private ClienteService clienteService;

	private EmailService emailService;

	@Autowired
	public PedidoServiceImpl(PedidoRepository pedidoRepository, BoletoService boletoService,
			PagamentoService pagamentoService, ProdutoService produtoService, ItemPedidoService itemPedidoService,
			ClienteService clienteService, EmailService emailService) {
		this.pedidoRepository = pedidoRepository;
		this.boletoService = boletoService;
		this.pagamentoService = pagamentoService;
		this.produtoService = produtoService;
		this.itemPedidoService = itemPedidoService;
		this.clienteService = clienteService;
		this.emailService = emailService;
	}

	@Override
	public Pedido find(Integer id) {
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		return pedido.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	@Transactional
	@Override
	public Pedido insert(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		if (pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
		}
		pedido = pedidoRepository.save(pedido);
		pagamentoService.save(pedido.getPagamento());

		for (ItemPedido ip : pedido.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(pedido);
		}

		itemPedidoService.save(pedido.getItens());
		emailService.sendOrderConfirmationHtmlEmail(pedido);
		return pedido;
	}

}
