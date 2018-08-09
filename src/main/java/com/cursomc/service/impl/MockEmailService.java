package com.cursomc.service.impl;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import com.cursomc.domain.Cliente;

public class MockEmailService extends AbstractEmailServiceImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(MockEmailService.class);

	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOGGER.info("Simulando envio de e-mail.");
		LOGGER.info(msg.toString());
		LOGGER.info("E-mail enviado");

	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOGGER.info("Simulando envio de e-mail HTML.");
		LOGGER.info(msg.toString());
		LOGGER.info("E-mail enviado");
	}

	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		// TODO Auto-generated method stub
		
	}

}
