package com.cursomc.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Cliente;
import com.cursomc.repositories.ClienteRepository;
import com.cursomc.service.ClienteService;
import com.cursomc.service.exceptions.ObjectNotFoundException;

@Service
public class ClienteServiceImpl implements ClienteService {

	private ClienteRepository clienteRepository;

	@Autowired
	public ClienteServiceImpl(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	@Override
	public Cliente find(Integer id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

}
