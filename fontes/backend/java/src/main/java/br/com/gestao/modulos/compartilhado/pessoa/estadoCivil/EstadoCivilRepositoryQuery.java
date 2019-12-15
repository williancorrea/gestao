package br.com.gestao.modulos.compartilhado.pessoa.estadoCivil;

import br.com.gestao.utils.jpa.QueryFiltroPadrao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface EstadoCivilRepositoryQuery {
    Page<EstadoCivil> findAll(Pageable pageable, QueryFiltroPadrao filtro);
}
