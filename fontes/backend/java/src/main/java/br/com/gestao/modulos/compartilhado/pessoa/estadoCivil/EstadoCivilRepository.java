package br.com.gestao.modulos.compartilhado.pessoa.estadoCivil;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoCivilRepository extends JpaRepository<EstadoCivil, Long>, EstadoCivilRepositoryQuery {

    Optional<EstadoCivil> findByUuid(String key);
}
