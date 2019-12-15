package br.com.gestao.modulos.compartilhado.cidadeEstado;

import br.com.gestao.utils.jpa.QueryFiltroPadrao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CidadeRepositoryQuery {
    Page<Cidade> findAll(Pageable pageable, QueryFiltroPadrao filtro);
}
