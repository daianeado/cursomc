package com.cursomc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Cliente;
import com.cursomc.security.UserSS;
import com.cursomc.service.ClienteService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private ClienteService clienteService;

	@Autowired
	public UserDetailsServiceImpl(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Cliente cli = clienteService.findByEmail(email);
		if (cli == null) {
			throw new UsernameNotFoundException(email);
		}

		return new UserSS(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfis());
	}

}
