package com.cursomc.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.cursomc.domain.PagamentoComBoleto;
import com.cursomc.service.BoletoService;

@Service
public class BoletoServiceImpl implements BoletoService{

	@Override
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataVencimento(cal.getTime());
	}

}
