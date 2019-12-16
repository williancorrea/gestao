package br.com.gestao.modulos.compartilhado.pessoa;

import br.com.gestao.utils.jpa.QueryFiltroPadrao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaRepositoryFiltro extends QueryFiltroPadrao {

    private String nome;
    private boolean motorista;
    private boolean cnh;
    private boolean representanteComercial;
    private boolean empresaPrincipalOuFilial;
}
