package com.cursomc.relatorios;

public class ColunasPropriedadeRelatorio {
	private final String descricaoColuna;
	private final Class<?> tipo;
	private final int largura;
	private final String estilo;
	private String nomePropriedade;

	public ColunasPropriedadeRelatorio(String nomePropriedade, String descricaoColuna, Class<?> tipo, int largura,
			String estilo) {
		this.nomePropriedade = nomePropriedade;
		this.descricaoColuna = descricaoColuna;
		this.tipo = tipo;
		this.largura = largura;
		this.estilo = estilo;
	}

	String getNomePropriedade() {
		return nomePropriedade;
	}

	public String getDescricaoColuna() {
		return descricaoColuna;
	}

	public Class<?> getTipo() {
		return tipo;
	}

	int getLargura() {
		return largura;
	}

	String getEstilo() {
		return estilo;
	}
}
