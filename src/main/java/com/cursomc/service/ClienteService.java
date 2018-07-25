package com.cursomc.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.cursomc.domain.Cliente;
import com.cursomc.dto.ClienteDTO;

public interface ClienteService {
	
	public Cliente find(Integer id);
	
	public Cliente update(Cliente cliente);

	public void delete(Integer id);

	public List<ClienteDTO> findAll();

	public Page<ClienteDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction);

	public Cliente fromDTO(ClienteDTO clienteDTO);

}
