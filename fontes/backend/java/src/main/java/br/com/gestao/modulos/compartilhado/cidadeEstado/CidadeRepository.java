package br.com.gestao.modulos.compartilhado.cidadeEstado;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CidadeRepository extends JpaRepository<Cidade, Long>, CidadeRepositoryQuery {

    Optional<Cidade> findByUuid(String key);
}
