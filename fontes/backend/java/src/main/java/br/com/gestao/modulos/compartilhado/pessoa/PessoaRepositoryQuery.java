package br.com.gestao.modulos.compartilhado.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PessoaRepositoryQuery {
    Page<Pessoa> findAll(Pageable pageable, PessoaRepositoryFiltro filtro);


}
