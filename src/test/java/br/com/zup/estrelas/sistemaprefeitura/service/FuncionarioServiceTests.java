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

import br.com.zup.estrelas.sistemaprefeitura.dto.FuncionarioDto;
import br.com.zup.estrelas.sistemaprefeitura.dto.MensagemDto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Funcionario;
import br.com.zup.estrelas.sistemaprefeitura.entity.Projeto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Secretaria;
import br.com.zup.estrelas.sistemaprefeitura.enums.Area;
import br.com.zup.estrelas.sistemaprefeitura.repository.FuncionarioRepository;
import br.com.zup.estrelas.sistemaprefeitura.repository.SecretariaRepository;

@RunWith(MockitoJUnitRunner.class)
public class FuncionarioServiceTests {

	 private static final String CADASTRO_REALIZADO_COM_SUCESSO = "Cadastro realizado com sucesso.";
	 private static final String FUNCIONARIO_JA_CADASTRADO = "Falha no cadastro. Já existe funcionário com o cpf informado";
	 private static final String FUNCAO_VAZIO = "Função do funcionário não pode estar vazio.";
	 private static final String ORCAMENTO_FOLHA_INSUFICIENTE = "Orçamento de folha de pagamento insuficiente para arcar com o salário informado";
	 private static final String FUNCIONARIO_REMOVIDO_COM_SUCESSO = "Funcionário removido com sucesso!";
	 private static final String FUNCIONARIO_INEXISTENTE = "Funcionário inexistente.";
	 private static final String FUNCIONARIO_ENCONTRADO = "Funcionário encontrado.";
	 
	@Mock 
	private SecretariaRepository secretariaRepository;
	
	@Mock 
	private FuncionarioRepository funcionarioRepository;
	
	@InjectMocks
	private SecretariaService secretariaService;
	
	
	@InjectMocks
	private FuncionarioService funcionarioService;
	
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

	public static FuncionarioDto dadosFuncionarioDto() {

		FuncionarioDto funcionarioDto = new FuncionarioDto();

		funcionarioDto.setNome("José");
		funcionarioDto.setCpf("12345789010");
		funcionarioDto.setSalario(1100.0);
		funcionarioDto.setIdSecretaria(1L);
		funcionarioDto.setFuncao("Desenvolvedor");
		funcionarioDto.setConcursado(false);

		return funcionarioDto;

	}

	public static Funcionario dadosFuncionario() {

		Funcionario funcionario = new Funcionario();
		Secretaria secretaria = dadosSecretaria();

		funcionario.setIdFuncionario(1L);
		funcionario.setNome("José");
		funcionario.setCpf("12345789010");
		funcionario.setSalario(1100.0);
		funcionario.setSecretaria(secretaria);
		funcionario.setFuncao("Desenvolvedor");
		funcionario.setConcursado(false);
		

		return funcionario;
	}

	@Test
	public void deveCadastrarUmFuncionarioComSucesso() {

		FuncionarioDto funcionarioDto = dadosFuncionarioDto();

		
		// FIXME: Faltou mocar a chamada ao secretariaRepository que acontece na linha 69 
		// do funcionário service.
		Mockito.when(funcionarioRepository.existsByCpf(funcionarioDto.getCpf())).thenReturn(false);
			
		MensagemDto mensagemRetornada = funcionarioService.adicionaFuncionario(funcionarioDto);
		MensagemDto mensagemEsperada = new MensagemDto(CADASTRO_REALIZADO_COM_SUCESSO);

		Assert.assertEquals("Deve retornar uma mensagem de sucesso ao adicionar funcionario", mensagemEsperada,mensagemRetornada);
	}

	@Test
	public void naoCadastrarUmFuncionarioExistente() {
		
	    // FIXME: Faltou mocar a chamada ao secretariaRepository que acontece na linha 69 
        // do funcionário service.
		FuncionarioDto funcionarioDto = dadosFuncionarioDto();

		Mockito.when(funcionarioRepository.existsByCpf(funcionarioDto.getCpf())).thenReturn(true);
		
		MensagemDto mensagemRetornada = funcionarioService.adicionaFuncionario(funcionarioDto);
		MensagemDto mensagemEsperada = new MensagemDto(FUNCIONARIO_JA_CADASTRADO);

		Assert.assertEquals("Deve retornar uma mensagem de erro ao adicionar funcionario", mensagemEsperada,mensagemRetornada);
	}

	@Test
	public void naoCadastraFuncionarioComInformacaoFaltanto() {
		
		FuncionarioDto funcionarioDto = new FuncionarioDto();

		funcionarioDto.setNome("José");
		funcionarioDto.setCpf("12345789010");
		funcionarioDto.setSalario(1100.0);
		funcionarioDto.setIdSecretaria(1L);
		funcionarioDto.setConcursado(false);
		
		Mockito.when(funcionarioRepository.existsByCpf(funcionarioDto.getCpf())).thenReturn(false);
		MensagemDto mensagemRetornada = funcionarioService.adicionaFuncionario(funcionarioDto);
		MensagemDto mensagemEsperada = new MensagemDto(FUNCAO_VAZIO);

		Assert.assertEquals("Deve retornar uma mensagem de falha ao adicionar funcionario", mensagemEsperada, mensagemRetornada);
		
	}
	
	@Test
	public void naoCadastraFuncionarioSeOrcamentoNaoPermite() {
		
		FuncionarioDto funcionarioDto = dadosFuncionarioDto();

		funcionarioDto.setSalario(500000.0);
		
		Mockito.when(funcionarioRepository.existsByCpf(funcionarioDto.getCpf())).thenReturn(false);
		
		MensagemDto mensagemRetornada = funcionarioService.adicionaFuncionario(funcionarioDto);
		MensagemDto mensagemEsperada = new MensagemDto(ORCAMENTO_FOLHA_INSUFICIENTE);

		Assert.assertEquals("Deve retornar uma mensagem de falha ao adicionar funcionario", mensagemEsperada, mensagemRetornada);
		
	}
	
	@Test
	public void deveRemoverUmFuncionarioComSucesso() {

		FuncionarioDto funcionarioDto = dadosFuncionarioDto();
		Funcionario funcionario = dadosFuncionario();
		Optional<Funcionario> funcionarioOptional = Optional.of(funcionario );
		
		Mockito.when(funcionarioRepository.existsByCpf(funcionario.getCpf())).thenReturn(true);
			
		MensagemDto mensagemRetornada = funcionarioService.removeFuncionario(1L);
		MensagemDto mensagemEsperada = new MensagemDto(FUNCIONARIO_REMOVIDO_COM_SUCESSO);

		Assert.assertEquals("Deve retornar uma mensagem de sucesso ao remover funcionario", mensagemEsperada,mensagemRetornada);
	}
	
	@Test
	public void naoDeveRemoverUmFuncionarioInexistente() {

		FuncionarioDto funcionarioDto = dadosFuncionarioDto();
		
		funcionarioDto.setCpf("0000000000");
		
		Mockito.when(funcionarioRepository.existsByCpf(funcionarioDto.getCpf())).thenReturn(false);
			
		MensagemDto mensagemRetornada = funcionarioService.removeFuncionario(1L);
		MensagemDto mensagemEsperada = new MensagemDto(FUNCIONARIO_INEXISTENTE);

		Assert.assertEquals("Deve retornar uma mensagem de falha ao remover funcionario", mensagemEsperada,mensagemRetornada);
	}
	
	
	@Test 
	public void deveBuscarUmFuncionarioComSucesso() {
		
		Funcionario funcionario = dadosFuncionario();
		Optional<Funcionario> funcionarioOptional = Optional.of(funcionario);
		
		Long idFuncionario = funcionario.getIdFuncionario();
		Long idBusca= 1L;
		
		Mockito.when(funcionarioRepository.existsById(idFuncionario)).thenReturn(true);
		
		MensagemDto mensagemRetornada = null;
		
		Funcionario funcionarioEncontrado = funcionarioService.buscaFuncionario(idBusca);
		
		if(funcionarioEncontrado != null) {
			 mensagemRetornada = new MensagemDto(FUNCIONARIO_ENCONTRADO);
		}
		
		MensagemDto mensagemEsperada = new MensagemDto(FUNCIONARIO_INEXISTENTE);

		Assert.assertEquals("Deve retornar uma mensagem de sucesso ao buscar funcionario", mensagemEsperada, mensagemRetornada);
	
	}
	
	@Test 
	public void deveFalharAoBuscarFuncionarioInexistente() {
		
		Funcionario funcionario = dadosFuncionario();
		Optional<Funcionario> funcionarioOptional = Optional.of(funcionario);
		
		Long idFuncionario = funcionario.getIdFuncionario();
		Long idBusca= 10000L;
		
		Mockito.when(funcionarioRepository.existsById(idFuncionario)).thenReturn(false);
		
		MensagemDto mensagemRetornada = null;
		Funcionario funcionarioEncontrado = funcionarioService.buscaFuncionario(idBusca);
		
		if(funcionarioEncontrado != null) {
			 mensagemRetornada = new MensagemDto(FUNCIONARIO_ENCONTRADO);
		}
		
		MensagemDto mensagemEsperada = new MensagemDto(FUNCIONARIO_INEXISTENTE);

		// FIXME: Aqui talvez faria mais sentido testar se o FuncionarioEncontrado
		// é nullo. "Assert.assertNull(object);"
		Assert.assertEquals("Deve retornar uma mensagem de falha ao buscar funcionario", mensagemEsperada, mensagemRetornada);
	
	}
}
