package com.cursomc.resources;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cursomc.domain.Cliente;
import com.cursomc.dto.ClienteDTO;
import com.cursomc.dto.ClienteNewDTO;
import com.cursomc.relatorios.ExportacaoException;
import com.cursomc.relatorios.GenerateReportsUtil;
import com.cursomc.relatorios.RelatorioConstantes;
import com.cursomc.service.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	private final Logger log = LoggerFactory.getLogger(ClienteResource.class);

	private ClienteService clienteService;

	@Autowired
	public ClienteResource(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		Cliente cliente = clienteService.find(id);
		return ResponseEntity.ok().body(cliente);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO clienteNewDTO) {
		Cliente cliente = clienteService.fromDTO(clienteNewDTO);
		cliente = clienteService.insert(cliente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable Integer id) {
		Cliente categoria = clienteService.fromDTO(clienteDTO);
		categoria.setId(id);
		categoria = clienteService.update(categoria);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		clienteService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll() {
		List<ClienteDTO> listCliente = clienteService.findAll();
		return ResponseEntity.ok().body(listCliente);
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<ClienteDTO> listCliente = clienteService.findPage(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(listCliente);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> getPDF() throws ExportacaoException {
		log.debug("REST request to get Clientes pdf : {}");
		ByteArrayOutputStream byteArrayOutputStream = this.clienteService.exportClient(RelatorioConstantes.PDF);
		return GenerateReportsUtil.output(byteArrayOutputStream, "clientes.pdf");
	}
}
