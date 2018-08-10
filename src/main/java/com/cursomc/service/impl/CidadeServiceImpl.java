package com.cursomc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Cidade;
import com.cursomc.repositories.CidadeRepository;
import com.cursomc.service.CidadeService;

@Service
public class CidadeServiceImpl implements CidadeService {

	private CidadeRepository cidadeRepository;

	@Autowired
	public CidadeServiceImpl(CidadeRepository cidadeRepository) {
		this.cidadeRepository = cidadeRepository;
	}

	@Override
	public List<Cidade> findAll() {
		return cidadeRepository.findAll();
	}

}
