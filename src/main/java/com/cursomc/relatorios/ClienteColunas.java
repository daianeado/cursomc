package com.cursomc.relatorios;

import java.util.ArrayList;
import java.util.List;

public class ClienteColunas {
	
	private final List<ColunasPropriedadeRelatorio> parametros = new ArrayList<>();
	
	private static final int NOME = 8;
	private static final int EMAIL = 8;
	private static final int CPF_CNPJ = 8;
	private static final int TIPO_CLIENTE = 8;
	
	{
		parametros.add(new ColunasPropriedadeRelatorio("nome", "Nome", String.class, NOME, RelatorioConstantes.ALINHAR_ESQUERDA));
		parametros.add(new ColunasPropriedadeRelatorio("email", "E-mail", String.class, EMAIL, RelatorioConstantes.ALINHAR_ESQUERDA));
		parametros.add(new ColunasPropriedadeRelatorio("tipoCliente", "Tipo de Cliente", String.class, TIPO_CLIENTE, RelatorioConstantes.ALINHAR_ESQUERDA));
		parametros.add(new ColunasPropriedadeRelatorio("cpfCnpjFormatado", "CPF/CNPJ", String.class, CPF_CNPJ, RelatorioConstantes.ALINHAR_ESQUERDA));
	}

	public List<ColunasPropriedadeRelatorio> getParametros() {
		return parametros;
	}
}
