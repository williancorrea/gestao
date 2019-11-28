package br.com.gestao.modulos.financeiro.banco;

import br.com.gestao.utils.jpa.IdentificadorComum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity(name = "FIN_BANCO")
@Data
public class Banco extends IdentificadorComum implements Serializable {
    private static final long serialVersionUID = 1882328173575174836L;

    @Size(max = 10)
    private String codigo;

    @NotBlank
    @Size(min = 5, max = 150)
    private String nome;

    @Size(max = 250)
    private String url;

    @Lob
    private String logo;

    private boolean inativo;

    public Banco() {
    }
}
