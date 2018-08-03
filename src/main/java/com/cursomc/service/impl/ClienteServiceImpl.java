package com.cursomc.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Cidade;
import com.cursomc.domain.Cliente;
import com.cursomc.domain.Endereco;
import com.cursomc.domain.enums.Perfil;
import com.cursomc.domain.enums.TipoCliente;
import com.cursomc.dto.ClienteDTO;
import com.cursomc.dto.ClienteNewDTO;
import com.cursomc.repositories.CidadeRepository;
import com.cursomc.repositories.ClienteRepository;
import com.cursomc.repositories.EnderecoRepository;
import com.cursomc.security.UserSS;
import com.cursomc.service.ClienteService;
import com.cursomc.service.exceptions.AuthorizationException;
import com.cursomc.service.exceptions.DataIntegrityException;
import com.cursomc.service.exceptions.ObjectNotFoundException;

@Service
public class ClienteServiceImpl implements ClienteService {

	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private ClienteRepository clienteRepository;
	private CidadeRepository cidadeRepository;
	private EnderecoRepository enderecoRepository;

	@Autowired
	public ClienteServiceImpl(ClienteRepository clienteRepository, CidadeRepository cidadeRepository,
			EnderecoRepository enderecoRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.clienteRepository = clienteRepository;
		this.cidadeRepository = cidadeRepository;
		this.enderecoRepository = enderecoRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public Cliente find(Integer id) {
		UserSS user = UserServiceImpl.authenticated();
		
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	@Override
	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		cliente = clienteRepository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
	}

	@Override
	public Cliente update(Cliente cliente) {
		Cliente novoCliente = find(cliente.getId());
		updateData(novoCliente, cliente);
		return clienteRepository.save(cliente);
	}

	@Override
	public void delete(Integer id) {
		find(id);
		try {
			clienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionadas.");
		}
	}

	@Override
	public List<ClienteDTO> findAll() {
		return clienteRepository.findAll().stream().map(cliente -> new ClienteDTO(cliente))
				.collect(Collectors.toList());
	}

	@Override
	public Page<ClienteDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest).map(cliente -> new ClienteDTO(cliente));
	}

	@Override
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null, null);
	}

	@Override
	public Cliente fromDTO(ClienteNewDTO clienteNewDTO) {
		Cliente cli = new Cliente(null, clienteNewDTO.getNome(), clienteNewDTO.getEmail(), clienteNewDTO.getCpfOuCnpj(),
				TipoCliente.toEnum(clienteNewDTO.getTipo()),bCryptPasswordEncoder.encode(clienteNewDTO.getSenha()));

		Optional<Cidade> cidade = cidadeRepository.findById(clienteNewDTO.getCidadeId());

		Endereco end = new Endereco(null, clienteNewDTO.getLogradouro(), clienteNewDTO.getNumero(),
				clienteNewDTO.getComplemento(), clienteNewDTO.getBairro(), clienteNewDTO.getCep(), cli, cidade.get());
		cli.getEnderecos().add(end);
		cli.getTelefones().add(clienteNewDTO.getTelefone1());
		if (clienteNewDTO.getTelefone2() != null) {
			cli.getTelefones().add(clienteNewDTO.getTelefone2());
		}
		if (clienteNewDTO.getTelefone3() != null) {
			cli.getTelefones().add(clienteNewDTO.getTelefone3());
		}

		return cli;
	}

	private void updateData(Cliente novoCliente, Cliente cliente) {
		novoCliente.setNome(cliente.getNome());
		novoCliente.setEmail(cliente.getEmail());
	}

	@Override
	public Cliente findByEmail(String email) {
		return clienteRepository.findByEmail(email);
	}
}
