package br.com.gestao.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

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

    @Transient
    public String getKey() {
        return this.id.toString();
    }

    @Transient
    public void setKey(String key) {
        try {
            if (key == null) {
                this.id = null;
            }
            this.id = Long.parseLong(key);
        } catch (Exception e) {
            this.id = null;
        }
    }

    @JsonIgnore
    @Transient
    public boolean isEditando() {
        return id != null;
    }
}
