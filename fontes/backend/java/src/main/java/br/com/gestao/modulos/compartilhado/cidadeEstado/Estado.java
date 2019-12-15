package br.com.gestao.modulos.compartilhado.cidadeEstado;

import br.com.gestao.utils.jpa.IdentificadorComum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Data
@Entity(name = "ESTADO")
public class Estado extends IdentificadorComum implements Serializable {
    private static final long serialVersionUID = 9205916071703569260L;

    @NotBlank
    @Size(min = 3, max = 150)
    private String nome;

    @NotBlank
    @Size(min = 2, max = 2)
    private String iniciais;

    @Column(name = "codigo_ibge")
    private Integer codigoIbge;

    public Estado() {
    }
}
