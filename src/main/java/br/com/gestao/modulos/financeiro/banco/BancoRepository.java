package br.com.gestao.modulos.financeiro.banco;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BancoRepository extends JpaRepository<Banco, Long>, BancoRepositoryQuery {

    Optional<Banco> findByUuid(String key);
}
