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

import br.com.zup.estrelas.sistemaprefeitura.dto.FuncionarioDto;
import br.com.zup.estrelas.sistemaprefeitura.dto.MensagemDto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Funcionario;
import br.com.zup.estrelas.sistemaprefeitura.service.IFuncionarioService;

@RestController
@RequestMapping("/funcionarios")
//TODO: Aqui cabe o mesmo comentário dos projetos.
public class FuncionarioController {

	@Autowired
	IFuncionarioService funcionarioService;
	
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public MensagemDto adicionaFuncionario(@RequestBody FuncionarioDto funcionario) {
		return funcionarioService.adicionaFuncionario(funcionario);
	}
	
	@GetMapping(path = "/{idFuncionario}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Funcionario buscaFuncionario(@PathVariable Long idFuncionario) {
		return funcionarioService.buscaFuncionario(idFuncionario);
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Funcionario> listaFuncionarios() {
		return funcionarioService.listaFuncionarios();
	}
	
	@DeleteMapping(path = "/{idFuncionario}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDto removeFuncionario(@PathVariable Long idFuncionario) {
		return funcionarioService.removeFuncionario(idFuncionario);
	}
	
	@PutMapping(path = "/{idFuncionario}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDto alteraFuncionario(@PathVariable Long idFuncionario, @RequestBody FuncionarioDto funcionario){
		return funcionarioService.alteraFuncionario(idFuncionario, funcionario);
	}
}
