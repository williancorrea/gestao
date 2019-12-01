package br.com.gestao.utils.jpa;

import br.com.gestao.config.GestaoApiProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    @Transient
    @JsonIgnore
    private GestaoApiProperties apiProperty;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    @JsonIgnore
    private Long id;

    @NotBlank
    @Size(min = 36, max = 36)
    @Column(name = "UUID", nullable = false)
    private String uuid;

    @PrePersist
    private void gerarUuid() {
        setUuid(UUID.randomUUID().toString());
    }
}
