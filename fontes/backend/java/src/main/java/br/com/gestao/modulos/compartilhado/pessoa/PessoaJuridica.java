package br.com.gestao.modulos.compartilhado.pessoa;

import br.com.gestao.utils.jpa.IdentificadorComum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity(name = "PESSOA_JURIDICA")
@Data
public class PessoaJuridica extends IdentificadorComum implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 18, max = 18)
    @CNPJ
    private String cnpj;

    @Column(name = "INSCRICAO_MUNICIPAL")
    @Size(max = 50)
    private String inscricaoMunicipal;

    @Column(name = "INSCRICAO_ESTADUAL")
    @Size(max = 50)
    private String inscricaoEstadual;

    @Column(name = "TIPO_REGIME")
    @Size(max = 20)
    private String tipoRegime;

    // Código de regime tributário: Simples Nacional
    @Column(name = "CRT")
    @Size(max = 50)
    private String crt;

    // Superintendência da Zona Franca de Manaus
    @Column(name = "SUFRAMA")
    @Size(max = 50)
    private String suframa;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CONSTITUICAO")
    private Date dataConstituicao;

    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private Pessoa pessoa;

    public PessoaJuridica() {
    }
}
