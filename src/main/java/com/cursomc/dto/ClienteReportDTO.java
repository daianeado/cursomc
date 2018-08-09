package com.cursomc.dto;

import java.io.Serializable;

import com.cursomc.domain.Cliente;
import com.cursomc.domain.enums.TipoCliente;

public class ClienteReportDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nome;
	private String email;
	private String cpfCnpjFormatado;
	private String tipoCliente;

	public ClienteReportDTO() {
	}

	public ClienteReportDTO(Cliente cliente) {
		this.nome = cliente.getNome();
		this.email = cliente.getEmail();
		this.cpfCnpjFormatado = formataCpfCnpj(cliente);
		this.tipoCliente = (cliente.getTipoCliente() == null) ? null : cliente.getTipoCliente().descricao();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpfCnpjFormatado() {
		return cpfCnpjFormatado;
	}

	public void setCpfCnpjFormatado(String cpfCnpjFormatado) {
		this.cpfCnpjFormatado = cpfCnpjFormatado;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	private String formataCpfCnpj(Cliente cliente) {
		String result = "";
		if (TipoCliente.PESSOA_FISICA.equals(cliente.getTipoCliente())) {
			result = cliente.getCpfCjnj().substring(0, 3) + "." + cliente.getCpfCjnj().substring(3, 6) + "."
					+ cliente.getCpfCjnj().substring(6, 9) + "-" + cliente.getCpfCjnj().substring(9, 11);
		} else if (TipoCliente.PESSOA_JURIDICA.equals(cliente.getTipoCliente())) {
			result = cliente.getCpfCjnj().substring(0, 2) + "." + cliente.getCpfCjnj().substring(2, 5) + "."
					+ cliente.getCpfCjnj().substring(5, 8) + "/" + cliente.getCpfCjnj().substring(8, 12) + "-"
					+ cliente.getCpfCjnj().substring(12, 14);
		}
		return result;
	}

}
