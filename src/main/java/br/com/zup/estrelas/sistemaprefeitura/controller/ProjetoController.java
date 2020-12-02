package br.com.zup.estrelas.sistemaprefeitura.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.zup.estrelas.sistemaprefeitura.dto.MensagemDto;
import br.com.zup.estrelas.sistemaprefeitura.dto.ProjetoDto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Projeto;
import br.com.zup.estrelas.sistemaprefeitura.service.IProjetoService;

@RestController
@RequestMapping("/projetos")
//TODO: Idnilson, aqui eu consigo compreender por quê tratou os projetos
//como um recurso independente, mas dado que ele só pode pertencer à uma
//secretaria e isso não muda, seria interessante tratá-lo como um subrecurso
//de secretaria, seu endpoint seria algo como:
///secretarias/{id}/projetos. Dê uma olhada na referência do portal
//desenvolvimento pra web para entender melhor por quê isso faz mas sentido como um subrecurso
//e qualquer dúvida pode falar comigo.
public class ProjetoController {

	@Autowired
	IProjetoService projetoService;
	
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDto adicionaProjeto(@RequestBody ProjetoDto projeto) {
		return projetoService.adicionaProjeto(projeto);
	}
	
	@GetMapping(path = "/{idProjeto}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Projeto buscaProjeto(@PathVariable Long idProjeto) {
		return projetoService.buscaProjeto(idProjeto);
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Projeto> listaProjetos() {
		return projetoService.listaProjetos();
	}
	
	
	@PatchMapping(path = "/{idProjeto}", produces = {MediaType.APPLICATION_JSON_VALUE}) 
	public MensagemDto alteraProjeto(@PathVariable Long idProjeto, @RequestBody ProjetoDto projeto) {
		return projetoService.alteraProjeto(idProjeto, projeto);
	}
	
	@PutMapping(path = "/{idProjeto}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDto concluiProjeto(@PathVariable Long idProjeto, @RequestBody LocalDate dataEntrega) {
		return projetoService.concluiProjeto(idProjeto, dataEntrega);
	}
}
