package br.com.gestao.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("gestao")
@Data
public class GestaoProperties {

    private String teste = "VALOR PADRAO";
    private String teste1 = "Valor de teste 1";
    private String teste2 = "Valor de teste 2";
}
