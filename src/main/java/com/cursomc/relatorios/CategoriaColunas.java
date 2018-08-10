package com.cursomc.relatorios;

import java.util.ArrayList;
import java.util.List;

public class CategoriaColunas {
	
	private final List<ColunasPropriedadeRelatorio> parametros = new ArrayList<>();
	
	private static final int NOME = 4;
	private static final int ID = 4;
	{
		parametros.add(new ColunasPropriedadeRelatorio("id", "Nº", Integer.class, ID, RelatorioConstantes.ALINHAR_ESQUERDA));
		parametros.add(new ColunasPropriedadeRelatorio("nome", "Nome", String.class, NOME, RelatorioConstantes.ALINHAR_ESQUERDA));
	}

	public List<ColunasPropriedadeRelatorio> getParametros() {
		return parametros;
	}
}
