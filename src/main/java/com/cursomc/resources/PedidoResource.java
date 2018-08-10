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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cursomc.domain.Pedido;
import com.cursomc.dto.PedidoReportDTO;
import com.cursomc.relatorios.ExportacaoException;
import com.cursomc.relatorios.GenerateReportsUtil;
import com.cursomc.relatorios.RelatorioConstantes;
import com.cursomc.service.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

	private final Logger log = LoggerFactory.getLogger(ClienteResource.class);

	private PedidoService pedidoService;

	@Autowired
	public PedidoResource(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id) {
		Pedido pedido = pedidoService.find(id);
		return ResponseEntity.ok().body(pedido);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido pedido) {
		pedido = pedidoService.insert(pedido);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pedido.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<Pedido>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "instante") String orderBy,
			@RequestParam(value = "direction", defaultValue = "DESC") String direction) {
		Page<Pedido> listPedido = pedidoService.findPage(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(listPedido);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<PedidoReportDTO>> findAll() {
		List<PedidoReportDTO> listPedido = pedidoService.findAll();
		return ResponseEntity.ok().body(listPedido);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> getPDF() throws ExportacaoException {
		log.debug("REST request to get Pedidos pdf : {}");
		ByteArrayOutputStream byteArrayOutputStream = this.pedidoService.exportClient(RelatorioConstantes.PDF);
		return GenerateReportsUtil.output(byteArrayOutputStream, "categorias.pdf");
	}
}
