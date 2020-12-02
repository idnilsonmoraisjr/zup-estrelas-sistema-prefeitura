package br.com.zup.estrelas.sistemaprefeitura.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

import br.com.zup.estrelas.sistemaprefeitura.dto.MensagemDto;
import br.com.zup.estrelas.sistemaprefeitura.dto.SecretariaDto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Funcionario;
import br.com.zup.estrelas.sistemaprefeitura.entity.Projeto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Secretaria;
import br.com.zup.estrelas.sistemaprefeitura.enums.Area;
import br.com.zup.estrelas.sistemaprefeitura.repository.SecretariaRepository;

@RunWith(MockitoJUnitRunner.class)
public class SecretariaServiceTests {

	 private static final String CADASTRO_REALIZADO_COM_SUCESSO = "Cadastro realizado com sucesso.";
	 private static final String SECRETARIA_JA_CADASTRADA = "Falha no cadastro. Secretaria já existe.";
	 private static final String ORCAMENTO_PROJETOS_VAZIO = "O orçamento de projetos não pode estar vazio.";
	 private static final String SECRETARIA_REMOVIDA_COM_SUCESSO = "Secretaria removida com sucesso!";
	 private static final String SECRETARIA_INEXISTENTE = "Secretaria inexistente.";
	 private static final String SECRETARIA_ALTERADA_COM_SUCESSO = "Secretaria alterada com sucesso!";
	 private static final String ORCAMENTO_FOLHA_INVALIDO = "Não foi possível atualizar orçamento da folha de pagamento para valor sugerido.";
	 
	 public static SecretariaDto dadosSecretariaDto() {
		 	SecretariaDto secretariaDto = new SecretariaDto();
			
			secretariaDto.setArea(Area.EDUCACAO);
			secretariaDto.setOrcamentoProjetos(100000.0);
			secretariaDto.setOrcamentoFolha(100000.0);
			secretariaDto.setTelefone("40028922");
			secretariaDto.setEndereco("Rua da educação");
			secretariaDto.setSite("www.secedu.com.br");
			secretariaDto.setEmail("secedu@Email.com");
			
			return secretariaDto;
	 }
	 
	 
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
	 
	@Mock 
	private SecretariaRepository secretariaRepository;
	
	@InjectMocks
	private SecretariaService secretariaService;
	
	@Test
	public void deveCadastrarSecretariaComSucesso() {
		
		SecretariaDto secretariaDto = dadosSecretariaDto();
		
		Mockito.when(secretariaRepository.existsByArea(Area.EDUCACAO)).thenReturn(false);
		MensagemDto mensagemRetornada = secretariaService.adicionaSecretaria(secretariaDto);
		MensagemDto mensagemEsperada = new MensagemDto(CADASTRO_REALIZADO_COM_SUCESSO);
		
		Assert.assertEquals("Deve retornar uma mensagem de sucesso ao adicionar secretaria", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void naoDeveCadastrarSecretariaCasoExistaUmaDeMesmaArea() {
				
		SecretariaDto secretariaDto = dadosSecretariaDto();
		
		
		Mockito.when(secretariaRepository.existsByArea(Area.EDUCACAO)).thenReturn(true);
		MensagemDto mensagemRetornada = secretariaService.adicionaSecretaria(secretariaDto);
		MensagemDto mensagemEsperada = new MensagemDto(SECRETARIA_JA_CADASTRADA);
		
		Assert.assertEquals("Deve retornar uma mensagem de falha ao adicionar secretaria", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void naoDeveCadastrarSecretariaCasoNaoInformeAlgumValor() {
		
		SecretariaDto secretariaDto = new SecretariaDto();
		
		secretariaDto.setArea(Area.EDUCACAO);
		secretariaDto.setOrcamentoFolha(100000.0);
		secretariaDto.setTelefone("40028922");
		secretariaDto.setEndereco("Rua da educação");
		secretariaDto.setSite("www.secedu.com.br");
		secretariaDto.setEmail("secedu@Email.com");
		
		
		Mockito.when(secretariaRepository.existsByArea(Area.EDUCACAO)).thenReturn(false);
		MensagemDto mensagemRetornada = secretariaService.adicionaSecretaria(secretariaDto);
		MensagemDto mensagemEsperada = new MensagemDto(ORCAMENTO_PROJETOS_VAZIO);
		
		Assert.assertEquals("Deve retornar uma mensagem de falha ao adicionar secretaria", mensagemEsperada, mensagemRetornada);
		
	}
	
	
	@Test
	public void deveRemoverSecretariaComSucesso() {
		
		 SecretariaDto secretariaDto = dadosSecretariaDto();
		
		Mockito.when(secretariaRepository.existsByArea(Area.EDUCACAO)).thenReturn(false);
		secretariaService.adicionaSecretaria(secretariaDto);
		
		Mockito.when(secretariaRepository.existsById(1L)).thenReturn(true);
		
		MensagemDto mensagemRetornada = secretariaService.removeSecretaria(1L);
		MensagemDto mensagemEsperada = new MensagemDto(SECRETARIA_REMOVIDA_COM_SUCESSO);
		
		Assert.assertEquals("Deve retornar uma mensagem de sucesso ao remover secretaria", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void naoDeveRemoverSecretariaInexistente() {
		
		Mockito.when(secretariaRepository.existsById(1L)).thenReturn(false);
		
		MensagemDto mensagemRetornada = secretariaService.removeSecretaria(1L);
		MensagemDto mensagemEsperada = new MensagemDto( SECRETARIA_INEXISTENTE);
		
		Assert.assertEquals("Deve retornar uma mensagem de sucesso ao remover secretaria", mensagemEsperada, mensagemRetornada);
	}
	
	
	@Test
	public void deveAlterarUmaSecretaria() {

		Secretaria secretaria = new Secretaria();
		SecretariaDto secretariaDto = dadosSecretariaDto();
		
		BeanUtils.copyProperties(secretariaDto, secretaria);
		
		Optional<Secretaria> secretariaConsultada = Optional.of(secretaria);
		Mockito.when(secretariaRepository.findById(1L)).thenReturn(secretariaConsultada);
		
		// FIXME: Dê uma revisada pq aqui está parando na validação de folha.
		MensagemDto mensagemRetornada = secretariaService.alteraSecretaria(1L, secretariaDto);
		MensagemDto mensagemEsperada = new MensagemDto(SECRETARIA_ALTERADA_COM_SUCESSO );
		
		Assert.assertEquals("Deve retornar mensagem de sucesso ao alterar uma secretaria", mensagemEsperada, mensagemRetornada);
	}
	

	@Test
	public void naodeveAlterarUmaSecretariaSeOrcamentoForMenor() {

		Secretaria secretaria = new Secretaria();
		SecretariaDto secretariaDto = dadosSecretariaDto();
		secretariaDto.setOrcamentoFolha(5000.0);
		
		BeanUtils.copyProperties(secretariaDto, secretaria);
		
		Optional<Secretaria> secretariaConsultada = Optional.of(secretaria);
		Mockito.when(secretariaRepository.findById(1L)).thenReturn(secretariaConsultada);
		
		MensagemDto mensagemRetornada = secretariaService.alteraSecretaria(1L, secretariaDto);
		MensagemDto mensagemEsperada = new MensagemDto(ORCAMENTO_FOLHA_INVALIDO);
		
		Assert.assertEquals("Deve retornar mensagem de erro ao alterar uma secretaria", mensagemEsperada, mensagemRetornada);
	}
	
	
	@Test
	public void naodeveAlterarUmaSecretariaInexistente() {

		SecretariaDto secretariaDto = dadosSecretariaDto();
		
		Optional<Secretaria> secretariaConsultada = Optional.empty();
		Mockito.when(secretariaRepository.findById(1L)).thenReturn(secretariaConsultada);
		
		MensagemDto mensagemRetornada = secretariaService.alteraSecretaria(1L, secretariaDto);
		MensagemDto mensagemEsperada = new MensagemDto(SECRETARIA_INEXISTENTE);
		
		Assert.assertEquals("Deve retornar mensagem de erro ao alterar uma secretaria", mensagemEsperada, mensagemRetornada);
	}
	
}
