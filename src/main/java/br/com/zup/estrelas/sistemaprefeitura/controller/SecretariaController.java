package br.com.zup.estrelas.sistemaprefeitura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.zup.estrelas.sistemaprefeitura.dto.MensagemDto;
import br.com.zup.estrelas.sistemaprefeitura.dto.SecretariaDto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Secretaria;
import br.com.zup.estrelas.sistemaprefeitura.service.ISecretariaService;

@RestController
@RequestMapping("/secretarias")
public class SecretariaController {

	@Autowired
	ISecretariaService secretariaService;
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDto adicionaSecretaria(@RequestBody SecretariaDto secretaria) {
		return secretariaService.adicionaSecretaria(secretaria);
	}
	
	@GetMapping(path = "/{idSecretaria}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Secretaria buscaSecretaria(@PathVariable Long idSecretaria) {
		return secretariaService.buscaSecretaria(idSecretaria);
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Secretaria> listaSecretarias() {
		return secretariaService.listaSecretarias();
	}
	
	@DeleteMapping(path = "/{idSecretaria}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDto removeSecretaria (@PathVariable Long idSecretaria) {
		return secretariaService.removeSecretaria(idSecretaria);
	}
	
	@PutMapping(path = "/{idSecretaria}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDto alteraSecretaria (@PathVariable Long idSecretaria, @RequestBody SecretariaDto secretaria) {
		return secretariaService.alteraSecretaria(idSecretaria, secretaria);
	}
}
