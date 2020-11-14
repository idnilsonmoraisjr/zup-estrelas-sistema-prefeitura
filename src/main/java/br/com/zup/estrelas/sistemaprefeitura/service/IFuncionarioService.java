package br.com.zup.estrelas.sistemaprefeitura.service;

import java.util.List;

import br.com.zup.estrelas.sistemaprefeitura.dto.FuncionarioDto;
import br.com.zup.estrelas.sistemaprefeitura.dto.MensagemDto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Funcionario;
import br.com.zup.estrelas.sistemaprefeitura.entity.Secretaria;

public interface IFuncionarioService {
	
	public MensagemDto adicionaFuncionario(FuncionarioDto funcionario);
	
	public Funcionario buscaFuncionario(Long idFuncionario);
	
	public List<Funcionario> listaFuncionarios();
	
	public MensagemDto removeFuncionario(Long idFuncionario);
	
	public MensagemDto alteraFuncionario(Long idFuncionario, FuncionarioDto funcionario);
	
}
