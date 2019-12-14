package br.com.gestao.modulos.financeiro.banco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BancoRepositoryQuery {
    Page<Banco> findAll(Pageable pageable, BancoRepositoryFiltro bancoRepositoryFiltro);
}
