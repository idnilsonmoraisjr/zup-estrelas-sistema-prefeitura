package br.com.zup.estrelas.sistemaprefeitura.service;

import java.util.List;

import br.com.zup.estrelas.sistemaprefeitura.dto.MensagemDto;
import br.com.zup.estrelas.sistemaprefeitura.dto.SecretariaDto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Secretaria;

public interface ISecretariaService {

	public MensagemDto adicionaSecretaria(SecretariaDto secretaria);
	
	public Secretaria buscaSecretaria(Long idSecretaria);

	public List<Secretaria> listaSecretarias();
	
	public MensagemDto removeSecretaria(Long idSecretaria);
	
	public MensagemDto alteraSecretaria(Long idSecretaria, SecretariaDto secretaria);
}
