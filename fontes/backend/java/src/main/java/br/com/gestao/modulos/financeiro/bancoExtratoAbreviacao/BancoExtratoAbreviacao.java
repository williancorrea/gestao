package br.com.gestao.modulos.financeiro.bancoExtratoAbreviacao;

import br.com.gestao.modulos.financeiro.banco.Banco;
import br.com.gestao.utils.jpa.IdentificadorComum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity(name = "FIN_BANCO_EXTRATO_ABREVIACAO")
@Data
public class BancoExtratoAbreviacao extends IdentificadorComum implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ID_FIN_BANCO", referencedColumnName = "id", nullable = false)
    private Banco banco;

    @Size(max = 250)
    @NotBlank
    private String nomeReduzido;

    @Size(max = 250)
    @NotBlank
    private String nomeCompleto;

    public BancoExtratoAbreviacao() {
    }
}
