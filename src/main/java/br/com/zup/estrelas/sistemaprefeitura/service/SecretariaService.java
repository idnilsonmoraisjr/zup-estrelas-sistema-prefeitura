package br.com.zup.estrelas.sistemaprefeitura.service;


import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.zup.estrelas.sistemaprefeitura.dto.MensagemDto;
import br.com.zup.estrelas.sistemaprefeitura.dto.SecretariaDto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Funcionario;
import br.com.zup.estrelas.sistemaprefeitura.entity.Secretaria;
import br.com.zup.estrelas.sistemaprefeitura.repository.SecretariaRepository;

@Service
public class SecretariaService implements ISecretariaService {

	 private static final String CADASTRO_REALIZADO_COM_SUCESSO = "Cadastro realizado com sucesso.";
	 private static final String SECRETARIA_REMOVIDA_COM_SUCESSO = "Secretaria removida com sucesso!";
	 private static final String SECRETARIA_ALTERADA_COM_SUCESSO = "Secretaria alterada com sucesso!";
	 private static final String SECRETARIA_JA_CADASTRADA = "Falha no cadastro. Secretaria já existe.";
	 private static final String SECRETARIA_INEXISTENTE = "Secretaria inexistente.";
	 private static final String ORCAMENTO_FOLHA_INVALIDO = "Não foi possível atualizar orçamento da folha de pagamento para valor sugerido.";
	 private static final String ORCAMENTO_PROJETO_INVALIDO = "Não foi possível atualizar orçamento do projeto para valor sugerido.";
	 private static final String AREA_VAZIA = "Área da secretaria não pode estar vazia.";
	 private static final String ORCAMENTO_PROJETOS_VAZIO = "O orçamento de projetos não pode estar vazio.";
	 private static final String ORCAMENTO_FOLHA_VAZIO = "O orçamento de folha não pode estar vazio.";
	 private static final String TELEFONE_VAZIO = "O telefone não pode estar vazio.";
	 private static final String ENDERECO_VAZIO = "O endereço não pode estar vazio.";
	 private static final String SITE_VAZIO = "O site não pode estar vazio.";
	 private static final String EMAIL_VAZIO = "O Email não pode estar vazio.";
	 
	@Autowired 
	SecretariaRepository secretariaRepository;
	
	
	@Override
	public MensagemDto adicionaSecretaria(SecretariaDto secretaria) {
		
		if(secretaria.getArea() == null || secretaria.getArea().toString().isBlank() || secretaria.getArea().toString().isEmpty()) {
			return new MensagemDto(AREA_VAZIA);
		}
		
		if( secretaria.getOrcamentoProjetos() == null || secretaria.getOrcamentoProjetos() <= 0 ||  secretaria.getOrcamentoProjetos().toString().isBlank() || secretaria.getOrcamentoProjetos().toString().isEmpty()) {
			return new MensagemDto(ORCAMENTO_PROJETOS_VAZIO);
		}
		
		if(secretaria.getOrcamentoFolha() == null || secretaria.getOrcamentoFolha() <= 0 || secretaria.getOrcamentoFolha().toString().isBlank() || secretaria.getOrcamentoFolha().toString().isEmpty()) {
			return new MensagemDto(ORCAMENTO_FOLHA_VAZIO);
		}
		
		if(secretaria.getTelefone() == null || secretaria.getTelefone().isBlank() || secretaria.getTelefone().isEmpty()) {
			return new MensagemDto(TELEFONE_VAZIO);
		}
		
		if(secretaria.getEndereco() == null || secretaria.getEndereco().isBlank() || secretaria.getEndereco().isEmpty()) {
			return new MensagemDto(ENDERECO_VAZIO);
		}
		
		if(secretaria.getSite() == null || secretaria.getSite().isBlank() || secretaria.getSite().isEmpty()) {
			return new MensagemDto(SITE_VAZIO);
		}
		
		if(secretaria.getEmail() == null || secretaria.getEmail().isBlank() || secretaria.getEmail().isEmpty()) {
			return new MensagemDto(EMAIL_VAZIO);
		}
		
		if(secretariaRepository.existsByArea(secretaria.getArea())) {
			return new MensagemDto(SECRETARIA_JA_CADASTRADA);
		}
		
		Secretaria novaSecretaria = new Secretaria();
		BeanUtils.copyProperties (secretaria, novaSecretaria) ;
		
		secretariaRepository.save(novaSecretaria);
		return new MensagemDto(CADASTRO_REALIZADO_COM_SUCESSO);
	}

	@Override
	public Secretaria buscaSecretaria(Long idSecretaria) { 
		return secretariaRepository.findById(idSecretaria).orElse(null);
	}

	@Override
	public List<Secretaria> listaSecretarias() {
		return (List<Secretaria>) secretariaRepository.findAll();
	}

	@Override
	public MensagemDto removeSecretaria(Long idSecretaria) {
		 
		if (secretariaRepository.existsById(idSecretaria)) {
	            secretariaRepository.deleteById(idSecretaria);
	            return new MensagemDto(SECRETARIA_REMOVIDA_COM_SUCESSO);
	        }
	        return new MensagemDto(SECRETARIA_INEXISTENTE);
	}

	@Override
	public MensagemDto alteraSecretaria(Long idSecretaria, SecretariaDto secretaria) {

		Optional<Secretaria> secretariaConsultada = secretariaRepository.findById(idSecretaria);
		 
		if (secretariaConsultada.isPresent()) {

	            Secretaria secretariaAlterada = secretariaConsultada.get();
	            secretariaAlterada.setArea(secretaria.getArea());
	            secretariaAlterada.setOrcamentoProjetos(secretaria.getOrcamentoProjetos());
	           
	            if(secretariaAlterada.getOrcamentoFolha() >= secretaria.getOrcamentoFolha()) {
	            	return new MensagemDto(ORCAMENTO_FOLHA_INVALIDO);
	            }
	            
	            if(secretariaAlterada.getOrcamentoProjetos() >= secretaria.getOrcamentoProjetos()) {
	            	return new MensagemDto(ORCAMENTO_PROJETO_INVALIDO);
	            }
	            	            
	            List<Funcionario> funcionarios = secretariaAlterada.getFuncionarios();
	            double totalFolhaPagamento = 0.0;	            
	            calculaTotalDaFolhaPagamento(funcionarios, idSecretaria, totalFolhaPagamento);
            	Double novoOrcamentoFolha = secretaria.getOrcamentoFolha() - totalFolhaPagamento;
            	
            	secretariaAlterada.setOrcamentoFolha(novoOrcamentoFolha);
	            secretariaAlterada.setTelefone(secretaria.getTelefone());
	            secretariaAlterada.setEndereco(secretaria.getEndereco());
	            secretariaAlterada.setSite(secretaria.getSite());
	            secretariaAlterada.setEmail(secretaria.getEmail());

	            secretariaRepository.save(secretariaAlterada);
	            return new MensagemDto(SECRETARIA_ALTERADA_COM_SUCESSO);
	        }

	        return new MensagemDto(SECRETARIA_INEXISTENTE);
	    }
	
	public double calculaTotalDaFolhaPagamento(List<Funcionario> funcionarios, Long idSecretaria, double totalFolhaPagamento) {
		
        for (Funcionario funcionarioCadastrado : funcionarios) {
        	
        	Long idSecretariaDoFuncionario = funcionarioCadastrado.getSecretaria().getIdSecretaria();
        	
        	if(idSecretariaDoFuncionario.equals(idSecretaria)) {
        		totalFolhaPagamento += funcionarioCadastrado.getSalario();
        	}
        }
        return totalFolhaPagamento;
	}
}
