package com.cursomc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Pagamento;
import com.cursomc.repositories.PagamentoRepository;
import com.cursomc.service.PagamentoService;

@Service
public class PagamentoServiceImpl implements PagamentoService {

	private PagamentoRepository pagamentoRepository;

	@Autowired
	public PagamentoServiceImpl(PagamentoRepository pagamentoRepository) {
		this.pagamentoRepository = pagamentoRepository;
	}

	@Override
	public void save(Pagamento pagamento) {
		pagamentoRepository.save(pagamento);
	}

}
