package br.com.gestao.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("gestao")
@Data
public class GestaoProperties {

    private String teste = "VALOR PADRAO";
}
