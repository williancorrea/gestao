package br.com.gestao.modulos.financeiro.bancoAgencia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BancoAgenciaRepositoryQuery {
    Page<BancoAgencia> findAll(Pageable pageable, BancoAgenciaRepositoryFiltro filtro);
}
