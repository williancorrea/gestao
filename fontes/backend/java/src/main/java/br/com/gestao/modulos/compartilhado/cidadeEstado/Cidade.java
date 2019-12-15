package br.com.gestao.modulos.compartilhado.cidadeEstado;

import br.com.gestao.utils.jpa.IdentificadorComum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Data
@Entity(name = "CIDADE")
public class Cidade extends IdentificadorComum implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 3, max = 150)
    private String nome;

    @Column(name = "CODIGO_IBGE")
    private Integer codigoIbge;

    @NotNull
    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Estado estado;

    public Cidade() {
    }
}
