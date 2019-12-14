package br.com.gestao.config.propriedades;

import br.com.gestao.config.AutowireHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class GestaoApiPropertiesEntity {

    @Autowired
    private GestaoApiProperties gestaoApiProperties;

    public GestaoApiProperties getPropriedades() {
        AutowireHelper.autowire(this, this.gestaoApiProperties);
        return gestaoApiProperties;
    }
}
