package com.cursomc.service.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.cursomc.domain.Cliente;
import com.cursomc.domain.enums.TipoCliente;
import com.cursomc.dto.ClienteNewDTO;
import com.cursomc.repositories.ClienteRepository;
import com.cursomc.resources.exception.FieldMessage;
import com.cursomc.service.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

	private ClienteRepository clienteRepository;

	public ClienteInsertValidator() {
	}

	@Autowired
	public ClienteInsertValidator(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	@Override
	public void initialize(ClienteInsert ann) {

	}

	@Override
	public boolean isValid(ClienteNewDTO clienteNewDTO, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		if (clienteNewDTO.getTipo().equals(TipoCliente.PESSOA_FISICA.getCod())
				&& !BR.isValidCPF(clienteNewDTO.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}

		if (clienteNewDTO.getTipo().equals(TipoCliente.PESSOA_JURIDICA.getCod())
				&& !BR.isValidCNPJ(clienteNewDTO.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}

		Cliente cliente = clienteRepository.findByEmail(clienteNewDTO.getEmail());

		if (cliente != null) {
			list.add(new FieldMessage("email", "E-mail já existente"));
		}

		// inclua os testes aqui, inserindo erros na lista
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}

}
