package com.cursomc.service;

import java.util.Date;

import com.cursomc.domain.PagamentoComBoleto;

public interface BoletoService {

	void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instante);

}
