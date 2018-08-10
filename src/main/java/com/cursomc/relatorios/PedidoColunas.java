package com.cursomc.relatorios;

import java.util.ArrayList;
import java.util.List;

public class PedidoColunas {
	
	private final List<ColunasPropriedadeRelatorio> parametros = new ArrayList<>();
	
	private static final int INSTANTE = 4;
	private static final int ID = 4;
	private static final int CLIENTE = 4;
	private static final int ENDERECO = 4;
	
	{
		parametros.add(new ColunasPropriedadeRelatorio("id", "Nº", Integer.class, ID, RelatorioConstantes.ALINHAR_ESQUERDA));
		parametros.add(new ColunasPropriedadeRelatorio("instante", "Data", String.class, INSTANTE, RelatorioConstantes.ALINHAR_ESQUERDA));
		parametros.add(new ColunasPropriedadeRelatorio("cliente.nome", "Cliente", String.class, CLIENTE, RelatorioConstantes.ALINHAR_ESQUERDA));
		parametros.add(new ColunasPropriedadeRelatorio("enderecoDeEntrega.logradouro", "Endereco", String.class, ENDERECO, RelatorioConstantes.ALINHAR_ESQUERDA));
	}

	public List<ColunasPropriedadeRelatorio> getParametros() {
		return parametros;
	}
}
