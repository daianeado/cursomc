package com.cursomc.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cursomc.domain.Cliente;
import com.cursomc.domain.ItemPedido;
import com.cursomc.domain.PagamentoComBoleto;
import com.cursomc.domain.Pedido;
import com.cursomc.domain.enums.EstadoPagamento;
import com.cursomc.dto.PedidoReportDTO;
import com.cursomc.relatorios.ColunasPropriedadeRelatorio;
import com.cursomc.relatorios.DynamicReportsUtil;
import com.cursomc.relatorios.ExportacaoException;
import com.cursomc.relatorios.PedidoColunas;
import com.cursomc.repositories.PedidoRepository;
import com.cursomc.security.UserSS;
import com.cursomc.service.BoletoService;
import com.cursomc.service.ClienteService;
import com.cursomc.service.EmailService;
import com.cursomc.service.ItemPedidoService;
import com.cursomc.service.PagamentoService;
import com.cursomc.service.PedidoService;
import com.cursomc.service.ProdutoService;
import com.cursomc.service.exceptions.AuthorizationException;
import com.cursomc.service.exceptions.ObjectNotFoundException;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

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

	@Override
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

		UserSS user = UserServiceImpl.authenticated();

		if (user == null) {
			throw new AuthorizationException("Acesso negado!");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		return pedidoRepository.findByCliente(cliente, pageRequest);
	}

	@Override
	public List<PedidoReportDTO> findAll() {
		return pedidoRepository.findAll().stream().map(pedido -> new PedidoReportDTO(pedido))
				.collect(Collectors.toList());
	}

	@Override
	public ByteArrayOutputStream exportClient(String tipo) throws ExportacaoException {
		Page<PedidoReportDTO> result = findAllForReport(0, 24, "id", "ASC").map(pedido -> new PedidoReportDTO(pedido));
		return export(result, "Lista de Pedidos", new PedidoColunas().getParametros(), tipo);
	}

	private ByteArrayOutputStream export(Page<PedidoReportDTO> page, String titulo,
			List<ColunasPropriedadeRelatorio> propriedades, String tipo) throws ExportacaoException {
		JasperReportBuilder dr = DynamicReportsUtil.getExportacao(titulo, propriedades, null, tipo);
		return DynamicReportsUtil.getReportStream(dr, page.getContent(), tipo);
	}

	private Page<Pedido> findAllForReport(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return pedidoRepository.findAll(pageRequest);
	}

}
