package br.com.gestao.modulos.financeiro.bancoExtratoAbreviacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BancoExtratoAbreviacaoRepository extends JpaRepository<BancoExtratoAbreviacao, Long>, BancoExtratoAbreviacaoRepositoryQuery {

    Optional<BancoExtratoAbreviacao> findByUuid(String key);
}
