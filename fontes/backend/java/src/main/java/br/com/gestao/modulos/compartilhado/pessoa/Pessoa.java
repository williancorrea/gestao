package br.com.gestao.modulos.compartilhado.pessoa;

import br.com.gestao.modulos.compartilhado.cidadeEstado.Cidade;
import br.com.gestao.utils.Utils;
import br.com.gestao.utils.jpa.IdentificadorComum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ToString(exclude = {"pessoaFisica"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity(name = "PESSOA")
@Data
public class Pessoa extends IdentificadorComum implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 5, max = 250)
    private String nome;

    @Size(max = 250)
    private String fantasia;

    @Lob
    private String imagem;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PessoaTipo tipo;

    @Size(max = 250)
    @Email
    private String email;

    @Size(max = 250)
    @URL
    private String site;

    @Size(max = 20)
    private String telefone1;

    @Size(max = 10)
    private String telefone1Obs;

    @Size(max = 20)
    private String telefone2;

    @Size(max = 10)
    private String telefone2Obs;

    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID")
    @ManyToOne()
    private Cidade cidade;

    @Size(max = 250)
    private String endereco;

    @Size(max = 100)
    private String bairro;

    @Size(max = 9)
    private String cep;

    @Lob
    @Column(name = "obs")
    private String obs;

    private boolean inativo;

    @JsonProperty("pessoaFisica")
    @JsonIgnoreProperties({"pessoa", "controle", "dataEmissaoRg",
            "naturalidade", "nacionalidade", "tipoSangue", "tituloEleitoralNumero", "tituloEleitoralZona", "tituloEleitoralSecao",
            "reservistaNumero", "reservistaCategoria", "estadoCivil",
    })
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    private PessoaFisica pessoaFisica;

    @JsonProperty("pessoaJuridica")
    @JsonIgnoreProperties({"pessoa", "tipoRegime", "crt", "suframa", "dataConstituicao"})
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    private PessoaJuridica pessoaJuridica;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(nome = "EMPRESA_PESSOA", joinColumns = { @JoinColumn(nome = "ID_PESSOA") }, inverseJoinColumns = { @JoinColumn(nome = "ID_EMPRESA") })
//    private Set<Empresa> listaEmpresa;

    @JsonIgnore
    @Column(name = "EMPRESA_REPRESENTANTE_COMERCIAL")
    private boolean empresaRepresentanteComercial;

    @JsonIgnore
    @Column(name = "EMPRESA_PRINCIPAL_OU_FILIAL")
    private boolean empresaPrincipaOulFilial;


    public Pessoa() {
    }

    @JsonIgnore
    @Transient
    public boolean isPessoaFisica() {
        return PessoaTipo.FISICA.equals(tipo);
    }

    @JsonIgnore
    @Transient
    public boolean isPessoaJuridica() {
        return PessoaTipo.JURIDICA.equals(tipo);
    }

    public String getTelefone1Formatado() {
        return Utils.formatarTelefone(this.telefone1);
    }

    public String getTelefone2Formatado() {
        return Utils.formatarTelefone(this.telefone2);
    }

}
