package br.com.gestao.modulos.financeiro.banco;

import br.com.gestao.utils.jpa.QueryFiltroPadrao;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BancoRepositoryFiltro extends QueryFiltroPadrao {

    private String codigo;
    private String nome;
    private String url;
}
