package br.com.gestao.utils.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 36, max = 36)
    private String chave;

    @PrePersist
    private void gerarChave() {
        setChave(UUID.randomUUID().toString());
    }

    @JsonIgnore
    @Transient
    public boolean isEditando() {
        return id != null;
    }
}
