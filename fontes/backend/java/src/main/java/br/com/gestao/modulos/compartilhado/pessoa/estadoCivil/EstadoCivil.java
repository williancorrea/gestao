package br.com.gestao.modulos.compartilhado.pessoa.estadoCivil;

import br.com.gestao.utils.jpa.IdentificadorComum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Data
@Entity(name = "ESTADO_CIVIL")
public class EstadoCivil extends IdentificadorComum implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 5, max = 150)
    private String nome;

    @Size(max = 512)
    @Lob
    private String descricao;

    private boolean inativo;

    public EstadoCivil() {
    }
}
