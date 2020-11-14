package br.com.zup.estrelas.sistemaprefeitura.service;

import java.time.LocalDate;
import java.util.List;

import br.com.zup.estrelas.sistemaprefeitura.dto.MensagemDto;
import br.com.zup.estrelas.sistemaprefeitura.dto.ProjetoDto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Projeto;

public interface IProjetoService {

	public MensagemDto adicionaProjeto(ProjetoDto projeto);
	
	public Projeto buscaProjeto(Long idProjeto);
	
	public List<Projeto> listaProjetos();
	
	public MensagemDto alteraProjeto(Long idProjeto, ProjetoDto projeto);
	
	public MensagemDto concluiProjeto(Long idProjeto, LocalDate dataEntrega);
}
