package br.com.gestao.config.propriedades;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("gestao")
@Data
public class GestaoApiProperties {

    private boolean identificadorPadraoId  = false;
    private String nomeTeste = "";
}
