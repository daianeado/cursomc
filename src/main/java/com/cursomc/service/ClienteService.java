package com.cursomc.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.data.domain.Page;

import com.cursomc.domain.Cliente;
import com.cursomc.dto.ClienteDTO;
import com.cursomc.dto.ClienteNewDTO;
import com.cursomc.relatorios.ExportacaoException;

public interface ClienteService {
	
	public Cliente find(Integer id);
	
	public Cliente insert(Cliente cliente);
	
	public Cliente update(Cliente cliente);

	public void delete(Integer id);

	public List<ClienteDTO> findAll();

	public Page<ClienteDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction);

	public Cliente fromDTO(ClienteDTO clienteDTO);
	
	public Cliente fromDTO(ClienteNewDTO clientenewDTO);
	
	public Cliente findByEmail(String email);
	
	public ByteArrayOutputStream exportClient(String tipo) throws ExportacaoException;
	
	public Page<Cliente> findAllForReport(Integer page, Integer linesPerPage, String orderBy, String direction);

}
