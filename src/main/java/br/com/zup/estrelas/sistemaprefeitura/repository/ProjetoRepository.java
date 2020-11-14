package br.com.zup.estrelas.sistemaprefeitura.repository;

import org.springframework.data.repository.CrudRepository;
import br.com.zup.estrelas.sistemaprefeitura.entity.Projeto;

public interface ProjetoRepository extends CrudRepository<Projeto, Long>{

	public boolean existsByNome(String nome);
}
