package com.cursomc.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Cliente;
import com.cursomc.service.AuthService;
import com.cursomc.service.ClienteService;
import com.cursomc.service.EmailService;
import com.cursomc.service.exceptions.ObjectNotFoundException;

@Service
public class AuthServiceImpl implements AuthService {

	private ClienteService clienteService;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private EmailService emailService;

	private Random rand = new Random();

	@Autowired
	public AuthServiceImpl(ClienteService clienteService, BCryptPasswordEncoder bCryptPasswordEncoder,
			EmailService emailService) {
		this.clienteService = clienteService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.emailService = emailService;
	}

	@Override
	public void sendNewPassword(String email) {
		Cliente cliente = clienteService.findByEmail(email);
		if (cliente == null) {
			throw new ObjectNotFoundException("E-mail não encontrado");
		}

		String newPass = newPassword();
		cliente.setSenha(bCryptPasswordEncoder.encode(newPass));
		clienteService.update(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for (int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);
		switch (opt) {
		case 0:
			// gerar numero
			return (char) (rand.nextInt(10) + 48);
		case 1:
			// gerar letra maiúscula
			return (char) (rand.nextInt(26) + 65);
		case 2:
			// gerar letra minúscula
			return (char) (rand.nextInt(26) + 97);
		}
		return 0;
	}

}
