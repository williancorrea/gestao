package br.com.gestao.utils.jpa;

import br.com.gestao.config.propriedades.GestaoApiPropertiesEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@ToString
@EqualsAndHashCode
@MappedSuperclass
@Data
public abstract class IdentificadorComum implements Serializable {
    private static final long serialVersionUID = 3361676533116988939L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    @JsonIgnore
    private Long id;

    @JsonIgnore
    @Column(name = "UUID", nullable = false)
    private String uuid;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private String key;

    @Transient
    @JsonProperty(value = "key")
    public String getkeyOuID() {
        if (new GestaoApiPropertiesEntity().getPropriedades().isIdentificadorPadraoId()) {
            return getId().toString();
        }
        return getUuid();
    }

    @PrePersist
    private void gerarUuid() {
        setUuid(UUID.randomUUID().toString());
    }

}
