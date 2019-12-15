package br.com.gestao.modulos.financeiro.bancoAgencia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BancoAgenciaRepository extends JpaRepository<BancoAgencia, Long>, BancoAgenciaRepositoryQuery {

    Optional<BancoAgencia> findByUuid(String key);
}
