package br.com.gestao.modulos.financeiro.bancoAgencia;

import br.com.gestao.modulos.financeiro.banco.Banco;
import br.com.gestao.utils.jpa.IdentificadorComum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity(name = "FIN_BANCO_AGENCIA")
@Data
public class BancoAgencia extends IdentificadorComum implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ID_FIN_BANCO", referencedColumnName = "id", nullable = false)
    private Banco banco;

    //TODO: criar a classe cidade
//    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "id")
//    @ManyToOne
//    @NotNull
//    private Cidade cidade;

    @Size(max = 50)
    @NotBlank
    private String codigo;

    @Size(max = 1)
    private String digito;

    @NotBlank
    @Size(min = 5, max = 100)
    private String nome;

    @Size(min = 5, max = 100)
    private String logradouro;

    @Size(min = 9, max = 9)
    private String cep;

    @Size(max = 100)
    private String bairro;

    @Size(max = 20)
    private String telefoneAgencia;

    @Size(max = 100)
    private String gerente;

    @Size(max = 20)
    private String telefoneGerente;

    @Lob
    private String obs;

    public BancoAgencia() {
    }
}
