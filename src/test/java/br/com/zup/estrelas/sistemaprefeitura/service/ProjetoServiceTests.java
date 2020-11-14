package br.com.zup.estrelas.sistemaprefeitura.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;


import br.com.zup.estrelas.sistemaprefeitura.dto.MensagemDto;
import br.com.zup.estrelas.sistemaprefeitura.dto.ProjetoDto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Funcionario;
import br.com.zup.estrelas.sistemaprefeitura.entity.Projeto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Secretaria;
import br.com.zup.estrelas.sistemaprefeitura.enums.Area;
import br.com.zup.estrelas.sistemaprefeitura.repository.ProjetoRepository;
import br.com.zup.estrelas.sistemaprefeitura.repository.SecretariaRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProjetoServiceTests {

	private static final String CADASTRO_REALIZADO_COM_SUCESSO = "Cadastro realizado com sucesso.";
	private static final String PROJETO_JA_CADASTRADO = "Falha no cadastro. Já existe um projeto com o mesmo nome.";
	private static final String DESCRICAO_VAZIO = "Descrição do projeto não pode estar vazia.";
	
	@Autowired 
	private ProjetoRepository projetoRepository;
	
	@InjectMocks
	private ProjetoService projetoService;
	
	@InjectMocks
	private SecretariaService secretariaService;
	
	@Autowired 
	private SecretariaRepository secretariaRepository;
	
	public static Secretaria dadosSecretaria() {

		Secretaria secretaria = new Secretaria();
		List<Funcionario> funcionarios = new ArrayList<>();
		List<Projeto> projetos = new ArrayList<>();

		secretaria.setIdSecretaria(1L);
		secretaria.setArea(Area.EDUCACAO);
		secretaria.setOrcamentoProjetos(100000.0);
		secretaria.setOrcamentoFolha(100000.0);
		secretaria.setTelefone("40028922");
		secretaria.setEndereco("Rua da educação");
		secretaria.setSite("www.secedu.com.br");
		secretaria.setEmail("secedu@Email.com");
		secretaria.setFuncionarios(funcionarios);
		secretaria.setProjetos(projetos);

		return secretaria;
	}
	
	
	public Projeto dadosProjeto() {
		
		Projeto projeto = new Projeto();
		Secretaria secretaria = dadosSecretaria();
		
		projeto.setIdProjeto(1l);
		projeto.setNome("Educação para toda população");
		projeto.setDescricao("Promover educação para todos");
		projeto.setCusto(10000.0);
		projeto.setSecretaria(secretaria);
		projeto.setConcluido(false);
		projeto.setDataInicio(LocalDate.now());
			
		return projeto;
	}
	
	public ProjetoDto dadosProjetoDto() {
		
		ProjetoDto projetoDto = new ProjetoDto();
		Secretaria secretaria = dadosSecretaria();
		
		projetoDto.setNome("Educação para todos");
		projetoDto.setDescricao("Promover educação para todos");
		projetoDto.setCusto(10000.0);
		projetoDto.setIdSecretaria(1L);
		
		return projetoDto;
	}
	
	@Test
	public void AdicionaProjetoComSucesso() {
		
		ProjetoDto projetoDto = dadosProjetoDto();
		String nomeDoProjeto = projetoDto.getNome();
		
		Optional<Secretaria> secretaria = Optional.of(dadosSecretaria());
		
		Mockito.when(projetoRepository.existsByNome(nomeDoProjeto)).thenReturn(false);
		Mockito.when(secretariaRepository.existsById(projetoDto.getIdSecretaria())).thenReturn(true);
		
		MensagemDto mensagemRetornada = projetoService.adicionaProjeto(projetoDto);
	    MensagemDto mensagemEsperada = new MensagemDto(CADASTRO_REALIZADO_COM_SUCESSO);
	        
	    Assert.assertEquals("Deve cadastrar o projeto com sucesso", mensagemEsperada, mensagemRetornada);
		
	}
	
	
	@Test
	public void naoDeveAdionarProjetoCOmNomeExistente() {
		
		ProjetoDto projetoDto = dadosProjetoDto();
		String nomeDoProjeto = projetoDto.getNome();
		
		Optional<Secretaria> secretaria = Optional.of(dadosSecretaria());
		
		Mockito.when(projetoRepository.existsByNome(nomeDoProjeto)).thenReturn(true);
		Mockito.when(secretariaRepository.existsById(projetoDto.getIdSecretaria())).thenReturn(true);
		
		MensagemDto mensagemRetornada = projetoService.adicionaProjeto(projetoDto);
	    MensagemDto mensagemEsperada = new MensagemDto(PROJETO_JA_CADASTRADO);
	        
	    Assert.assertEquals("Deve apresentar mensagem de erro ao cadastrar projeto", mensagemEsperada, mensagemRetornada);
		
	}
	
	
	@Test
	public void naoCadastraProjetoComInformacaoFaltanto() {
		
		ProjetoDto projetoDto = new ProjetoDto();
		Secretaria secretaria = dadosSecretaria();
		
		projetoDto.setNome("Educação para todos");
		projetoDto.setCusto(10000.0);
		projetoDto.setIdSecretaria(1L);
		
		Mockito.when(projetoRepository.existsByNome(projetoDto.getNome())).thenReturn(false);
		MensagemDto mensagemRetornada = projetoService.adicionaProjeto(projetoDto);
		MensagemDto mensagemEsperada = new MensagemDto(DESCRICAO_VAZIO);

		Assert.assertEquals("Deve retornar uma mensagem de falha ao adicionar projeto", mensagemEsperada, mensagemRetornada);
		
	}
}
