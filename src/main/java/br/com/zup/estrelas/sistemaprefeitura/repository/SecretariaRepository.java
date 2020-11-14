package br.com.zup.estrelas.sistemaprefeitura.repository;

import org.springframework.data.repository.CrudRepository;
import br.com.zup.estrelas.sistemaprefeitura.entity.Secretaria;
import br.com.zup.estrelas.sistemaprefeitura.enums.Area;

public interface SecretariaRepository extends CrudRepository <Secretaria, Long>{
	
	public boolean existsByArea(Area area);
	
}
