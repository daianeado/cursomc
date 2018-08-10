package com.cursomc.dto;

import java.text.SimpleDateFormat;

import com.cursomc.domain.Cliente;
import com.cursomc.domain.Endereco;
import com.cursomc.domain.Pagamento;
import com.cursomc.domain.Pedido;

public class PedidoReportDTO {

	private Integer id;
	private String instante;
	private Pagamento pagamento;
	private Cliente cliente;
	private Endereco enderecoDeEntrega;

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public PedidoReportDTO() {

	}

	public PedidoReportDTO(Pedido pedido) {
		this.id = pedido.getId();
		this.instante = sdf.format(pedido.getInstante());
		this.pagamento = pedido.getPagamento();
		this.cliente = pedido.getCliente();
		this.enderecoDeEntrega = pedido.getEnderecoDeEntrega();

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInstante() {
		return instante;
	}

	public void setInstante(String instante) {
		this.instante = instante;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Endereco getEnderecoDeEntrega() {
		return enderecoDeEntrega;
	}

	public void setEnderecoDeEntrega(Endereco enderecoDeEntrega) {
		this.enderecoDeEntrega = enderecoDeEntrega;
	}

}
