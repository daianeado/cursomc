package com.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cursomc.domain.Cidade;
import com.cursomc.service.CidadeService;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeResource {
	
	private CidadeService cidadeService;

	@Autowired
	public CidadeResource(CidadeService cidadeService) {
		this.cidadeService = cidadeService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Cidade>> findAll() {
		List<Cidade> listCidade = cidadeService.findAll();
		return ResponseEntity.ok().body(listCidade);
	}

}
