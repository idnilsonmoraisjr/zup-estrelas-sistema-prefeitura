package br.com.zup.estrelas.sistemaprefeitura.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.zup.estrelas.sistemaprefeitura.dto.MensagemDto;
import br.com.zup.estrelas.sistemaprefeitura.dto.ProjetoDto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Projeto;
import br.com.zup.estrelas.sistemaprefeitura.entity.Secretaria;
import br.com.zup.estrelas.sistemaprefeitura.repository.ProjetoRepository;
import br.com.zup.estrelas.sistemaprefeitura.repository.SecretariaRepository;

@Service
public class ProjetoService implements IProjetoService {

	private static final String PROJETO_JA_CADASTRADO = "Falha no cadastro. Já existe um projeto com o mesmo nome.";
	private static final String CADASTRO_REALIZADO_COM_SUCESSO = "Cadastro realizado com sucesso.";
	private static final String PROJETO_ALTERADO_COM_SUCESSO = "Descrição do projeto alterada com sucesso.";
	private static final String PROJETO_INEXISTENTE = "Projeto inexistente.";
	private static final String ORCAMENTO_FOLHA_INSUFICIENTE = "Orçamento de folha de pagamento insuficiente para arcar com o custo informado.";
	private static final String SECRETARIA_INEXISTENTE = "Secretaria inexistente.";
	private static final String PROJETO_CONCLUIDO = "Projeto concluído com sucesso.";
	private static final String DATA_INVALIDA_CONCLUSAO = "Não foi possível concluir o projeto. A data final não pode ser antes da inicial.";
	private static final String NOME_VAZIO = "Nome do projeto não pode estar vazio.";
	private static final String CUSTO_VAZIO = "Custo do projeto não pode estar vazio.";
	private static final String ID_SECRETARIA_VAZIO = "Id da secretaria não pode estar vazia.";
	private static final String DESCRICAO_VAZIO = "Descrição do projeto não pode estar vazia.";
	
	@Autowired
	ProjetoRepository projetoRepository;

	@Autowired
	SecretariaRepository secretariaRepository;

	@Override
	public MensagemDto adicionaProjeto(ProjetoDto projeto) {

	    // TODO: Aqui cabe o mesmo comentário que fiz na secretaria.
		if(projeto.getNome() == null || projeto.getNome().isBlank() || projeto.getNome().isEmpty()) {
			return new MensagemDto(NOME_VAZIO);
		}
		
		if( projeto.getCusto() == null || projeto.getCusto().isNaN() ) {
			return new MensagemDto(CUSTO_VAZIO);
		}
		
		if(projeto.getIdSecretaria() == null || projeto.getIdSecretaria().toString().isEmpty() ) {
			return new MensagemDto(ID_SECRETARIA_VAZIO);
		}
		
		if(projeto.getDescricao() == null ||projeto.getDescricao().isBlank() || projeto.getDescricao().isEmpty() ) {
			return new MensagemDto(DESCRICAO_VAZIO);
		}
		
		Secretaria novaSecretaria = secretariaRepository.findById(projeto.getIdSecretaria()).orElse(null);

		if (!secretariaRepository.existsById(projeto.getIdSecretaria())) {
			return new MensagemDto(SECRETARIA_INEXISTENTE);
		}

		if (projetoRepository.existsByNome(projeto.getNome())) {
			return new MensagemDto(PROJETO_JA_CADASTRADO);
		}

		if (novaSecretaria.getOrcamentoProjetos() < projeto.getCusto()) {
			return new MensagemDto(ORCAMENTO_FOLHA_INSUFICIENTE);
		}

		Projeto novoProjeto = new Projeto();
		novaSecretaria.setIdSecretaria(projeto.getIdSecretaria());
		BeanUtils.copyProperties(projeto, novoProjeto);

		Double orcamentoProjetosAtualizado = novaSecretaria.getOrcamentoProjetos() - projeto.getCusto();
		novaSecretaria.setOrcamentoProjetos(orcamentoProjetosAtualizado);

		novoProjeto.setSecretaria(novaSecretaria);
		projetoRepository.save(novoProjeto);

		return new MensagemDto(CADASTRO_REALIZADO_COM_SUCESSO);
	}

	@Override
	public Projeto buscaProjeto(Long idProjeto) {
		return projetoRepository.findById(idProjeto).orElse(null);
	}

	@Override
	public List<Projeto> listaProjetos() {
		return (List<Projeto>) projetoRepository.findAll();
	}

	@Override
	public MensagemDto alteraProjeto(Long idProjeto, ProjetoDto projeto) {

		Optional<Projeto> projetoConsultado = projetoRepository.findById(idProjeto);

		if (projetoConsultado.isPresent()) {

			Projeto projetoAlterado = projetoConsultado.get();
			projetoAlterado.setDescricao(projeto.getDescricao());
			projetoRepository.save(projetoAlterado);
			return new MensagemDto(PROJETO_ALTERADO_COM_SUCESSO);
		}
		return new MensagemDto(PROJETO_INEXISTENTE);
	}

	@Override
	public MensagemDto concluiProjeto(Long idProjeto, LocalDate dataEntrega) {

		Optional<Projeto> projetoConsultado = projetoRepository.findById(idProjeto);

		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dataFormatada = dataEntrega.toString();
		LocalDate dataVerificada = LocalDate.parse(dataFormatada, formatador);

		if (projetoConsultado.isPresent()) {

			Projeto projetoConcluido= projetoConsultado.get();
			if (dataEntrega.isBefore(projetoConcluido.getDataInicio())) {	
				return new MensagemDto(DATA_INVALIDA_CONCLUSAO);
			}
			
			if (dataEntrega.equals(dataVerificada)) {

				projetoConcluido.setConcluido(true);
				projetoConcluido.setDataEntrega(dataEntrega);
				projetoRepository.save(projetoConcluido);
				return new MensagemDto(PROJETO_CONCLUIDO);
			}
		}
		
		// FIXME: Aqui seria legal usar o fail first, ou seja, testar a negativa do ifpresent e já 
		// jogar esse erro caso ela ocorresse.
		return new MensagemDto(PROJETO_INEXISTENTE);
	}
	
	
		
		
		
	
}
