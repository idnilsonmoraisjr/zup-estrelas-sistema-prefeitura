package br.com.zup.estrelas.sistemaprefeitura.repository;

import org.springframework.data.repository.CrudRepository;
import br.com.zup.estrelas.sistemaprefeitura.entity.Funcionario;

public interface FuncionarioRepository extends CrudRepository<Funcionario, Long>{

	public boolean existsByCpf(String cpf);
}
