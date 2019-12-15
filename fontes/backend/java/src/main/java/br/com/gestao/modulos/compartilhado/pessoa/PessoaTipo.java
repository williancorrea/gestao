package br.com.gestao.modulos.compartilhado.pessoa;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PessoaTipo {
    FISICA("FISICA"),
    JURIDICA("JURIDICA");

    private String descricao;

    PessoaTipo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
