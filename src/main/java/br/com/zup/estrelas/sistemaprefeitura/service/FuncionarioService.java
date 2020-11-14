package br.com.zup.estrelas.sistemaprefeitura.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.zup.estrelas.sistemaprefeitura.dto.FuncionarioDto;
import br.com.zup.estrelas.sistemaprefeitura.dto.MensagemDto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Funcionario;
import br.com.zup.estrelas.sistemaprefeitura.entity.Secretaria;
import br.com.zup.estrelas.sistemaprefeitura.repository.FuncionarioRepository;
import br.com.zup.estrelas.sistemaprefeitura.repository.SecretariaRepository;

@Service
public class FuncionarioService implements IFuncionarioService {

	 private static final String FUNCIONARIO_JA_CADASTRADO = "Falha no cadastro. Já existe funcionário com o cpf informado";
	 private static final String CADASTRO_REALIZADO_COM_SUCESSO = "Cadastro realizado com sucesso.";
	 private static final String FUNCIONARIO_REMOVIDO_COM_SUCESSO = "Funcionário removido com sucesso!";
	 private static final String FUNCIONARIO_ALTERADO_COM_SUCESSO = "Funcionário alterado com sucesso.";
	 private static final String FUNCIONARIO_INEXISTENTE = "Funcionário inexistente.";
	 private static final String ORCAMENTO_FOLHA_INSUFICIENTE = "Orçamento de folha de pagamento insuficiente para arcar com o salário informado";
	 private static final String SECRETARIA_INEXISTENTE = "Secretaria inexistente.";
	 private static final String SALARIO_INVALIDO = "Salário inválido.O novo salário não pode ser menor que atual.";
	 private static final String IMPOSSIVEL_ALTERAR_CPF = "Não é possível alterar o CPF do funcionário.";
	 private static final String NOME_VAZIO = "Nome do funcionário não pode estar vazio.";
	 private static final String CPF_VAZIO = "Cpf do funcionário não pode estar vazio.";
	 private static final String SALARIO_VAZIO = "Salário do funcionário não pode estar vazio.";
	 private static final String SALARIO_INFERIOR_MINIMO = "O salário não pode ser inferior a R$1045.";
	 private static final String ID_SECRETARIA_VAZIO = "ID da secretaria não pode estar vazio.";
	 private static final String FUNCAO_VAZIO = "Função do funcionário não pode estar vazio.";
	 private static final String CONCURSADO_VAZIO = "Status de concursado não pode estar vazio.";
	 
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private SecretariaRepository secretariaRepository;
	
	@Override
	public MensagemDto adicionaFuncionario(FuncionarioDto funcionario) {
		
		if(funcionario.getNome() == null || funcionario.getNome().isBlank() || funcionario.getNome().isEmpty()) {
			return new MensagemDto(NOME_VAZIO);
		}
		
		if( funcionario.getCpf() == null ||  funcionario.getCpf().isBlank() || funcionario.getCpf().isEmpty()) {
			return new MensagemDto(CPF_VAZIO);
		}
		
		if( funcionario.getSalario() == null || funcionario.getSalario().toString().isEmpty() ) {
			return new MensagemDto(SALARIO_VAZIO);
		}
		
		if(funcionario.getIdSecretaria() == null ||funcionario.getIdSecretaria().toString().isEmpty()) {
			return new MensagemDto(ID_SECRETARIA_VAZIO);
		}
		
		if(funcionario.getFuncao() == null || funcionario.getFuncao().isBlank() || funcionario.getFuncao().isEmpty() ) {
			return new MensagemDto(FUNCAO_VAZIO);
		}
		
		if(funcionario.getConcursado() == null || funcionario.getConcursado().toString().isBlank() || funcionario.getConcursado().toString().isEmpty() ) {
			return new MensagemDto(CONCURSADO_VAZIO);
		}
		
		Secretaria novaSecretaria = secretariaRepository.findById(funcionario.getIdSecretaria()).orElse(null);
		
		if(!secretariaRepository.existsById(funcionario.getIdSecretaria())) {
			return new MensagemDto(SECRETARIA_INEXISTENTE);
		}
		
		if(funcionarioRepository.existsByCpf(funcionario.getCpf())) {
			return new MensagemDto(FUNCIONARIO_JA_CADASTRADO);
		}
		
		final double SALARIOMINIMO = 1045.0;
		
		if(funcionario.getSalario() < SALARIOMINIMO) {
			return new MensagemDto(SALARIO_INFERIOR_MINIMO);
		}
		
		if(novaSecretaria.getOrcamentoFolha() < funcionario.getSalario()) {
			return new MensagemDto(ORCAMENTO_FOLHA_INSUFICIENTE);
		}
		
		Funcionario novoFuncionario = new Funcionario();
		BeanUtils.copyProperties (funcionario, novoFuncionario);
		
		atualizaOrcamentoSecretariaAoCadastrar(novaSecretaria,  funcionario);
		novoFuncionario.setSecretaria(novaSecretaria);
		funcionarioRepository.save(novoFuncionario);
		
		return new MensagemDto(CADASTRO_REALIZADO_COM_SUCESSO);
	}

	@Override
	public Funcionario buscaFuncionario(Long idFuncionario) {
		return funcionarioRepository.findById(idFuncionario).orElse(null);
	}

	@Override
	public List<Funcionario> listaFuncionarios() {
		return (List<Funcionario>) funcionarioRepository.findAll();
	}

	@Override
	public MensagemDto removeFuncionario(Long idFuncionario) {
		
		if (funcionarioRepository.existsById(idFuncionario)) {
		
			Optional<Funcionario> funcionarioConsultado = funcionarioRepository.findById(idFuncionario);
			Funcionario funcionarioASerRemovido = funcionarioConsultado.get();
			atualizaOrcamentoSecretariaAoRemover(funcionarioASerRemovido);
			funcionarioRepository.deleteById(idFuncionario);
			
            return new MensagemDto(FUNCIONARIO_REMOVIDO_COM_SUCESSO);
        }
        return new MensagemDto(FUNCIONARIO_INEXISTENTE);
	}

	@Override
	public MensagemDto alteraFuncionario(Long idFuncionario, FuncionarioDto funcionario){
			
		Optional<Funcionario> funcionarioConsultado = funcionarioRepository.findById(idFuncionario);
		 
		if (funcionarioConsultado.isPresent()) {

				Funcionario funcionarioAlterado = funcionarioConsultado.get();
				Funcionario funcionarioAtual = funcionarioRepository.findById(idFuncionario).get();
				Secretaria secretariaConsultada = secretariaRepository.findById(funcionario.getIdSecretaria()).get();
				
				final Double DIFERENCA_SALARIAL = calculaDiferencaSalarial (funcionario, funcionarioAlterado);
				
				if(secretariaConsultada.getOrcamentoFolha() < funcionario.getSalario()) {
					return new MensagemDto(ORCAMENTO_FOLHA_INSUFICIENTE);
					}
				if(funcionario.getSalario() < funcionarioAlterado.getSalario()) {
					return new MensagemDto(SALARIO_INVALIDO);
				}
				
				String cpfCadastrado = funcionarioAlterado.getCpf();
				if (!funcionario.getCpf().equals(cpfCadastrado)) {
					return new MensagemDto(IMPOSSIVEL_ALTERAR_CPF);
				}
				    
	    		Long idSecretariaNova = funcionario.getIdSecretaria();
	    		Secretaria secretariaNova = secretariaRepository.findById(funcionario.getIdSecretaria()).orElse(null);         
	    		Secretaria secretariaAntiga = new Secretaria();
	    		
	            if(idSecretariaNova != funcionarioAtual.getSecretaria().getIdSecretaria()) {
	            	
	            		atualizaOrcamentoAcrescentando(funcionarioAtual, funcionarioAlterado.getSecretaria(), DIFERENCA_SALARIAL);
		         		atualizaOrcamentoDiminuindo( funcionario, secretariaConsultada);
	         		
		         		funcionarioAlterado.setSalario(funcionario.getSalario());
	            		funcionarioAlterado.setSecretaria(secretariaNova);
	            		funcionarioRepository.save(funcionarioAlterado);
            		
	            		return new MensagemDto(FUNCIONARIO_ALTERADO_COM_SUCESSO);
	            } else {
	            	  
	            	secretariaAntiga = atualizaOrcamentoDaFolhaAntiga( funcionario, funcionarioAlterado);
	            }
	            funcionarioAlterado.setNome(funcionario.getNome());
	            funcionarioAlterado.setSalario(funcionario.getSalario());
	            funcionarioAlterado.setFuncao(funcionario.getFuncao());
	            funcionarioAlterado.setConcursado(funcionario.getConcursado());
	            funcionarioRepository.save(funcionarioAlterado);
	            secretariaRepository.save(secretariaAntiga);
	            return new MensagemDto(FUNCIONARIO_ALTERADO_COM_SUCESSO);
	        }
	        return new MensagemDto(FUNCIONARIO_INEXISTENTE);
	    }
	
	
	public Secretaria atualizaOrcamentoSecretariaAoCadastrar(Secretaria novaSecretaria, FuncionarioDto funcionario) {
		
		Double orcamentoFolhaAtualizado = novaSecretaria.getOrcamentoFolha() - funcionario.getSalario();
		novaSecretaria.setOrcamentoFolha(orcamentoFolhaAtualizado);
		
		return novaSecretaria;	
	}
	
	public Secretaria atualizaOrcamentoSecretariaAoRemover(Funcionario funcionarioASerRemovido) {
		
		Secretaria secretariaDoFuncionario = funcionarioASerRemovido.getSecretaria();
		Double orcamentoFolhaAtualizado = secretariaDoFuncionario.getOrcamentoFolha() + funcionarioASerRemovido.getSalario();
		secretariaDoFuncionario.setOrcamentoFolha(orcamentoFolhaAtualizado);
		
		return secretariaDoFuncionario;
	}
	
	public Double calculaDiferencaSalarial (FuncionarioDto funcionario, Funcionario funcionarioAlterado) {
		
		Double salarioAtual =  funcionarioAlterado.getSalario();
		Double salarioNovo = funcionario.getSalario();
		Double diferencaSalarial = salarioNovo - salarioAtual;
		
		return diferencaSalarial;
	}

	public Secretaria atualizaOrcamentoDaFolhaAntiga (FuncionarioDto funcionario, Funcionario funcionarioAlterado) {
		
		Double orcamentoFolhaAtualizado = funcionarioAlterado.getSecretaria().getOrcamentoFolha() - calculaDiferencaSalarial(funcionario, funcionarioAlterado);
		funcionarioAlterado.getSecretaria().setOrcamentoFolha(orcamentoFolhaAtualizado);
     
		Secretaria secretariaAntiga = secretariaRepository.findById(funcionarioAlterado.getSecretaria().getIdSecretaria()).get();
		secretariaAntiga.setOrcamentoFolha(orcamentoFolhaAtualizado);
		return secretariaAntiga;
	}
	
	public void atualizaOrcamentoAcrescentando( Funcionario funcionarioAlterado, Secretaria secretariaAntiga, Double DIFERENCA_SALARIAL) {
		
 		secretariaAntiga.setOrcamentoFolha((secretariaAntiga.getOrcamentoFolha() + funcionarioAlterado.getSalario()) - DIFERENCA_SALARIAL);
		secretariaRepository.save(secretariaAntiga);
	}
	
	public void atualizaOrcamentoDiminuindo( FuncionarioDto funcionario, Secretaria secretariaNova) {
		
		secretariaNova.setOrcamentoFolha(secretariaNova.getOrcamentoFolha() - funcionario.getSalario());
 		secretariaRepository.save(secretariaNova);
	}	
}
