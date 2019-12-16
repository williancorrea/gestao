package br.com.gestao.modulos.compartilhado.pessoa;

import br.com.gestao.modulos.compartilhado.cidadeEstado.Cidade;
import br.com.gestao.modulos.compartilhado.pessoa.estadoCivil.EstadoCivil;
import br.com.gestao.utils.jpa.IdentificadorComum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity(name = "PESSOA_FISICA")
@Data
public class PessoaFisica extends IdentificadorComum implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 14, max = 14)
    @CPF
    private String cpf;

    @Size(max = 13)
    private String rg;

    @Column(name = "ORGAO_RG")
    @Size(max = 10)
    private String orgaoRg;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_EMISSAO_RG")
    private Date dataEmissaoRg;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_NASCIMENTO")
    private Date dataNascimento;

    @Size(max = 1)
    private String sexo;

    @Size(max = 250)
    private String naturalidade;

    @Size(max = 250)
    private String nacionalidade;

    @Column(name = "TIPO_SANGUE")
    @Size(max = 5)
    private String tipoSangue;

    @Column(name = "CNH_NUMERO")
    @Size(max = 30)
    private String cnhNumero;

    @Column(name = "CNH_CATEGORIA")
    @Size(max = 2)
    private String cnhCategoria;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "CNH_VENCIMENTO")
    private Date cnhVencimento;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "CNH_PRIMEIRA_HABILITACAO")
    private Date cnhPrimeiraHabilitacao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "CNH_EMISSAO_DATA")
    private Date cnhEmissaoData;

    @JoinColumn(name = "ID_CNH_EMISSAO_CIDADE", referencedColumnName = "ID")
    @ManyToOne()
    private Cidade cnhEmissaoCidade;

    @Column(name = "MOTORISTA_INATIVO")
    private Boolean motoristaInativo;

    @Column(name = "TITULO_ELEITORAL_NUMERO")
    @Size(max = 30)
    private String tituloEleitoralNumero;

    @Column(name = "TITULO_ELEITORAL_ZONA")
    @Size(max = 3)
    private String tituloEleitoralZona;

    @Column(name = "TITULO_ELEITORAL_SECAO")
    @Size(max = 10)
    private String tituloEleitoralSecao;

    @Column(name = "RESERVISTA_NUMERO")
    @Size(max = 30)
    private String reservistaNumero;

    @Column(name = "RESERVISTA_CATEGORIA")
    @Size(max = 50)
    private String reservistaCategoria;

    @Column(name = "NOME_MAE")
    @Size(max = 250)
    private String nomeMae;

    @Column(name = "NOME_PAI")
    @Size(max = 250)
    private String nomePai;

    @JoinColumn(name = "ID_ESTADO_CIVIL", referencedColumnName = "id")
    @ManyToOne()
    @JsonIgnoreProperties({"controle", "descricao"})
    private EstadoCivil estadoCivil;

    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Pessoa pessoa;

    public PessoaFisica() {
    }

    public void setOrgaoRg(String orgaoRg) {
        if (orgaoRg != null) {
            this.orgaoRg = orgaoRg.toUpperCase();
        }
    }
}
