package br.com.gestao.modulos.financeiro.bancoExtratoAbreviacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BancoExtratoAbreviacaoRepositoryQuery {
    Page<BancoExtratoAbreviacao> findAll(Pageable pageable, BancoExtratoAbreviacaoRepositoryFiltro filtro);
}
